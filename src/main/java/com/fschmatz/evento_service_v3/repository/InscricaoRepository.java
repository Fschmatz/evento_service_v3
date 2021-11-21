package com.fschmatz.evento_service_v3.repository;
import com.fschmatz.evento_service_v3.entity.Inscricao;
import com.fschmatz.evento_service_v3.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Integer> {

    public Iterable<Inscricao> findInscricaoByIdUsuario(Integer idUsuario);
    public Iterable<Inscricao> findInscricaoByIdUsuario(Usuario usuario);


    //public Iterable<Inscricao> getAllByIdUsuario(Integer idUsuario);


  /*  @Query(value = "select i.id_evento,e.nome,e.data from Inscricao i,Evento e where i.id_evento = e.id_evento and i.id_usuario = :idUsuario", nativeQuery = true)
    public Iterable<Inscricao> getAllByIdUsuarioComNome(@Param(value="idUsuario")Integer idUsuario);
*/
    //Iterable<Inscricao> getAllByIdUsuario(Integer idUsuario);
}
