import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; // Importando useNavigate para redirecionamento
import './inicio.css';
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import d from "../../components/img/icons/logar.png";
import Slider from "react-slick";

export default function Inicio() {
  const navigate = useNavigate(); // Hook para navegação

  useEffect(() => {
    // Verifica se o token está presente no armazenamento local ou na sessão
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (token) {
      navigate('/rolon'); // Redireciona para a página rolon se o token existir
    }
  }, [navigate]);  //O array de dependências inclui navigate

  const setting = {
    dots: true,
    infinite: true,
    arrows: false,
    autoplay: true,
    autoplaySpeed: 300,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1
  };

  return (
    <div className="container_page">
      <div className='container3'>
        <div className="teste">
          <div className='frases_inicio'>
            <h1 className='titulo_inicio'>Tudo o que você<br /> precisa para a escola</h1>
            <p className='subtitulo_inicio'>Explore our curated selection of high-quality school supplies for all your educational needs.</p>            
            <button className='btn_inicio'>COMPRE AGORA</button>
          </div>

          <div className="slider_inicio">
            <Slider {...setting}>
              <div className="carousel_inicio">
                <img src={d} alt="" className='img_inicio' />
              </div>

              <div className="carousel_inicio">
                <img src={d} alt="" className='img_inicio' />
              </div>

              <div className="carousel_inicio">
                <img src={d} alt="" className='img_inicio' />
              </div>
            </Slider>
          </div>
        </div>
      </div>

      <div className="container4">
        <h1 className='titulo_carde'>OS melhores</h1>
        <div className="v">
          <div className="caixa">
            <div className="carde">
              <div className="caixaImg"></div>
              <div className="conteudo"></div>
            </div>

            <div className="carde">
              <div className="caixaImg"></div>
              <div className="conteudo"></div>
            </div>

            <div className="carde">
              <div className="caixaImg"></div>
              <div className="conteudo"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}