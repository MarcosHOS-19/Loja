// src/routes.jsx

import { BrowserRouter, Routes, Route } from "react-router-dom";
import Inicio from "./pages/inicio/index";
import Cadastro from "./pages/cadastro/index";
import Produto from "./pages/cadastroProduto/index";
import Layout from "./components/layout";
import Rolon from "./pages/Teste/index";
import PrivateRoute from './service/routesPrivate';
import Perfil from "./pages/Perfil/index";
import ListarClientes from "./pages/ListarCliente/index";
import ListarPedidos from "./pages/ListarPedido/index"
import ListarProduto from "./pages/ListarProduto/index";  
import Carrinho from "./pages/carrinho/index";  

export default function Routessistem() {
  return (
    <BrowserRouter>
      <Layout />
      <Routes>
        <Route path='/' element={<Inicio />} />
        <Route path='/cadastro' element={<Cadastro />} />
        <Route path='/rolon' element={<Rolon />} />
        <Route path='/perfil' element={<Perfil/>} />
        <Route path='/listar-cliente' element={<ListarClientes/>} />
        <Route path='/listar-pedido' element={<ListarPedidos/>} />
        <Route path='/produto' element={<Produto/>}/>
        <Route path='/listar-produtos' element={<ListarProduto/>}/>
        <Route path='/carrinho' element={<Carrinho/>}/>

      </Routes>
    </BrowserRouter>
  );
}