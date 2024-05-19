package br.com.pizzaria.repository;

import br.com.pizzaria.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c JOIN FETCH c.pizza WHERE c.cart.idUsers = :userId")
    List<CartItem> findAllByCart_IdUsers(Long userId);
}
