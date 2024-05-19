package br.com.pizzaria.controller;

import br.com.pizzaria.entity.Pizza;
import br.com.pizzaria.repository.PizzaRepository;
import br.com.pizzaria.util.ImageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pizza")
public class PizzaController {

    @Autowired
    private PizzaRepository pizzaRepository;

    @GetMapping("/imagem/{id}")
    public ResponseEntity<?> getImagemPizza(@PathVariable Long id) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza não encontrada."));

        return ResponseEntity.status(HttpStatus.OK).body(ImageUtil.convertBlobToBase64PNG(pizza.getImagemPizza()));
    }

    @GetMapping("/listar")
    public ResponseEntity<?> getAllPizzas() {
        List<Pizza> pizzas = pizzaRepository.findAll();

        List<Map<String, Object>> pizzasDTO = pizzas.stream().map(pizza -> {
            Map<String, Object> pizzaMap = new HashMap<>();
            pizzaMap.put("idPizza", pizza.getIdPizza());
            pizzaMap.put("nomePizza", pizza.getNomePizza());
            pizzaMap.put("imagemPizza", ImageUtil.convertBlobToBase64PNG(pizza.getImagemPizza()));
            pizzaMap.put("tamanhoPizza", pizza.getTamanhoPizza());
            pizzaMap.put("siglaPizza", pizza.getSiglaPizza());
            pizzaMap.put("valorPizza", pizza.getValorPizza());
            pizzaMap.put("descricaoPizza", pizza.getDescricaoPizza());

            return pizzaMap;
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(pizzasDTO);
    }
}
