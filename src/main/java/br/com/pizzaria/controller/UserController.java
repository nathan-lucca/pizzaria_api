package br.com.pizzaria.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.pizzaria.entity.User;
import br.com.pizzaria.repository.UserRepository;
import br.com.pizzaria.model.UserDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody UserDTO usuarioDTO) {
        if (usuarioDTO.getNomeUsers() == null || usuarioDTO.getNomeUsers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira seu Nome.");
        }

        if (usuarioDTO.getCpfUsers() == null || usuarioDTO.getCpfUsers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira seu CPF.");
        }

        if (usuarioDTO.getSenhaUsers() == null || usuarioDTO.getSenhaUsers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira sua Senha.");
        }

        if (usuarioDTO.getRepetirSenhaUsers() == null || usuarioDTO.getRepetirSenhaUsers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, repita sua Senha.");
        }

        if (userRepository.existsByCpfUsers(usuarioDTO.getCpfUsers())) {
            throw new ResponseStatusException(HttpStatus.FOUND, "CPF já cadastrado. Por favor, insira um CPF diferente.");
        }

        if (!usuarioDTO.getSenhaUsers().equals(usuarioDTO.getRepetirSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "As senhas não coincidem.");
        }

        // Convertendo o DTO para uma entidade
        User usuario = new User();
        usuario.setNomeUsers(usuarioDTO.getNomeUsers());
        usuario.setCpfUsers(usuarioDTO.getCpfUsers());
        usuario.setSenhaUsers(usuarioDTO.getSenhaUsers());

        try {
            userRepository.save(usuario);

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
        if (usuarioDTO.getCpfUsers() == null || usuarioDTO.getCpfUsers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira seu CPF.");
        }

        if (usuarioDTO.getSenhaUsers() == null || usuarioDTO.getSenhaUsers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira sua Senha.");
        }

        Optional<User> usuarioOptional = userRepository.findByCpfUsersAndSenhaUsers(usuarioDTO.getCpfUsers(), usuarioDTO.getSenhaUsers());
        if (!usuarioOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "CPF ou Senha inválidos. Por favor, insira um CPF e Senha válidos.");
        }

        User usuario = usuarioOptional.get();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Você realizou login com sucesso.");
        response.put("dados", usuario);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarUsuarios() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao listar usuários.", e);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable("id") Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        userRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body("Conta excluída com sucesso.");
    }

    @PutMapping("/trocar_senha")
    public ResponseEntity<?> trocarSenhaUsuario(@Valid @RequestBody UserDTO usuarioDTO) {
        if (!userRepository.existsByCpfUsers(usuarioDTO.getCpfUsers())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        if (usuarioDTO.getSenhaUsers() == null || usuarioDTO.getSenhaUsers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira sua nova Senha.");
        }

        if (usuarioDTO.getRepetirSenhaUsers() == null || usuarioDTO.getRepetirSenhaUsers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, repita sua nova Senha.");
        }

        if (!usuarioDTO.getSenhaUsers().equals(usuarioDTO.getRepetirSenhaUsers())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "As senhas não coincidem.");
        }

        // Convertendo o DTO para uma entidade
        User usuario = userRepository.findByCpfUsers(usuarioDTO.getCpfUsers()).get();
        usuario.setSenhaUsers(usuarioDTO.getSenhaUsers());
        userRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.OK).body("Sua senha foi alterada com sucesso.");
    }
}
