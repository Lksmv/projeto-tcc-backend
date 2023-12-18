package com.roberta.alugueltrajes.service;

import com.roberta.alugueltrajes.dtos.AluguelDTO;
import org.springframework.stereotype.Service;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class RelatorioService {

    public byte[] gerarRelatorioWord(List<AluguelDTO> alugueis) throws IOException {
        XWPFDocument document = new XWPFDocument();

        XWPFParagraph title = document.createParagraph();
        XWPFRun run = title.createRun();
        run.setText("Relatório de Aluguéis");
        run.setBold(true);
        run.setFontSize(14);

        XWPFTable table = document.createTable(alugueis.size() + 1, 8);
        table.setWidth("100%");

        // Configurar cabeçalho da tabela
        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("Código Aluguel");
        headerRow.getCell(1).setText("Nome Cliente");
        headerRow.getCell(2).setText("Telefone");
        headerRow.getCell(3).setText("Valor");
        headerRow.getCell(4).setText("Aluguel Status");
        headerRow.getCell(5).setText("Data Devolução");
        headerRow.getCell(6).setText("Data Retirada");
        headerRow.getCell(7).setText("Data Contrato");

        // Preencher os dados da lista na tabela
        for (int i = 0; i < alugueis.size(); i++) {
            AluguelDTO aluguel = alugueis.get(i);
            XWPFTableRow dataRow = table.getRow(i + 1);
            dataRow.getCell(0).setText(aluguel.getCodigo().toString());
            dataRow.getCell(1).setText(aluguel.getClienteDTO().getNome());
            dataRow.getCell(2).setText(aluguel.getClienteDTO().getTelefone());
            dataRow.getCell(3).setText("R$ " + aluguel.getValor());
            dataRow.getCell(4).setText(aluguel.getStatusAluguel().toString());
            dataRow.getCell(5).setText(aluguel.getDataDevolucao().toString());
            dataRow.getCell(6).setText(aluguel.getDataSaida().toString());
            dataRow.getCell(7).setText(aluguel.getDataEmissao().toString());
        }

        // Salvar o documento em um array de bytes
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}
