package br.com.pizzaria.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "pizza")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPizza;

    @Column(nullable = false)
    private String nomePizza;

    @Column(nullable = false)
    private byte[] imagemPizza;

    @Column(nullable = false)
    private String tamanhoPizza;

    @Column(nullable = false)
    private String siglaPizza;

    @Column(nullable = false)
    private String valorPizza;

    @Column(nullable = false)
    private String descricaoPizza;
}
