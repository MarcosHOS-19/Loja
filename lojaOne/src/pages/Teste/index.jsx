import { useAuth } from '../../service/AuthContext';
import React, { useEffect, useState } from 'react';
import api from '../../service/api';
import { jwtDecode } from "jwt-decode";
import Slider from "react-slick"; // Importando o Slider
import './Rolon.css';

export default function Rolon() {
  const { user, logout } = useAuth();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [quantidadeDesejada, setQuantidadeDesejada] = useState({});

  useEffect(() => {
    const tokenLocal = localStorage.getItem("token");
    const tokenSession = sessionStorage.getItem("token");
    const token = tokenLocal || tokenSession;

    if (!token) {
      logout(); // Chama a função de logout se não houver token
      return; // Não continua se não estiver logado
    }

    const decodedToken = jwtDecode(token);
    const userId = decodedToken.sub; // O ID do usuário

    if (!userId) {
      console.error("ID do usuário não encontrado no token.");
      logout(); // Logout se o ID do usuário não puder ser encontrado
      return;
    }

    const fetchProducts = async () => {
      try {
        const response = await api.get('users/produtos');
        console.log("Resposta da API:", response.data); // Verifique o que está sendo retornado
        setProducts(response.data);
      } catch (err) {
        console.error("Erro ao buscar produtos:", err);
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [logout]);

  const adicionarAoCarrinho = async (produtoId) => {
    if (!user) {
      alert("Você precisa estar logado para adicionar produtos ao carrinho.");
      return;
    }

    // Verifica se o usuário tem a role ROLE_CLIENTE
    if (!user.roles || !user.roles.includes("ROLE_CLIENTE")) {
      alert("Você não tem permissão para adicionar produtos ao carrinho.");
      return;
    }

    const data = {
      id: produtoId,
      quantidadeDesejada: quantidadeDesejada[produtoId] || 1
    };

    try {
      const tokenLocal = localStorage.getItem("token");
      const tokenSession = sessionStorage.getItem("token");
      const token = tokenLocal || tokenSession;

      const decodedToken = jwtDecode(token);
      const userId = decodedToken.sub;

      if (!userId) {
        console.error("ID do usuário não encontrado no token.");
        return;
      }

      // Adiciona o token no cabeçalho da requisição
      await api.post(`cliente/${userId}/adicionar`, data, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      alert("Produto adicionado ao carrinho com sucesso!");
    } catch (err) {
      alert("Erro ao adicionar produto ao carrinho: " + (err.response?.data?.message || "Erro desconhecido"));
    }
  };

  // Agrupando produtos por descrição
  const groupedProducts = products.reduce((acc, product) => {
    (acc[product.descricao] = acc[product.descricao] || []).push(product);
    return acc;
  }, {});

  return (
    <div className="coi">
                
      {loading && <p className='null'>Carregando produtos...</p>}
      {error && <p className='null'>Erro ao carregar produtos: {error.message}</p>}

      {!loading && !error && products.length === 0 && <p className='null'>Nenhum produto disponível.</p>}
      {!loading && !error && products.length > 0 && (
        <div className='vou'>
          <h2 className='null'>Lista de Produtos</h2>
          {Object.keys(groupedProducts).map(descricao => (
            <div className='onu' key={descricao}>
              <h3>{descricao}</h3>

               <Slider {...{
                dots: true,
                infinite: false,
                speed: 500,
                slidesToShow: 3,
                slidesToScroll: 1,
                responsive: [
                  {
                    breakpoint: 1024,
                    settings: {
                      slidesToShow: 2,
                      slidesToScroll: 1,
                      infinite: false,
                      dots: true
                    }
                  },
                  {
                    breakpoint: 600,
                    settings: {
                      slidesToShow: 1,
                      slidesToScroll: 1
                    }
                  }
                ]
              }}>
                {groupedProducts[descricao].map(product => {
                  const imageUrl = `http://192.168.0.8:8080/academico/api/v1/users/imagens/${product.imagem}`// Ajuste a URL conforme necessário
                  return (
                    <div key={product.id} className="card">
                      <img src={imageUrl} alt={product.nome} className='imag-card' />
                      <h3>{product.nome}</h3>
                      <p>Preço: R$ {product.preco}</p>
                      <input
                        type="number"
                        min="1"
                        defaultValue="1"
                        onChange={(e) => setQuantidadeDesejada({ ...quantidadeDesejada, [product.id]: e.target.value })}
                      />
                      <button onClick={() => adicionarAoCarrinho(product.id)}>Adicionar ao Carrinho</button>
                    </div>
                  );
                })}
              </Slider>
             
            </div>
          ))}
        </div>
      )}
    </div>
  );
}