package br.com.pizzaria.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.pizzaria.entity.Cart;
import br.com.pizzaria.entity.CartItem;
import br.com.pizzaria.entity.Pizza;
import br.com.pizzaria.model.CartDTO;
import br.com.pizzaria.repository.CartItemRepository;
import br.com.pizzaria.repository.CartRepository;
import br.com.pizzaria.repository.PizzaRepository;
import br.com.pizzaria.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.lang.Float;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    public CartItem addCartItem(CartDTO cartDTO) {
        if (!userRepository.existsById(cartDTO.getIdUsers())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        Optional<Pizza> pizzaOpt = pizzaRepository.findById(cartDTO.getIdPizza());
        if (!pizzaOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza não encontrada.");
        }

        Pizza pizza = pizzaOpt.get();
        Cart cart = cartRepository.findByIdUsers(cartDTO.getIdUsers()).orElse(new Cart());
        if (cart.getIdUsers() == null) {
            cart.setIdUsers(cartDTO.getIdUsers());
        }

        CartItem cartItem = new CartItem();
        cartItem.setPizza(pizza);
        cartItem.setTamanhoPizza(cartDTO.getTamanhoPizza());
        cartItem.setQuantPizza(cartDTO.getQuantPizza());
        cartItem.setValortotalItem(cartDTO.getValorTotalCart());
        cartItem.setCart(cart);

        List<CartItem> items = cart.getItems();
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(cartItem);
        cart.setItems(items);

        cartRepository.save(cart);
        return cartItem;
    }

    public List<CartItem> listCartItemsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        return cartItemRepository.findAllByCart_IdUsers(userId);
    }

    public void removeCartItemSize(Long userId, Long pizzaId, String tamanho) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        Cart cart = cartRepository.findByIdUsers(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrinho não encontrado."));

        List<CartItem> items = cart.getItems();
        Optional<CartItem> cartItemOpt = items.stream()
                .filter(item -> item.getPizza().getIdPizza().equals(pizzaId) && item.getTamanhoPizza().equals(tamanho))
                .findFirst();

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();

            if (cartItem.getQuantPizza() > 1) {
                cartItem.setQuantPizza(cartItem.getQuantPizza() - 1);
                cartItem.setValortotalItem(cartItem.getQuantPizza() * Float.parseFloat(cartItem.getPizza().getValorPizza()));
            } else {
                items.remove(cartItem);
            }

            cartRepository.save(cart);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado no carrinho.");
        }
    }
}
