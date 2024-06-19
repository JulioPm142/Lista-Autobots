package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entidades.Mercadoria;

@Repository
public interface RepositorioMercadoria extends JpaRepository<Mercadoria, Long> {
}
