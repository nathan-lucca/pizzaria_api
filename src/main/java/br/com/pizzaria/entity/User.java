package br.com.pizzaria.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsers;

    @Column(nullable = false)
    private String nomeUsers;

    @Column(nullable = false)
    private String cpfUsers;

    @Column(nullable = false)
    private String senhaUsers;

    @Transient
    private String repetirSenhaUsers;
}
