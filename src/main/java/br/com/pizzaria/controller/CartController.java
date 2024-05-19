package br.com.pizzaria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.pizzaria.entity.Cart;
import br.com.pizzaria.entity.CartItem;
import br.com.pizzaria.entity.Pizza;
import br.com.pizzaria.repository.CartRepository;
import br.com.pizzaria.repository.PizzaRepository;
import br.com.pizzaria.repository.CartItemRepository;
import br.com.pizzaria.model.CartDTO;
import br.com.pizzaria.repository.UserRepository;
import br.com.pizzaria.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarCarrinho(@Valid @RequestBody CartDTO cartDTO) {
        // Verificar se o usuário existe
        Optional<User> usuario = userRepository.findById(cartDTO.getIdUsers());
        if (!usuario.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        // Procurar um carrinho existente para o usuário
        Cart cart = cartRepository.findByIdUsers(cartDTO.getIdUsers()).orElse(new Cart());
        if (cart.getIdUsers() == null) {
            cart.setIdUsers(cartDTO.getIdUsers());
        }

        // Adicionar o item ao carrinho
        CartItem cartItem = new CartItem();

        // Buscar a pizza pelo ID
        Optional<Pizza> pizzaOpt = pizzaRepository.findById(cartDTO.getIdPizza());

        if (!pizzaOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza não encontrada.");
        }

        Pizza pizza = pizzaOpt.get();

        cartItem.setPizza(pizza);
        cartItem.setTamanhoPizza(cartDTO.getTamanhoPizza());
        cartItem.setQuantPizza(cartDTO.getQuantPizza());
        cartItem.setValortotalItem(cartDTO.getValorTotalCart());
        cartItem.setCart(cart);

        try {
            // Adicionar o item ao carrinho existente
            List<CartItem> items = cart.getItems();

            if (items == null) {
                items = new ArrayList<>();
            }

            items.add(cartItem);
            cart.setItems(items);

            cartRepository.save(cart);

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
        // Verificar se o usuário existe
        Optional<User> usuario = userRepository.findById(userId);
        if (!usuario.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        try {
            List<CartItem> cartItems = cartItemRepository.findAllByCart_IdUsers(userId);

            return ResponseEntity.status(HttpStatus.OK).body(cartItems);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao listar carrinho do usuário.", e);
        }
    }
}
