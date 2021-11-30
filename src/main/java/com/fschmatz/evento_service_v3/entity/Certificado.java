package com.fschmatz.evento_service_v3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Certificado {

    @Id
    private Integer id_certificado;
    private String nomeUsuario;
    private String nomeEvento;
    private String dataEvento;
    private String cargaHoraria;
}


/*
- nome do evento
- nome do usuario
- carga horaria ( botar um valor qualquer )
- data do evento*/
