package com.fschmatz.evento_service_v3.repository;

import com.fschmatz.evento_service_v3.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Usuario getByCpf(String cpf);
}
