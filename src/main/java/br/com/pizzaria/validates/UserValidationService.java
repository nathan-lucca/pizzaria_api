package br.com.pizzaria.validates;

import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.pizzaria.repository.UserRepository;
import br.com.pizzaria.entity.User;
import br.com.pizzaria.model.UserDTO;

@Service
public class UserValidationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserValidationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validateCadastro(UserDTO usuarioDTO) {
        if (Strings.isBlank(usuarioDTO.getNomeUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira seu Nome.");
        }

        if (Strings.isBlank(usuarioDTO.getCpfUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira seu CPF.");
        }

        if (Strings.isBlank(usuarioDTO.getSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira sua Senha.");
        }

        if (Strings.isBlank(usuarioDTO.getRepetirSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira sua Senha.");
        }

        if (userRepository.existsByCpfUsers(usuarioDTO.getCpfUsers())) {
            throw new ResponseStatusException(HttpStatus.FOUND,
                    "CPF já cadastrado. Por favor, insira um CPF diferente.");
        }

        if (!usuarioDTO.getSenhaUsers().equals(usuarioDTO.getRepetirSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "As senhas não coincidem.");
        }
    }

    public void validateLogin(UserDTO usuarioDTO) {
        if (Strings.isBlank(usuarioDTO.getCpfUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira seu CPF.");
        }

        if (Strings.isBlank(usuarioDTO.getSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira sua Senha.");
        }

        Optional<User> user = userRepository.findByCpfUsers(usuarioDTO.getCpfUsers());

        if (!user.isPresent() || !passwordEncoder.matches(usuarioDTO.getSenhaUsers(), user.get().getSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "CPF ou Senha inválidos.");
        }
    }

    public void validateTrocaSenha(UserDTO usuarioDTO) {
        if (Strings.isBlank(usuarioDTO.getCpfUsers()) || !userRepository.existsByCpfUsers(usuarioDTO.getCpfUsers())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        if (Strings.isBlank(usuarioDTO.getSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira sua nova Senha.");
        }

        if (Strings.isBlank(usuarioDTO.getRepetirSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, repita sua nova Senha.");
        }

        if (!usuarioDTO.getSenhaUsers().equals(usuarioDTO.getRepetirSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "As senhas não coincidem.");
        }
    }
}