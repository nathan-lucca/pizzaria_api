package br.com.pizzaria.controller;

import br.com.pizzaria.entity.Pizza;
import br.com.pizzaria.util.ImageUtil;
import br.com.pizzaria.validates.PizzaValidationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pizza")
public class PizzaController {
    @Autowired
    private PizzaValidationService pizzaValidationService;

    @GetMapping("/imagem/{idPizza}")
    public ResponseEntity<?> getImagemPizza(@PathVariable Long idPizza) {
        Pizza pizza = pizzaValidationService.validateGetImagemPizza(idPizza);

        return ResponseEntity.status(HttpStatus.OK).body(ImageUtil.convertBlobToBase64PNG(pizza.getImagemPizza()));
    }

    @GetMapping("/listar")
    public ResponseEntity<?> getAllPizzas() {
        List<Pizza> pizzas = pizzaValidationService.validateGetAllPizzas();

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
