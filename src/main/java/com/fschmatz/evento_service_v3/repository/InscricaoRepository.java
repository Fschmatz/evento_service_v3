package com.fschmatz.evento_service_v3.repository;
import com.fschmatz.evento_service_v3.entity.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Integer> {

    public Iterable<Inscricao> getAllByIdUsuario(Integer idUsuario);

}
