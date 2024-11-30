import { useState } from "react";
import "./cadastroProduto.css";
import { useNavigate } from "react-router-dom";
import api from "../../service/api";
import { setAuthToken } from "../../service/api";
import { jwtDecode } from "jwt-decode";
import { useAuth } from '../../service/AuthContext'; 

const Produto = () => {
  const [nome, setNome] = useState("");
  const [descricao, setDescricao] = useState("");
  const [preco, setPreco] = useState("");
  const [quantidade, setQuantidade] = useState("");
  const [imagem, setImagem] = useState(null); // Use state for the image file
  const [message, setMessage] = useState("");

  const navigate = useNavigate();
  const { login } = useAuth(); 

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    // Validação de dados
    if (!nome || !descricao || !preco || !quantidade || !imagem) {
      setMessage("Todos os campos são obrigatórios.");
      return;
    }
  
    // Prepare form data for upload
    const formData = new FormData();
    formData.append('nome', nome);
    formData.append('descricao', descricao);
    formData.append('preco', preco);
    formData.append('quantidade', quantidade);
    formData.append('imagem', imagem);
  
    try {
      const token = localStorage.getItem("token");
      setAuthToken(token);
      const response = await api.post("funcionario/produto", formData, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      console.log(response.data);
      setMessage("Produto cadastrado com sucesso!");
      navigate("/produtos");
    } catch (error) {
      console.error("Erro ao cadastrar produto:", error);
      setMessage(
        "Erro ao cadastrar produto: " +
        (error.response?.data?.message || "Erro desconhecido")
      );
    }
  };

  return (
    <div className="container1">
      <form onSubmit={handleSubmit} className="produto-form">
        <h2 className="title">Cadastro de Produto</h2>
        <div className="input-field1">
          <label htmlFor="nome">Nome:</label>
          <input type="text" id="nome" value={nome} onChange={(e) => setNome(e.target.value)} required />
        </div>
        <div className="input-field1">
          <label htmlFor="descricao">Descrição:</label>
          <input type="text" id="descricao" value={descricao} onChange={(e) => setDescricao(e.target.value)} required />
        </div>
        <div className="input-field1">
          <label htmlFor="preco">Preço:</label>
          <input type="number" id="preco" value={preco} onChange={(e) => setPreco(e.target.value)} required />
        </div>
        <div className="input-field1">
          <label htmlFor="quantidade">Quantidade:</label>
          <input type="number" id="quantidade" value={quantidade} onChange={(e) => setQuantidade(e.target.value)} required />
        </div>
        <div className="input-field1">
          <label htmlFor="imagem">Imagem:</label>
          <input type="file" id="imagem" onChange={(e) => setImagem(e.target.files[0])} required />
        </div>
        <button type="submit" className="btn2 solid">Cadastrar Produto</button>
        {message && <p style={{ color: message.includes("Erro") ? "red" : "green" }}>{message}</p>}
      </form>
    </div>
  );
};

export default Produto;