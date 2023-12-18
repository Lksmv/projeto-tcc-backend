package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.*;
import com.roberta.alugueltrajes.enums.StatusAluguel;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.service.AluguelService;
import com.roberta.alugueltrajes.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/aluguel")
public class AluguelController {

    private final AluguelService aluguelService;
    private final RelatorioService relatorioService;

    @Operation(summary = "Criar Aluguel", description = "Cria um novo registro de aluguel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluguel criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<AluguelDTO> criarAluguel(@RequestBody @Valid AluguelCreateDTO aluguel) throws NaoEncontradoException, RegraNegocioException {
        return new ResponseEntity<>(aluguelService.create(aluguel), HttpStatus.OK);
    }

    @Operation(summary = "Listar Aluguéis", description = "Retorna uma lista paginada de aluguéis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de aluguéis recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public Page<AluguelDTO> list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "filtro", required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size);
        return aluguelService.findAll(pageable, filtro);
    }
    @Operation(summary = "Listar Aluguéis por codigo cliente", description = "Retorna uma lista de aluguéis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de aluguéis recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/por-cliente/{codigo}")
    public List<AluguelDTO> listByClienteCodigo(@PathVariable Integer codigo) throws NaoEncontradoException {
        return aluguelService.findAllByCliente(codigo);
    }
    @Operation(summary = "Listar Aluguéis por codigo produto", description = "Retorna uma lista de aluguéis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de aluguéis recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/por-produto/{codigo}")
    public List<AluguelDTO> listByProduto(@PathVariable Integer codigo) throws NaoEncontradoException {
        return aluguelService.findAllByProduto(codigo);
    }
    @Operation(summary = "Listar Aluguéis por dataSaida entre (dataInicial e dataFinal) + categoria + status", description = "Retorna uma lista de aluguéis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de aluguéis recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/por-data")
    public List<AluguelDTO> listByDataSaidaBetween(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicial,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFinal,
            @RequestParam(required = false) Integer codigoCategoria,
            @RequestParam(required = false) Integer status
    ) throws NaoEncontradoException {
        return aluguelService.findAllBetweenWithOptionalCategoryAndStatus(dataInicial, dataFinal, codigoCategoria, status);
    }

    @Operation(summary = "Atualizar Aluguel", description = "Atualiza os detalhes de um aluguel existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluguel atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Aluguel não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/{codigo}")
    public ResponseEntity<AluguelDTO> atualizarAluguel(
            @PathVariable Integer codigo,
            @RequestBody @Valid AluguelUpdateDTO aluguelUpdateDTO) throws NaoEncontradoException {
        AluguelDTO aluguelDTO = aluguelService.update(codigo, aluguelUpdateDTO);
        return ResponseEntity.ok(aluguelDTO);
    }

    @GetMapping("/relatorio-por-cliente/{codigo}/download")
    public ResponseEntity<byte[]> downloadRelatorio(@PathVariable Integer codigo) throws IOException, NaoEncontradoException {
        byte[] relatorio = relatorioService.gerarRelatorioWord(aluguelService.findAllByProduto(codigo));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorioClienteCod"+codigo+".docx");
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(relatorio.length)
                .body(relatorio);
    }

    @GetMapping("/relatorio-por-data/download")
    public ResponseEntity<byte[]> downloadRelatorioPorData(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicial,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFinal,
            @RequestParam(required = false) Integer codigoCategoria,
            @RequestParam(required = false) Integer status
    ) throws IOException, NaoEncontradoException {
        byte[] relatorio = relatorioService.gerarRelatorioWord(aluguelService.findAllBetweenWithOptionalCategoryAndStatus(dataInicial, dataFinal, codigoCategoria, status));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorioPorData.docx");
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(relatorio.length)
                .body(relatorio);
    }

    @GetMapping("/relatorio-por-produto/{codigo}/download")
    public ResponseEntity<byte[]> downloadRelatorioPorProduto(@PathVariable Integer codigo) throws IOException, NaoEncontradoException {
        byte[] relatorio = relatorioService.gerarRelatorioWord(aluguelService.findAllByCliente(codigo));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorioProdutoCod"+codigo+".docx");
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(relatorio.length)
                .body(relatorio);
    }
    @Operation(summary = "Atualizar status Aluguel", description = "Atualiza o status dos produtos de um aluguel existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluguel atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Aluguel não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/update-status")
    public ResponseEntity<Void> atualizarAluguel(@RequestBody AluguelProdutoUpdateDTO aluguelProdutoUpdateDTO) throws NaoEncontradoException {
        aluguelService.updateStatusAluguel(aluguelProdutoUpdateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/contrato/{codigo}")
    public ResponseEntity<byte[]> gerarContratoAluguel(@PathVariable Integer codigo) throws IOException, NaoEncontradoException {


        byte[] contrato = aluguelService.gerarContrato(codigo);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contratoAluguelCod" + codigo + ".docx");
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(contrato.length)
                .body(contrato);
    }



    @Operation(summary = "Deletar Aluguel", description = "Exclui um aluguel existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aluguel excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Aluguel não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletarAluguel(@PathVariable Integer codigo) throws NaoEncontradoException {
        aluguelService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Aluguel por ID", description = "Recupera um aluguel por seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluguel recuperado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Aluguel não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<AluguelDTO> buscarAluguelPorId(@PathVariable Integer codigo) throws NaoEncontradoException {
        AluguelDTO aluguelDTO = aluguelService.getById(codigo);
        return ResponseEntity.ok(aluguelDTO);
    }

    @GetMapping("/ultimos-sete-dias")
    public Integer getQuantidadeAlugueisUltimos7Dias(){
        return aluguelService.getQuantidadeAlugueisUltimos7Dias();
    }
    @GetMapping("/ultimos-sete-dias-por-dia")
    public List<AluguelDiaDTO> getQuantidadeAlugueisUltimos7DiasPorDia(){
        return aluguelService.getRelatorioUltimosSeteDias();
    }
    @GetMapping("/proximos-sete-dias-por-dia")
    public List<AluguelDiaDTO> getQuantidadeAlugueisProximos7DiasPorDia(){
        return aluguelService.getRelatorioProximosSeteDias();
    }
    @GetMapping("/quantidade-atrasados")
    public Integer getQuantidadeAtrasados(){
        return aluguelService.getQuantidadeAtrasados();
    }

}
