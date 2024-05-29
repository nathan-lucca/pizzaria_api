package br.com.pizzaria.entity;

import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrders;

    private Long idUsers;

    private LocalDate dataPedido;
    private String statusPedido;

    @JsonManagedReference
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
