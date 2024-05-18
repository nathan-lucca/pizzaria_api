package br.com.pizzaria.model;

import lombok.Data;

@Data
public class UserDTO {
    private String nomeUsers;
    private String cpfUsers;
    private String senhaUsers;
    private String repetirSenhaUsers;
}
