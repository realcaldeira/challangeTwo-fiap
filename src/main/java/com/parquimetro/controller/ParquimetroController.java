package com.parquimetro.controller;

import com.parquimetro.EstacionamentoException;
import com.parquimetro.dto.ValorDevidoDTO;
import com.parquimetro.model.RegistroEstacionamento;
import com.parquimetro.service.RegistroEstacionamentoService;
import com.parquimetro.service.VeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parquimetro")
@Tag(name = "Parquimetro", description = "Operações relacionadas ao gerenciamento de parquímetros")
public class ParquimetroController {
    @Autowired
    private RegistroEstacionamentoService registroEstacionamentoService;

    @Autowired
    private VeiculoService veiculoService;

    @PostMapping("/iniciar/{placa}")
    @Operation(summary = "Inicia o estacionamento de um veículo", description = "Registra a entrada de um veículo no parquímetro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estacionamento iniciado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<RegistroEstacionamento> iniciarEstacionamento(
            @Parameter(description = "Placa do veículo", required = true, example = "ABC-1234")
            @PathVariable String placa
    ) {
        RegistroEstacionamento registro = registroEstacionamentoService.iniciarEstacionamento(placa);
        return ResponseEntity.ok(registro);
    }

    @PutMapping("/encerrar/{placa}")
    @Operation(summary = "Encerra o estacionamento de um veículo", description = "Registra a saída de um veículo e calcula o valor a ser pago.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estacionamento encerrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Estacionamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<RegistroEstacionamento> encerrarEstacionamento(
            @Parameter(description = "Placa do veículo", required = true, example = "ABC-1234")
            @PathVariable String placa,
            @Parameter(description = "Forma de pagamento", required = true, example = "PIX")
            @RequestParam String formaPagamento
    ) throws EstacionamentoException {

        if (!formaPagamentoValida(formaPagamento)) {
            throw new IllegalArgumentException("Forma de pagamento inválida: " + formaPagamento);
        }

        RegistroEstacionamento registro = registroEstacionamentoService.encerrarEstacionamento(placa, formaPagamento);
        return ResponseEntity.ok(registro);
    }
    private boolean formaPagamentoValida(String formaPagamento) {
        return formaPagamento != null && !formaPagamento.trim().isEmpty();
    }

    @GetMapping("/valor-devido/{placa}")
    @Operation(summary = "Consulta o valor devido do estacionamento", description = "Retorna o valor a ser pago por um veículo com base na placa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor devido retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estacionamento não encontrado para a placa informada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ValorDevidoDTO> consultarValorDevido(
            @Parameter(description = "Placa do veículo", required = true, example = "ABC-1234")
            @PathVariable String placa
    ) throws Exception {
        ValorDevidoDTO valorDevidoDTO = registroEstacionamentoService.consultarValorDevido(placa);
        return ResponseEntity.ok(valorDevidoDTO);
    }
}