import { useState } from "react";
import "./cadastro.css";
import { setAuthToken } from '../../service/api';
import api from '../../service/api';
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode"; 
import envelope from '../../components/img/icons/envelope.png';
import senh from '../../components/img/icons/trancar.png';
import avatar from '../../components/img/icons/entrar-avatar.png';
import deco from '../../components/img/icons/deco.png';
import { useAuth } from '../../service/AuthContext'; 

const Cadastro = () => {
  const [isSignUpMode, setIsSignUpMode] = useState(false);
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [rm, setRm] = useState("")
  const [message, setMessage] = useState(""); // Estado para mensagens

  const navigate = useNavigate();
  const { login } = useAuth(); // Obter a função de login do contexto

  // Função para lidar com o envio do formulário de cadastro
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post("users/cliente", {
        nome,
        email,
        senha,
        tipoUsuario: "Cliente",
      });
      console.log(response.data);
      setMessage("Cadastro realizado com sucesso!");
      setIsSignUpMode(false); // Muda para o modo de login após cadastro
    } catch (error) {
      console.error('Erro ao cadastrar:', error);
      setMessage("Erro ao cadastrar: " + (error.response?.data?.message || "Email já cadastrado."));
    }
  };
  
  // Função para lidar com o envio do formulário de login
  const handleLogin = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('email', email);
    formData.append('senha', senha);
  
    try {
      const response = await api.post('login', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
      });
      
      const token = response.data.access_token; // Verifique se o token está sendo retornado corretamente
  
      if (token) {
        localStorage.setItem('token', token);
        sessionStorage.setItem('token', token);
        setAuthToken(token); // Configura o token para futuras requisições
  
        const decoded = jwtDecode(token);
        login(decoded, token); // Realiza o login no contexto
        
        // Verifica os papéis do usuário
        if (decoded.roles.includes('ROLE_CLIENTE')) {
          setMessage("Login realizado com sucesso!");
          navigate('/rolon');
        } else if (decoded.roles.includes('ROLE_FUNCIONARIO')) {
          setMessage("Login realizado com sucesso!");
          navigate('/rolon');
        } else {
          console.log('Papel do usuário não reconhecido');
        }
      } else {
        console.error('Token não encontrado na resposta');
        setMessage("Erro ao fazer login: Token não encontrado.");
      }
    } catch (error) {
      console.error('Erro ao fazer login:', error);
      setMessage("Erro ao fazer login: " + (error.response?.data?.message || "Credenciais inválidas."));
    }
  };

  return (
    <div className={`container ${isSignUpMode ? 'sign-up-mode' : ''}`}>
      <div className="forms-container">
        <div className="signin-signup">
          {/* Formulário de Login */}
          <form onSubmit={handleLogin} className={`sign-in-form ${isSignUpMode ? 'hidden' : ''}`}>
            <h2 className="title">Login</h2>
            <div className="input-field">
              <img src={envelope} alt="icon" className="icone"/>
              <input type="email" placeholder="Email" onChange={(e) => setEmail(e.target.value)} required />
            </div>
            <div className="input-field">
              <img src={senh} alt="icon" className="icone"/>
              <input type="password" placeholder="Senha" onChange={(e) => setSenha(e.target.value)} required />
            </div>
            <button type="submit" className="btn solid">Login</button>
            {message && <p style={{ color: 'green' }}>{message}</p>} {/* Mensagem de sucesso */}
          </form>

          {/* Formulário de Cadastro */}
          <form onSubmit={handleSubmit} className={`sign-up-form ${isSignUpMode ? '' : 'hidden'}`}>
            <h2 className="title">Cadastro</h2>
            <div className="input-field">
              <img src={avatar} alt="icon" className="icone"/>
              <input type="text" placeholder="Nome" onChange={(e) => setNome(e.target.value)} required />
            </div>
            <div className="input-field">
              <img src={envelope} alt="icon" className="icone"/>
              <input type="email" placeholder="Email" onChange={(e) => setEmail(e.target.value)} required />
            </div>
            <div className="input-field">
              <img src={envelope} alt="icon" className="icone"/>
              <input type="email" placeholder="Email" onChange={(e) => setRm(e.target.value)} required />
            </div>
            <div className="input-field">
              <img src={senh} alt="icon" className="icone"/>
              <input type="password" placeholder="Senha" onChange={(e) => setSenha(e.target.value)} required />
            </div>
            <button type="submit" className="btn solid">Cadastrar</button>
            {message && <p style={{ color: 'red' }}>{message}</p>} {/* Mensagem de erro */}
          </form>
        </div>
      </div>

      {/* Painéis de Alternância entre Login e Cadastro */}
      <div className="panels-container">
        {/* Painel de Cadastro */}
        <div className={`panel left-panel ${isSignUpMode ? '' : 'hidden'}`}>
          <div className="content">
            <h3>Novo aqui?</h3>
            <p>Crie uma conta e comece a utilizar nossos serviços!</p>
            <button className="btn transparent" onClick={() => setIsSignUpMode(true)}>Cadastrar</button>
          </div>
          <img src={deco} alt="deco" className="image" />
        </div>

        {/* Painel de Login */}
        <div className={`panel right-panel ${isSignUpMode ? 'hidden' : ''}`}>
          <div className="content">
            <h3>Já tem uma conta?</h3>
            <p>Faça login e continue onde você parou!</p>
            <button className="btn transparent" onClick={() => setIsSignUpMode(false)}>Login</button>
          </div>
          <img src={deco} alt="deco" className="image" />
        </div>
      </div>
    </div>
  );
};

export default Cadastro;