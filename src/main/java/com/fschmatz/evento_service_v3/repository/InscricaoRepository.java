package com.fschmatz.evento_service_v3.repository;
import com.fschmatz.evento_service_v3.entity.Evento;
import com.fschmatz.evento_service_v3.entity.Inscricao;
import com.fschmatz.evento_service_v3.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Integer> {

    public Iterable<Inscricao> findInscricaoByIdUsuario(Integer idUsuario);
    public Iterable<Inscricao> findInscricaoByIdUsuario(Usuario usuario);
    public Iterable<Inscricao> findInscricaoByIdUsuarioAndCheckinEquals(Usuario usuario,Integer checkin);

    public Integer countByIdUsuarioAndIdEvento(Usuario usuario, Evento evento);


    @Transactional
    @Modifying
    @Query("update Inscricao i set i.checkin = 1 where i.idEvento = :idEvento and i.idUsuario = :idUsuario")
    void updateCheckIn(@Param("idEvento") Evento evento, @Param("idUsuario") Usuario usuario);
}

//void updateCheckIn(@Param("idEvento") Integer idEvento, @Param("idUsuario") Integer idUsuario);