package com.roberta.alugueltrajes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

@Data
public class ClienteCreateDTO {

    @Schema(description = "Codigo", example = "10")
    private Integer codigo;

    @Schema(description = "Nome", example = "João Silva")
    @NotBlank
    private String nome;

    @Schema(description = "CPF", example = "123.456.789-00")
    @CPF
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "Formato de CPF inválido")
    private String cpf;

    @Schema(description = "Data de Nascimento", example = "2000-01-01")
    @Past
    @NotNull
    private Date dataNascimento;

    @Schema(description = "Telefone", example = "(11)12345-6789")
    @Size(min= 14,max = 14, message = "O telefone está invalido")
    private String telefone;

    @Schema(description = "Rede Social", example = "@joao.silva")
    private String redeSocial;

    @Schema(description = "Pessoas Autorizadas", example = "Maria, Pedro")
    private String pessoasAutorizadas;

    @Schema(description = "Observações", example = "Cliente VIP")
    private String observacoes;

    @Schema(description = "CEP", example = "12345-678")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Formato de CEP inválido")
    private String cep;

    @Schema(description = "UF", example = "SP")
    @Pattern(regexp = "[A-Z]{2}", message = "UF inválida")
    private String uf;

    @Schema(description = "Endereço", example = "Rua Principal, 123")
    private String endereco;

    @Schema(description = "Bairro", example = "Centro")
    private String bairro;

    @Schema(description = "Cidade", example = "Blumenau")
    private String cidade;


}
