package br.com.pizzaria.validates;

import br.com.pizzaria.model.CartDTO;
import br.com.pizzaria.repository.UserRepository;
import br.com.pizzaria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CartValidationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    public void validateCart(CartDTO cartDTO) {
        if (cartDTO.getIdUsers() == null || !userRepository.existsById(cartDTO.getIdUsers())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        if (cartDTO.getIdPizza() == null || !pizzaRepository.existsById(cartDTO.getIdPizza())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza não encontrada.");
        }

        if (cartDTO.getTamanhoPizza() == null || cartDTO.getTamanhoPizza().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tamanho da pizza não informado.");
        }

        if (cartDTO.getQuantPizza() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade de pizza inválida.");
        }

        if (cartDTO.getValorTotalCart() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor total do carrinho inválido.");
        }
    }
}
