import React, { useEffect, useState } from 'react';
import { useAuth } from '../../service/AuthContext';
import api from '../../service/api';
import { jwtDecode } from "jwt-decode";
import './Carrinho.css';

const Carrinho = () => {
    const { user, logout } = useAuth();
    const [carrinho, setCarrinho] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [valorTotal, setValorTotal] = useState(0); // Estado para o valor total

    useEffect(() => {
        const tokenLocal = localStorage.getItem("token");
        const tokenSession = sessionStorage.getItem("token");
        const token = tokenLocal || tokenSession;

        if (!token) {
            logout();
            return;
        }

        const decodedToken = jwtDecode(token);
        const userId = decodedToken.sub;

        if (!userId) {
            console.error("ID do usuário não encontrado no token.");
            logout();
            return;
        }

        const fetchCarrinho = async () => {
            try {
                const response = await api.get(`cliente/${userId}/carrinho`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                setCarrinho(response.data.produtos); // Ajuste para acessar a lista de produtos
            } catch (err) {
                console.error(err);
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchCarrinho();
    }, [logout]);

    useEffect(() => {
        // Calcular o valor total sempre que o carrinho mudar
        const total = carrinho.reduce((acc, produto) => {
            return acc + produto.valor; // Soma apenas o valor do produto
        }, 0);
        setValorTotal(total);
    }, [carrinho]);

    const removerQuantidade = async (produtoId, quantidadeDesejada) => {
        const tokenLocal = localStorage.getItem("token");
        const tokenSession = sessionStorage.getItem("token");
        const token = tokenLocal || tokenSession;
    
        const decodedToken = jwtDecode(token);
          const userId = decodedToken.sub;
    
        try {
            await api.patch(`cliente/${userId}/remover-quantidade/${produtoId}?quantidade=${quantidadeDesejada}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
    
            setCarrinho(prevCarrinho => {
                return prevCarrinho.map(item => {
                    if (item.id === produtoId) {
                        const novaQuantidade = item.quantidadeDesejada - quantidadeDesejada;
                        if (novaQuantidade <= 0) {
                            return null; // Retorna null se a quantidade for zero ou negativa
                        }
                        return { ...item, quantidadeDesejada: novaQuantidade }; // Atualiza a quantidade
                    }
                    return item;
                }).filter(item => item !== null); // Remove os produtos que têm quantidade zero
            });
    
            setTimeout(() => {
                window.location.reload();
            }, 2000);
        } catch (err) {
            alert("Erro ao remover produto: " + (err.response?.data?.message || "Erro desconhecido"));
        }
    };

    const finalizarCompra = async () => {
        const tokenLocal = localStorage.getItem("token");
        const tokenSession = sessionStorage.getItem("token");
        const token = tokenLocal || tokenSession;

        const decodedToken = jwtDecode(token);
        const userId = decodedToken.sub;

        try {
            await api.post(`cliente/${userId}/finalizar-compra`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            alert("Compra finalizada com sucesso!");
            // Refresh na página após 2 segundos
            setTimeout(() => {
                window.location.reload();
            }, 2000);
        } catch (err) {
            if (err.response && err.response.status === 500) {
                setError("Não há produtos no carrinho.");
            } else {
                alert("Erro ao finalizar compra: " + (err.response?.data?.message || "Erro desconhecido"));
            }
        }
    };

    if (carrinho.length === 0) {
        return <p>Nenhum produto no carrinho.</p>;
    }

    return (
        <div className="carrinho-container">
            <h2>Carrinho de Compras</h2>
            {loading && <p>Carregando carrinho...</p>}
            {error && <p>Erro ao carregar carrinho: {error.message}</p>}
            <table className="carrinho-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Preço</th>
                        <th>Quantidade</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {carrinho.map(produto => (
                        <tr key={produto.id}>
                            <td>{produto.id}</td>
                            <td>{produto.nome}</td>
                            <td>R$ {produto.valor !== undefined ? produto.valor.toFixed(2) : 'N/A'}</td> {/* Verificação para evitar erro */}
                            <td>{produto.quantidadeDesejada}</td>
                            <td>
                                <button onClick={() => removerQuantidade(produto.id, 1)}>Remover 1</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <p>Valor total: R$ {valorTotal.toFixed(2)}</p>
            <button onClick={finalizarCompra}>Finalizar Compra</button>
        </div>
    );
};

export default Carrinho;