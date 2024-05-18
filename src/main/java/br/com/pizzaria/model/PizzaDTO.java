package br.com.pizzaria.model;

import lombok.Data;

@Data
public class PizzaDTO {
    private String nomePizza;
    private byte[] imagemPizza;
    private String tamanhoPizza;
    private String siglaPizza;
    private String valorPizza;
    private String descricaoPizza;
}
