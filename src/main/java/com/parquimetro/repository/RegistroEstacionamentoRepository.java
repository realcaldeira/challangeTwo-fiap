package com.parquimetro.repository;

import com.parquimetro.model.RegistroEstacionamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroEstacionamentoRepository extends JpaRepository<RegistroEstacionamento, Long> {
    List<RegistroEstacionamento> findByVeiculoPlacaAndHoraSaidaIsNull(String placa);
}