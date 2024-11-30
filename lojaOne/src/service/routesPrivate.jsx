import { useEffect } from "react";
import { useAuth } from './AuthContext';
import { Navigate, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

const PrivateRoute = () => {
  const { user, login, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');

    if (!token) {
      console.log("Token não encontrado, redirecionando para o cadastro.");
      navigate('/cadastro');
      return;
    }

    try {
      const decoded = jwtDecode(token);
      const isExpired = Date.now() >= decoded.exp * 1000; // Verifica se o token expirou

      if (isExpired) {
        console.log("Token expirado, realizando logout.");
        logout(); // Chama a função de logout
        navigate('/cadastro'); // Redireciona para a página de cadastro
        return;
      }

      // Se o usuário não estiver logado, faça o login
      if (!user) {
        login(decoded);
      }
    } catch (error) {
      console.error("Erro ao decodificar o token:", error);
      logout(); // Logout se o token não puder ser decodificado
      navigate('/cadastro'); // Redireciona para a página de cadastro
    }
  }, [navigate, login, logout, user]);

  // Se o usuário estiver logado, renderize o Outlet (página protegida)
  if (user) {
    return <Outlet />;
  }

  // Se o usuário não estiver logado, redirecione para a mesma página que ele estava
  "return <Navigate to={location.pathname} replace />;"
};

export default PrivateRoute;