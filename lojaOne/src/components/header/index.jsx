import { Link } from 'react-router-dom';
import './header.css';
import avatar from '../img/icons/entrar-avatar.png';
import carrinhos from '../img/icons/carrinho-de-compras.png';
import editar from '../img/icons/editar.png';
import { useAuth } from '../../service/AuthContext'; // Importe o contexto de autenticação

export default function Header() {
  const { user } = useAuth(); // Acesse as informações do usuário

  // Verifica se o usuário tem a ROLE_FUNCIONARIO
  const isFuncionario = user && user.roles && user.roles.includes('ROLE_FUNCIONARIO');
  const isCliente = user && user.roles && user.roles.includes('ROLE_CLIENTE');

  return (
    <header>
      <div className="conteiner2">
        <div className="local_logo">
          <Link to='/' className="logo">FIEB</Link>
        </div>
        <div className="local_icone">
          <Link to={user ? '/perfil' : '/cadastro'} className="direcao">
            <img src={avatar} alt="avatar" className="avatar" />
          </Link>
          {isCliente && (
            <Link to={'/carrinho'} className="direcao">
              <img src={carrinhos} alt="carrinho" className="avatar" />
            </Link>
          )}
          {isFuncionario && (
            <>
              <Link to='/produto' className="direcao">
                <img src={editar} alt="editar produto" className="avatar" />
              </Link>
              <Link to={'/listar-cliente'} className="direcao">
                <img src={carrinhos} alt="clientes" className="avatar" />
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}