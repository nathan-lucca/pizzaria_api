package br.com.pizzaria.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.pizzaria.entity.User;
import br.com.pizzaria.repository.UserRepository;
import br.com.pizzaria.model.UserDTO;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(UserDTO usuarioDTO) {
        // Convertendo o DTO para uma entidade
        User usuario = new User();
        usuario.setNomeUsers(usuarioDTO.getNomeUsers());
        usuario.setCpfUsers(usuarioDTO.getCpfUsers());
        usuario.setSenhaUsers(passwordEncoder.encode(usuarioDTO.getSenhaUsers()));

        return userRepository.save(usuario);
    }

    public Optional<User> loginUser(UserDTO usuarioDTO) {
        Optional<User> usuarioOptional = userRepository.findByCpfUsers(usuarioDTO.getCpfUsers());
        if (usuarioOptional.isPresent()
                && passwordEncoder.matches(usuarioDTO.getSenhaUsers(), usuarioOptional.get().getSenhaUsers())) {
            return usuarioOptional;
        } else {
            return Optional.empty();
        }
    }

    public User trocarSenhaUser(UserDTO usuarioDTO) {
        // Convertendo o DTO para uma entidade
        User usuario = userRepository.findByCpfUsers(usuarioDTO.getCpfUsers()).get();
        usuario.setSenhaUsers(passwordEncoder.encode(usuarioDTO.getSenhaUsers()));

        return userRepository.save(usuario);
    }
}
