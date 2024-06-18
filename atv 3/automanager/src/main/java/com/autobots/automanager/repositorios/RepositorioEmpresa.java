package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entidades.Empresa;

@Repository
public interface RepositorioEmpresa extends JpaRepository<Empresa, Long> {
}
