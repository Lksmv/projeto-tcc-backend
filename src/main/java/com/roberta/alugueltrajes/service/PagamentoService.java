package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.PagamentoCreateDTO;
import com.roberta.alugueltrajes.dtos.PagamentoDTO;
import com.roberta.alugueltrajes.entity.PagamentoEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.repository.AluguelRepository;
import com.roberta.alugueltrajes.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ObjectMapper objectMapper;
    private final AluguelRepository aluguelRepository;
    private final FormaDePagamentoService formaDePagamentoService;

    public PagamentoDTO criarPagamento(PagamentoCreateDTO pagamentoCreateDTO) throws NaoEncontradoException {
        PagamentoEntity pagamentoEntity = objectMapper.convertValue(pagamentoCreateDTO, PagamentoEntity.class);
        pagamentoEntity.setData(Date.from(LocalDate.now().atStartOfDay(ZoneId.of("America/Sao_Paulo")).toInstant()));
        pagamentoEntity.setFormaDePagamento(formaDePagamentoService.getById(pagamentoCreateDTO.getIdFormaPagamento()));
        pagamentoEntity.setAluguelEntity(aluguelRepository.findByCodigo(pagamentoCreateDTO.getCodigoAluguel()).get());
        return toDTO(pagamentoRepository.save(pagamentoEntity));
    }

    public PagamentoDTO atualizarPagamento(Integer idPagamento, PagamentoCreateDTO pagamentoCreateDTO) throws NaoEncontradoException {
        PagamentoEntity pagamentoEntity = getPagamentoById(idPagamento);
        pagamentoEntity.setValor(pagamentoCreateDTO.getValor());
        pagamentoEntity.setData(Date.from(LocalDate.now().atStartOfDay(ZoneId.of("America/Sao_Paulo")).toInstant()));
        pagamentoEntity.setFormaDePagamento(formaDePagamentoService.getById(pagamentoCreateDTO.getIdFormaPagamento()));
        return toDTO(pagamentoRepository.save(pagamentoEntity));
    }

        public void deletarPagamento(Integer idPagamento) throws NaoEncontradoException {
        PagamentoEntity pagamentoEntity = getPagamentoById(idPagamento);
        pagamentoRepository.delete(pagamentoEntity);
    }

    public PagamentoDTO getDetalhesPagamento(Integer idPagamento) throws NaoEncontradoException {
        PagamentoEntity pagamentoEntity = getPagamentoById(idPagamento);
        return toDTO(pagamentoEntity);
    }

    public PagamentoEntity getPagamentoById(Integer id) throws NaoEncontradoException {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Pagamento não encontrado com o ID: " + id));
    }

    public Page<PagamentoDTO> listarPagamentos(Pageable pageable) {
        Page<PagamentoEntity> pagamentos = pagamentoRepository.findAll(pageable);
        return pagamentos.map(this::toDTO);
    }

    public PagamentoDTO toDTO(PagamentoEntity pagamentoEntity) {
        PagamentoDTO pagamentoDTO = objectMapper.convertValue(pagamentoEntity, PagamentoDTO.class);
        pagamentoDTO.setFormaDePagamentoDTO(formaDePagamentoService.toDto(pagamentoEntity.getFormaDePagamento()));
        return pagamentoDTO;
    }
}
