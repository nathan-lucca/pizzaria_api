package br.com.pizzaria.repository;

import br.com.pizzaria.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByIdUsers(Long userId);
}
