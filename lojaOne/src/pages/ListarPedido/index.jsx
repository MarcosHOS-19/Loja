import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom'; // Importar Link
import api from '../../service/api';
import { useAuth } from '../../service/AuthContext';
import './ListarPedido.css';

const ListarPedidos = () => {
    const [clientes, setClientes] = useState([]);
    const [compras, setCompras] = useState([]);
    const [clienteAtivo, setClienteAtivo] = useState(null); // Estado para armazenar o cliente ativo
    const [nomeClienteAtivo, setNomeClienteAtivo] = useState(""); // Estado para armazenar o nome do cliente ativo
    const { user } = useAuth(); // Obter informações do usuário autenticado

    useEffect(() => {
        const fetchClientes = async () => {
            try {
                const token = localStorage.getItem("token");
                const response = await api.get("funcionario/clientes/compras-hoje", {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                setClientes(response.data);
            } catch (error) {
                console.error("Erro ao buscar clientes:", error);
            }
        };

        fetchClientes();
    }, []);

    const handleViewCart = async (clienteId, clienteNome) => {
        if (clienteAtivo === clienteId) {
            // Se o cliente ativo é o mesmo que o clicado, oculta as compras
            setCompras([]);
            setClienteAtivo(null);
            setNomeClienteAtivo(""); // Limpa o nome do cliente ativo
        } else {
            try {
                const token = localStorage.getItem("token");
                const response = await api.get(`funcionario/compras/${clienteId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                setCompras(response.data);
                setClienteAtivo(clienteId); // Define o cliente ativo
                setNomeClienteAtivo(clienteNome); // Define o nome do cliente ativo
            } catch (error) {
                console.error("Erro ao buscar compras do cliente:", error);
            }
        }
    };

    return (
        <div className="container2">
            <h2>Lista de Pedidos</h2>
            <div className="button-group">
                <Link to="/listar-cliente">
                    <button>Listar Cliente</button>
                </Link>
                <Link to="/listar-produtos">
                    <button>Listar Produtos</button>
                </Link>
            </div>
            {compras.length > 0 && clienteAtivo && (
                <div className="card1">
                    <h3>Compras do Cliente {nomeClienteAtivo}</h3>
                    <ul>
                        {compras.map(compra => (
                            <li key={compra.id}>
                                <p>ID da Compra: {compra.id}</p>
                                <p>Data da Compra: {new Date(compra.dataCompra).toLocaleDateString()}</p>
                                <p>Total: R$ {compra.total.toFixed(2)}</p>
                                <ul>
                                    {compra.produtos.map(produto => (
                                        <li key={produto.id}>
                                            {produto.nome} - {produto.quantidade} x R$ {produto.valor.toFixed(2)}
                                        </li>
                                    ))}
                                </ul>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {clientes.map(cliente => (
                        <tr key={cliente.id}>
                            <td>{cliente.id}</td>
                            <td>{cliente.nome}</td>
                            <td>{cliente.rm}</td>
                            <td>
                                <button onClick={() => handleViewCart(cliente.id, cliente.nome)}>Ver Carrinho</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ListarPedidos;