package br.com.pizzaria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.pizzaria.entity.CartItem;
import br.com.pizzaria.model.CartDTO;
import br.com.pizzaria.services.CartService;
import br.com.pizzaria.validates.CartValidationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartValidationService cartValidationService;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarCarrinho(@Valid @RequestBody CartDTO cartDTO) {
        cartValidationService.validateCart(cartDTO);

        try {
            var cartItem = cartService.addCartItem(cartDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item adicionado ao carrinho com sucesso.");
            response.put("dados", cartItem);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao adicionar item ao carrinho.", e);
        }
    }

    @GetMapping("/listar/{userId}")
    public ResponseEntity<?> listarCarrinhoUsuario(@PathVariable("userId") Long userId) {
        try {
            List<CartItem> cartItems = cartService.listCartItemsByUserId(userId);

            return ResponseEntity.status(HttpStatus.OK).body(cartItems);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao listar carrinho do usuário.", e);
        }
    }

    @DeleteMapping("/remover/{userId}/{pizzaId}/{tamanho}")
    public ResponseEntity<?> removerTamanhoPizza(
            @PathVariable("userId") Long userId,
            @PathVariable("pizzaId") Long pizzaId,
            @PathVariable("tamanho") String tamanho) {
        try {
            cartService.removeCartItemSize(userId, pizzaId, tamanho);

            return ResponseEntity.status(HttpStatus.OK).body("Item removido com sucesso.");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao remover item do carrinho.", e);
        }
    }

}
