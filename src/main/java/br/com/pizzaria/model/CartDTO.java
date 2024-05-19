package br.com.pizzaria.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CartDTO {
    @NotNull
    private Long idUsers;

    @NotNull
    private Long idPizza;

    @NotNull
    private String tamanhoPizza;

    @NotNull
    private int quantPizza;

    @NotNull
    private float valorTotalCart;

}
