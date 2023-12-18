package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.*;
import com.roberta.alugueltrajes.entity.*;
import com.roberta.alugueltrajes.enums.StatusAluguel;
import com.roberta.alugueltrajes.enums.StatusAluguelProduto;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.repository.AluguelProdutoRepository;
import com.roberta.alugueltrajes.repository.AluguelRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AluguelService {


    private final AluguelRepository repository;
    private final ObjectMapper objectMapper;
    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService;
    private final ProdutoService produtoService;
    private final PagamentoService pagamentoService;
    private final AluguelProdutoRepository aluguelProdutoRepository;
    private final CreditoService creditoService;

    public AluguelDTO create(AluguelCreateDTO dto) throws NaoEncontradoException, RegraNegocioException {

        if(dto.getValorPago() > 0 && dto.getFormaPagamento() == 0 && dto.getPatrocinio().equals("N")){
            throw new RegraNegocioException("Informe uma forma de pagamento");
        }

        for(Integer cod : dto.getListaProdutos()){
            if(findAluguelConflitante(dto.getDataSaida(),dto.getDataDevolucao(),cod).size() > 0){
                throw new RegraNegocioException(("Produto: "+cod+" já tem um aluguel para essa data!"));
            }
        }

        if(dto.getDataSaida().after(dto.getDataDevolucao())){
            throw new RegraNegocioException("Data de Devolução anterior a data de Saída.");
        }

        AluguelEntity entity = objectMapper.convertValue(dto, AluguelEntity.class);
        Integer nextCodigo = repository.getNextCodigo();
        entity.setCodigo(nextCodigo==null?1:nextCodigo);

        FuncionarioEntity funcionarioEntity = funcionarioService.getByCodigo(dto.getCodigoFuncionario());
        entity.setFuncionarioEntity(funcionarioEntity);
        entity.setAtivo('T');

        entity.setDataEmissao(Date.from(LocalDate.now().atStartOfDay(ZoneId.of("America/Sao_Paulo")).toInstant()));
        entity.setStatusAluguel(StatusAluguel.ABERTO);

        ClienteEntity cliente = clienteService.getEntityByCodigo(dto.getCodigoCliente());
        entity.setClienteEntity(cliente);

        entity.setPatrocinio(dto.getUtilizarCredito().charAt(0));

        entity.setUtilizarCredito(dto.getUtilizarCredito().charAt(0));

        entity = repository.save(entity);

        List<Integer> lista = dto.getListaProdutos();
        for (int i = 0; i < lista.size(); i++) {
            ProdutoEntity produto = produtoService.getEntityByCodigo(lista.get(i));
            AluguelProdutoId aluguelProdutoId = new AluguelProdutoId(entity.getIdAluguel(), produto.getIdProduto());
            aluguelProdutoRepository.save(new AluguelProdutoEntity(aluguelProdutoId, produto, entity, StatusAluguelProduto.ALUGADO));
            char p = entity.getPatrocinio();
            if (entity.getPatrocinio() != 'S') addQnt(produto.getCodigo());
        }

        if(entity.getUtilizarCredito()=='S'){
            double valorCredito = 0;
            for( CreditoEntity credito : cliente.getCreditoEntities()){
                valorCredito +=credito.getValor();
            }
            if(valorCredito > 0){
                double pagamento = 0;
                if(valorCredito > entity.getValor()){
                    pagamento = entity.getValor();
                }else{
                    pagamento = valorCredito;
                }
                PagamentoCreateDTO pagamentoCreateDTO = new PagamentoCreateDTO();
                pagamentoCreateDTO.setCodigoAluguel(entity.getCodigo());
                pagamentoCreateDTO.setValor(pagamento);
                pagamentoCreateDTO.setIdFormaPagamento(999);
                pagamentoService.criarPagamento(pagamentoCreateDTO);
                CreditoCreateDTO creditoCreateDTO = new CreditoCreateDTO();
                creditoCreateDTO.setData(entity.getDataEmissao());
                creditoCreateDTO.setValor(-pagamento);
                creditoCreateDTO.setCodigoCliente(cliente.getCodigo());
                creditoCreateDTO.setObservacoes("Utilizado em aluguel");
                creditoService.create(creditoCreateDTO);

            }
        }

        if(dto.getValorPago() > 0 && dto.getFormaPagamento() != 0 && dto.getPatrocinio().equals("N")){
            PagamentoCreateDTO pagamentoCreateDTO = new PagamentoCreateDTO();
            pagamentoCreateDTO.setCodigoAluguel(entity.getCodigo());
            pagamentoCreateDTO.setValor(dto.getValorPago());
            pagamentoCreateDTO.setIdFormaPagamento(dto.getFormaPagamento());
            pagamentoService.criarPagamento(pagamentoCreateDTO);
        }

        return toDto(getEntityByCodigo(entity.getIdAluguel()));
    }

    private void addQnt(Integer codigoProduto) throws NaoEncontradoException {
        produtoService.adicionarQuantidadeAlugado(codigoProduto);
    }

    public AluguelDTO update(Integer codigo, AluguelUpdateDTO dto) throws NaoEncontradoException {
        AluguelEntity aluguelEntity = getEntityByCodigo(codigo);
        aluguelEntity.setDataSaida(dto.getDataSaida());
        aluguelEntity.setDataDevolucao(dto.getDataDevolucao());
        aluguelEntity.setDataEmissao(dto.getDataEmissao());
        aluguelEntity.setObservacoes(dto.getObservacoes());
        aluguelEntity.setValorAdicional(dto.getValorAdicional());
        aluguelEntity.setStatusAluguel(dto.getStatusAluguel());
        int contadorAlugados = 0;
        int contadorCancelado = 0;
        for(AluguelProdutoEntity aluguelProdutoEntity : aluguelEntity.getAluguelProdutoEntities()){
            if(aluguelProdutoEntity.getStatus()== StatusAluguelProduto.ALUGADO){
                contadorAlugados++;
            }
            if(aluguelProdutoEntity.getStatus()== StatusAluguelProduto.CANCELADO){
                contadorCancelado++;
            }
        }
        if(contadorAlugados == 0){
            aluguelEntity.setStatusAluguel(StatusAluguel.FECHADO);
        }
        if(contadorCancelado == aluguelEntity.getAluguelProdutoEntities().size()){
            aluguelEntity.setStatusAluguel(StatusAluguel.CANCELADO);
        }
        return toDto(repository.save(aluguelEntity));
    }
    public void updateStatusAluguel(AluguelProdutoUpdateDTO aluguelProdutoUpdateDTO) throws NaoEncontradoException {
        AluguelEntity entity = getEntityByCodigo(aluguelProdutoUpdateDTO.getCodigoAluguel());
        ProdutoEntity produto = produtoService.getEntityByCodigo(aluguelProdutoUpdateDTO.getCodigoProduto());
        AluguelProdutoId aluguelProdutoId = new AluguelProdutoId(entity.getIdAluguel(),produto.getIdProduto());
        AluguelProdutoEntity aluguelEntity = aluguelProdutoRepository.getReferenceById(aluguelProdutoId);
        aluguelEntity.setStatus(aluguelProdutoUpdateDTO.getStatus());
        aluguelProdutoRepository.save(aluguelEntity);
    }


    public void delete(Integer codigo) throws NaoEncontradoException {
        AluguelEntity entity = getEntityByCodigo(codigo);
        entity.setAtivo('F');
        repository.save(entity);
    }

    public AluguelEntity getEntityByCodigo(Integer codigo) throws NaoEncontradoException {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new NaoEncontradoException("Aluguel não encontrado com o Codigo: " + codigo));
    }


    public AluguelDTO getById(Integer codigo) throws NaoEncontradoException {
        return toDto(getEntityByCodigo(codigo));
    }

    public byte[] gerarContrato(Integer codigo) throws NaoEncontradoException, IOException {
        AluguelDTO aluguelDTO = getById(codigo);

        XWPFDocument document = new XWPFDocument();

        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setBold(true);
        titleRun.setFontSize(14);
        titleRun.setText("Contrato de Aluguel");

        XWPFParagraph infoParagraph = document.createParagraph();
        infoParagraph.setSpacingAfter(200);
        XWPFRun infoRun = infoParagraph.createRun();
        infoRun.setFontSize(12);
        infoRun.setText("Informações do Aluguel:");

        XWPFParagraph detailsParagraph = document.createParagraph();
        XWPFRun detailsRun = detailsParagraph.createRun();
        detailsRun.setFontSize(12);
        detailsRun.setText("Código do Aluguel: " + aluguelDTO.getCodigo());
        detailsRun.addCarriageReturn();
        detailsRun.setText("Cliente: " + aluguelDTO.getClienteDTO().getNome());
        detailsRun.addCarriageReturn();
        detailsRun.setText("Data de Saída: " + aluguelDTO.getDataSaida());
        detailsRun.addCarriageReturn();
        detailsRun.setText("Data de Devolução: " + aluguelDTO.getDataDevolucao());

        XWPFParagraph clausesParagraph = document.createParagraph();
        XWPFRun clausesRun = clausesParagraph.createRun();
        clausesRun.setFontSize(12);
        clausesRun.setText("Cláusulas do Contrato:");

        clausesRun.addCarriageReturn();
        clausesRun.setText("1. O locatário concorda em alugar os produtos listados no contrato pelo período estabelecido.");
        clausesRun.addCarriageReturn();
        clausesRun.setText("2. O locador se compromete a fornecer os produtos em boas condições de uso.");

        XWPFParagraph signatureLocadorParagraph = document.createParagraph();
        signatureLocadorParagraph.setAlignment(ParagraphAlignment.LEFT); // Alinhamento à esquerda
        XWPFRun signatureLocadorRun = signatureLocadorParagraph.createRun();
        signatureLocadorRun.setFontSize(12);
        signatureLocadorRun.setText("Assinatura do Locador: __________________________");

        XWPFParagraph signatureLocatarioParagraph = document.createParagraph();
        signatureLocatarioParagraph.setAlignment(ParagraphAlignment.LEFT); // Alinhamento à direita
        XWPFRun signatureLocatarioRun = signatureLocatarioParagraph.createRun();
        signatureLocatarioRun.setFontSize(12);
        signatureLocatarioRun.setText("Assinatura do Locatário: __________________________");

        XWPFParagraph warningParagraph = document.createParagraph();
        XWPFRun warningRun = warningParagraph.createRun();
        warningRun.setFontSize(12);
        warningRun.setText("Este contrato é falso e provisório apenas para fins de exemplo. Não tem valor legal.");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();

        return outputStream.toByteArray();
    }



    public Page<AluguelDTO> findAll(Pageable pageable, String parametro) {
        Page<AluguelEntity> page = repository.findAtivosByNomeOrCodigo('T',parametro.toLowerCase(),pageable);
        return page.map(this::toDto);
    }
    public List<AluguelDTO> findAllByCliente(Integer codigo) throws NaoEncontradoException {
        ClienteEntity clienteEntity = clienteService.getEntityByCodigo(codigo);
        return repository.findAllByClienteEntity(clienteEntity).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public List<AluguelDTO> findAllByProduto(Integer codigo) throws NaoEncontradoException {
        ProdutoEntity entity = produtoService.getEntityByCodigo(codigo);
        return aluguelProdutoRepository.findAluguelProdutoEntitiesByProdutoEntity(entity).stream()
                .map(aluguelProdutoEntity -> toDto(aluguelProdutoEntity.getAluguelEntity()))
                .collect(Collectors.toList());
    }
    public List<AluguelDTO> findAllBetweenWithOptionalCategoryAndStatus(Date dataInicial,Date dataFinal,Integer codigoCategoria, Integer statusAluguel) throws NaoEncontradoException {
        List<AluguelEntity> alugueis = repository.findAluguelsByDataSaidaBetweenCategoriaAndStatus(dataInicial,dataFinal,codigoCategoria, statusAluguel==null?null:StatusAluguel.porId(statusAluguel));
        if(alugueis.isEmpty()){
            return new ArrayList<>();
        }else {
            return alugueis.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }
    }
    public List<AluguelDTO> findAluguelConflitante(Date dataInicial,Date dataFinal,Integer codigo) throws NaoEncontradoException {
        return repository.findAluguelsConflitantes(dataInicial,dataFinal,codigo)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


        private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public AluguelDTO toDto(AluguelEntity entity) {
        AluguelDTO dto = objectMapper.convertValue(entity, AluguelDTO.class);
        dto.setClienteDTO(clienteService.toDto(entity.getClienteEntity()));
        dto.setFuncionarioDTO(funcionarioService.toDto(entity.getFuncionarioEntity()));
        dto.setListaPagamentos(entity.getPagamentoEntities().stream().map(pagamento -> pagamentoService.toDTO(pagamento)).collect(Collectors.toSet()));
        Set<AluguelProdutoDTO> lista = new HashSet<>();

        for( AluguelProdutoEntity aluguel : entity.getAluguelProdutoEntities()){
            AluguelProdutoDTO aluguelProdutoDTO = new AluguelProdutoDTO();
            aluguelProdutoDTO.setProdutoDTO(produtoService.toDto(aluguel.getProdutoEntity()));
            aluguelProdutoDTO.setStatus(aluguel.getStatus());
            lista.add(aluguelProdutoDTO);
        }

        dto.setListaProdutos(lista);
        return dto;
    }

    public Integer getQuantidadeAlugueisUltimos7Dias() {
        Date dataSeteDiasAtras = Date.from(LocalDate.now().minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dataAtual = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Integer quantidade = repository.countByDataEmissaoBetween(dataSeteDiasAtras, dataAtual);
        return quantidade;
    }

    public List<AluguelDiaDTO> getRelatorioUltimosSeteDias(){
        List<AluguelDiaDTO> relatorio = new ArrayList<>();
        for(int i=0; i < 7;i++){
            LocalDate localDate = LocalDate.now().minusDays(i);
            Date data = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Integer quantidade = repository.countByDataEmissao(data);
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            String diaDaSemana = obterNomeDiaDaSemana(dayOfWeek);
            relatorio.add(new AluguelDiaDTO(data, quantidade, diaDaSemana));
        }
        return relatorio;
    }
    public List<AluguelDiaDTO> getRelatorioProximosSeteDias(){
        List<AluguelDiaDTO> relatorio = new ArrayList<>();
        for(int i=0; i < 7;i++){
            LocalDate localDate = LocalDate.now().plusDays(i);
            Date data = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Integer quantidade = repository.countByDataSaidaAndStatusAluguel(data,StatusAluguel.ABERTO);
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            String diaDaSemana = obterNomeDiaDaSemana(dayOfWeek);
            relatorio.add(new AluguelDiaDTO(data, quantidade, diaDaSemana));
        }
        return relatorio;
    }
    public Integer getQuantidadeAtrasados(){
        Date dataAtual = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Integer quantidade = repository.countByDataDevolucaoBeforeAndStatusAluguel(dataAtual, StatusAluguel.ABERTO);
        return quantidade;
    }

    private String obterNomeDiaDaSemana(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "Segunda-feira";
            case TUESDAY:
                return "Terça-feira";
            case WEDNESDAY:
                return "Quarta-feira";
            case THURSDAY:
                return "Quinta-feira";
            case FRIDAY:
                return "Sexta-feira";
            case SATURDAY:
                return "Sábado";
            case SUNDAY:
                return "Domingo";
            default:
                return "";
        }
    }




}
