package com.fschmatz.evento_service_v3.repository;


import com.fschmatz.evento_service_v3.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
    public Optional<Evento> findByNome(String nome);
}
