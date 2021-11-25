package com.fschmatz.evento_service_v3.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_inscricao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_evento")
    private Evento idEvento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario")
    //@Column(name = "id_usuario")
    private Usuario idUsuario;

    private String data;
    private Integer checkin;

    String getDataDiaInscricao(){
        Date dataAtual = new Date();
        String dataFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataAtual);
        return dataFormatada;
    }

}
