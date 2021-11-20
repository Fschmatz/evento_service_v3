package com.fschmatz.evento_service_v3.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_inscricao;
    private Integer id_evento;
    @Column(name = "id_usuario")
    private Integer idUsuario;
    private String data;

}
