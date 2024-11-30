import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom'; // Importar Link
import api from '../../service/api';
import { useAuth } from '../../service/AuthContext';
import './ListaProdutos.css';

const ListarProduto = () => {
    const [produtos, setProdutos] = useState([]); // Estado para armazenar os produtos
    const [loading, setLoading] = useState(true); // Estado para indicar se está carregando
    const [error, setError] = useState(null); // Estado para armazenar erros

    const { user } = useAuth(); // Obter informações do usuário autenticado

    useEffect(() => {
        const fetchProdutos = async () => {
            try {
                const token = localStorage.getItem("token");
                const response = await api.get("funcionario/produto", {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                setProdutos(response.data);
            } catch (error) {
                console.error("Erro ao buscar produtos:", error);
                setError(error); // Armazena o erro para exibição
            } finally {
                setLoading(false); // Define loading como false após a requisição
            }
        };

        fetchProdutos();
    }, []);

    const handleDelete = async (id) => {
        const confirmDelete = window.confirm("Você tem certeza que deseja deletar este produto?");
        if (!confirmDelete) return;

        try {
            const token = localStorage.getItem("token");
            await api.delete(`funcionario/produto/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setProdutos(produtos.filter(produto => produto.id !== id)); // Remove o produto da lista
        } catch (error) {
            console.error("Erro ao deletar produto:", error);
            alert("Erro ao deletar produto: " + (error.response?.data?.message || "Erro desconhecido"));
        }
    };

    const handleEdit = (id) => {
        // Aqui você pode redirecionar para uma página de edição ou abrir um modal
        // Exemplo: history.push(`/editar-produto/${id}`);
        alert(`Redirecionar para editar o produto com ID: ${id}`);
    };

    return (
        <div className="container2">
            <h2>Lista de Produtos</h2>
            <div className="button-group">
                <Link to="/listar-pedido">
                    <button>Listar Pedido</button>
                </Link>
                <Link to="/listar-cliente">
                    <button>Listar Cliente</button>
                </Link>
            </div>
            {loading && <p>Carregando produtos...</p>}
            
            {produtos.length > 0 && (
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Descrição</th>
                            <th>Preço</th>
                            <th>Quantidade</th>
                            <th>Ações</th> {/* Nova coluna para ações */}
                        </tr>
                    </thead>
                    <tbody>
                        {produtos.map(produto => (
                            <tr key={produto.id}>
                                <td>{produto.id}</td>
                                <td>{produto.nome}</td>
                                <td>{produto.descricao}</td>
                                <td>R$ {produto.preco.toFixed(2)}</td>
                                <td>{produto.quantidade}</td>
                                <td>
                                    <button onClick={() => handleEdit(produto.id)}>Editar</button>
                                    <button onClick={() => handleDelete(produto.id)}>Deletar</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default ListarProduto;