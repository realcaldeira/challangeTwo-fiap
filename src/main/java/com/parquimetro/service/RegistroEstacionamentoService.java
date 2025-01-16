package com.parquimetro.service;

import com.parquimetro.EstacionamentoException;
import com.parquimetro.dto.ValorDevidoDTO;
import com.parquimetro.model.RegistroEstacionamento;
import com.parquimetro.model.Veiculo;
import com.parquimetro.repository.RegistroEstacionamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistroEstacionamentoService {

    @Autowired
    private RegistroEstacionamentoRepository registroEstacionamentoRepository;

    @Autowired
    private VeiculoService veiculoService;

    public RegistroEstacionamento iniciarEstacionamento(String placa) {
        Veiculo veiculo = veiculoService.buscarVeiculoPorPlaca(placa)
                .orElseGet(() -> {
                    Veiculo novoVeiculo = new Veiculo();
                    novoVeiculo.setPlaca(placa);
                    return veiculoService.salvarVeiculo(novoVeiculo);
                });

        RegistroEstacionamento registro = new RegistroEstacionamento();
        registro.setVeiculo(veiculo);
        registro.setHoraEntrada(LocalDateTime.now());
        return registroEstacionamentoRepository.save(registro);
    }

    public RegistroEstacionamento encerrarEstacionamento(String placa, String formaPagamento) throws EstacionamentoException {
        List<RegistroEstacionamento> registros = registroEstacionamentoRepository.findByVeiculoPlacaAndHoraSaidaIsNull(placa);
        if (registros.isEmpty()) {
            throw new EstacionamentoException("Nenhum estacionamento ativo encontrado para a placa: " + placa);
        }

        RegistroEstacionamento registro = registros.get(0);

        registro.setHoraSaida(LocalDateTime.now());

        if (registro.getHoraSaida() != null) {
            throw new EstacionamentoException("O estacionamento para a placa " + placa + " já foi pago.");
        }

        double valor = calcularValor(registro.getHoraEntrada(), registro.getHoraSaida());
        registro.setValorPago(valor);

        registro.setFormaPagamento(formaPagamento);

        return registroEstacionamentoRepository.save(registro);
    }

    private double calcularValor(LocalDateTime horaEntrada, LocalDateTime horaSaida) {
        Duration duracao = Duration.between(horaEntrada, horaSaida);
        long minutos = duracao.toMinutes();

        double valorPorHora = 5.0;
        double valorMinuto = valorPorHora / 60;

        if (minutos < 15) {
            minutos = 15;
        }

        return minutos * valorMinuto;
    }

    public ValorDevidoDTO consultarValorDevido(String placa) {
        List<RegistroEstacionamento> registros = registroEstacionamentoRepository.findByVeiculoPlacaAndHoraSaidaIsNull(placa);
        if (registros.isEmpty()) {
            throw new EstacionamentoException("Nenhum estacionamento ativo encontrado para a placa: " + placa);
        }

        RegistroEstacionamento registro = registros.get(0);

        double valorDevido = calcularValor(registro.getHoraEntrada(), LocalDateTime.now());

        ValorDevidoDTO valorDevidoDTO = new ValorDevidoDTO();
        valorDevidoDTO.setPlaca(placa);
        valorDevidoDTO.setValorDevido(valorDevido);

        return valorDevidoDTO;
    }
}