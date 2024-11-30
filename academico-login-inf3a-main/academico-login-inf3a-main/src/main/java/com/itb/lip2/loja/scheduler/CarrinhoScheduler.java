package com.itb.lip2.loja.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.itb.lip2.loja.service.CarrinhoService;

@Component
public class CarrinhoScheduler {

    @Autowired
    private CarrinhoService carrinhoService;

    @Scheduled(cron = "0 0 0 * * ?") // Executa à meia-noite todos os dias
    public void resetarCarrinhos() {
        carrinhoService.resetarCarrinhos(); // Chama o método do serviço para resetar os carrinhos
    }
}