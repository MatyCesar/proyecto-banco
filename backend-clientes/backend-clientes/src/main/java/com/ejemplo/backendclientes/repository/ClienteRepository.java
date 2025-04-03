package com.ejemplo.backendclientes.repository;

import com.ejemplo.backendclientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
