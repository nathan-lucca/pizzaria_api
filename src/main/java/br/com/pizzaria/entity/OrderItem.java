package br.com.pizzaria.entity;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Data
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrderItem;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_orders")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "id_pizza")
    private Pizza pizza;

    private String tamanhoPizza;
    private int quantPizza;
    private float valortotalItem;
}
