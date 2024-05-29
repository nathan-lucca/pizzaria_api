package br.com.pizzaria.model;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long idOrderItem;
    private Long pizzaId;
    private String tamanhoPizza;
    private Integer quantPizza;
    private Double valorTotalItem;
}
