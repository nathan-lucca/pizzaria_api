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
}
