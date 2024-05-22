package br.com.pizzaria.validates;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.pizzaria.entity.Pizza;
import br.com.pizzaria.repository.PizzaRepository;

import java.util.List;

@Service
public class PizzaValidationService {
    private PizzaRepository pizzaRepository;

    public PizzaValidationService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public Pizza validateGetImagemPizza(Long idPizza) {
        return pizzaRepository.findById(idPizza)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza não encontrada."));
    }

    public List<Pizza> validateGetAllPizzas() {
        List<Pizza> pizzas = pizzaRepository.findAll();
        if (pizzas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma pizza encontrada.");
        }

        return pizzas;
    }
}
