package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entidades.Venda;

@Repository
public interface RepositorioVenda extends JpaRepository<Venda, Long> {
}
