package com.parquimetro.service;

import com.parquimetro.model.Veiculo;
import com.parquimetro.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepository;

    public Veiculo salvarVeiculo(Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }

    public Optional<Veiculo> buscarVeiculoPorPlaca(String placa) {
        return veiculoRepository.findByPlaca(placa);
    }

}