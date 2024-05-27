package br.com.pizzaria.repository;

import br.com.pizzaria.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Verificar se CPF já existe
    boolean existsByCpfUsers(String cpfUsers);

    // Verificar se a senha é do CPF informado
    Optional<User> findByCpfUsersAndSenhaUsers(String cpfUsers, String senhaUsers);

    // Procurar usuário pelo CPF
    Optional<User> findByCpfUsers(String cpfUsers);
}