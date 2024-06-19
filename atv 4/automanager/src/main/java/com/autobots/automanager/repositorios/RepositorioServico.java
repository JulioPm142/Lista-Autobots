package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entidades.Servico;

@Repository
public interface RepositorioServico extends JpaRepository<Servico, Long> {
}
