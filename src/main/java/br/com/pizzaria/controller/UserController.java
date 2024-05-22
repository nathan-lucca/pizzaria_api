package br.com.pizzaria.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.pizzaria.entity.User;
import br.com.pizzaria.repository.UserRepository;
import br.com.pizzaria.services.UserService;
import br.com.pizzaria.validates.UserValidationService;
import br.com.pizzaria.model.UserDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidationService userValidationService;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody UserDTO usuarioDTO) {
        userValidationService.validateCadastro(usuarioDTO);

        try {
            User usuario = userService.saveUser(usuarioDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Você foi cadastrado com sucesso.");
            response.put("dados", usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao cadastrar usuário.", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> logarUsuario(@Valid @RequestBody UserDTO usuarioDTO) {
        userValidationService.validateLogin(usuarioDTO);

        Optional<User> usuarioOptional = userService.loginUser(usuarioDTO);
        if (!usuarioOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "CPF ou Senha inválidos. Por favor, insira um CPF e Senha válidos.");
        }

        try {
            User usuario = usuarioOptional.get();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Você realizou login com sucesso.");
            response.put("dados", usuario);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao logar usuário.", e);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarUsuarios() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao listar usuários.", e);
        }
    }

    @DeleteMapping("/excluir/{userId}")
    public ResponseEntity<?> excluirUsuario(@PathVariable("userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        userRepository.deleteById(userId);

        return ResponseEntity.status(HttpStatus.OK).body("Conta excluída com sucesso.");
    }

    @PutMapping("/trocar_senha")
    public ResponseEntity<?> trocarSenhaUsuario(@Valid @RequestBody UserDTO usuarioDTO) {
        userValidationService.validateTrocaSenha(usuarioDTO);

        try {
            User usuario = userService.trocarSenhaUser(usuarioDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sua senha foi alterada com sucesso.");
            response.put("dados", usuario);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao alterar senha.", e);
        }
    }
}
