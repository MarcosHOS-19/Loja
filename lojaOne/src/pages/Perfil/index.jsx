import { useState, useEffect } from "react";
import "./perfil.css";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import api, { setAuthToken } from "../../service/api"; 
import { useAuth } from "../../service/AuthContext";

export default function Perfil() {
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [rm, setRm] = useState("");
  const [senha, setSenha] = useState("");
  const navigate = useNavigate();
  const { user, logout } = useAuth(); // Adicione logout aqui

  // Verifica se o usuário é um cliente
  const isCliente = user?.roles.includes("ROLE_CLIENTE");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const tokenLocal = localStorage.getItem("token");
        const tokenSession = sessionStorage.getItem("token");
        const token = tokenLocal || tokenSession;

        const decodedToken = jwtDecode(token);
        const userId = decodedToken.sub; // O ID do usuário

        if (!userId) {
          console.error("ID do usuário não encontrado no token.");
          return;
        }

        const response = await api.get(`users/${userId}`); // Aqui estamos usando o método GET

        // Definindo os dados do usuário nos estados
        setNome(response.data.nome);
        setEmail(response.data.email);
        setRm(response.data.rm);
        setSenha(response.data.senha);
      } catch (error) {
        console.error("Erro ao buscar dados do perfil:", error);
      }
    };
  
    fetchData();
  }, [user]);

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
        const token = localStorage.getItem("token") || sessionStorage.getItem("token");
        setAuthToken(token); // Configurando o token para a requisição

        const decodedToken = jwtDecode(token);
        const userId = decodedToken.sub; // O ID do usuário
        const roles = decodedToken.roles || []; 

        // Configurando os cabeçalhos
        const config = {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        };

        let response;
        let data;

        // Criando o corpo da requisição de acordo com o papel do usuário
        if (roles.includes("ROLE_CLIENTE")) {
            data = {
                tipoUsuario: "Cliente", // Adicione esta linha
                nome,
                email,
                rm, // Certifique-se de que 'rm' é um campo esperado no servidor
                senha,
            };
            response = await api.put(`cliente/${userId}`, data, config);
        } else if (roles.includes("ROLE_FUNCIONARIO")) {
            data = {
                tipoUsuario: "Funcionario", // Se necessário
                nome,
                email,
                senha,
            };
            response = await api.put(`funcionario/${userId}`, data, config);
        } else {
            console.error("Papel do usuário não reconhecido");
            return;
        }

        if (response.status === 200) {
            console.log("Perfil atualizado com sucesso!");
        }
    } catch (error) {
        console.error("Erro ao atualizar perfil:", error);
        if (error.response) {
            console.error("Detalhes do erro:", error.response.data);
        }
    }
};

  const handleLogout = () => {
    logout(); // Chama a função de logout
    navigate("/cadastro"); // Redireciona para a página de login
  };

  return (
    <div className="container_page_perfil">
      <h2>Perfil do {nome || user?.nome}</h2> 
      <form onSubmit={handleUpdate} className="perfil-form">
        <div className="input-field">
          <label style={{ fontSize: '18px', fontWeight: 'bold', marginBottom: '10px' }}>Nome:</label>
          <input
            type="text"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            style={{ padding: '10px', fontSize: '16px', borderRadius: '5px', border: '1px solid #ccc', width: '100%' }}
          />
        </div>
        <div className="input-field">
          <label style={{ fontSize: '18px', fontWeight: 'bold', marginBottom: '10px' }}>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            style={{ padding: '10px', fontSize: '16px', borderRadius: '5px', border: '1px solid #ccc', width: '100%' }}
          />
        </div>
        {/* Renderiza o campo RM apenas se o usuário for CLIENTE */}
        {isCliente && (
          <div className="input-field">
            <label style={{ fontSize: '18px', fontWeight: 'bold', marginBottom: '10px' }}>RM:</label >
            <input
              type="text"
              value={rm}
              onChange={(e) => setRm(e.target.value)}
              style={{ padding: '10px', fontSize: '16px', borderRadius: '5px', border: '1px solid #ccc', width: '100%' }}
            />
          </div>
        )}
        <div className="input-field">
          <label style={{ fontSize: '18px', fontWeight: 'bold', marginBottom: '10px' }}>Senha:</label>
          <input
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            style={{ padding: '10px', fontSize: '16px', borderRadius: '5px', border: '1px solid #ccc', width: '100%' }}
         disabled />
        </div>
        <button type="submit" className="btn-perfil">
          Atualizar Perfil
        </button>
        <button onClick={handleLogout} style={{ backgroundColor: 'yellow', color: 'black', padding: '10px', borderRadius: '5px', border: 'none', cursor: 'pointer' }}>
          Logout
        </button>
      </form>
    </div>
  );
}