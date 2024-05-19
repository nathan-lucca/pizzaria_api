package br.com.pizzaria.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCartItem;

    @ManyToOne
    @JoinColumn(name = "id_cart")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "id_pizza")
    private Pizza pizza;

    private String tamanhoPizza;
    private int quantPizza;
    private float valortotalItem;
}
