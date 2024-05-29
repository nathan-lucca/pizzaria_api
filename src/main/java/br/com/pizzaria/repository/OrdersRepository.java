package br.com.pizzaria.repository;

import br.com.pizzaria.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM `Orders` o WHERE o.idUsers = :userId")
    List<Orders> findAllByIdUsers(Long userId);

    Optional<Orders> findByIdUsersAndStatusPedido(Long userId, String statusPedido);
}
