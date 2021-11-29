package com.fschmatz.evento_service_v3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscricaoSync {

    @Id
    private Integer id_inscricao;
    private Integer id_evento;
    private Integer id_usuario;
    private String cpf_user;
    private Integer checkin;
    private String data;
}
