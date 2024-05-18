package br.com.pizzaria.repository;

import br.com.pizzaria.entity.Pizza;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
}
