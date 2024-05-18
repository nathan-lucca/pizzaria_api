package br.com.pizzaria.controller;

import br.com.pizzaria.entity.Pizza;
import br.com.pizzaria.repository.PizzaRepository;
import br.com.pizzaria.util.ImageUtil;
import jakarta.validation.Valid;

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

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarPizza(@Valid @RequestBody Pizza pizza) {
        if (pizza.getNomePizza() == null || pizza.getNomePizza().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira o nome da pizza.");
        }

        if (pizza.getDescricaoPizza() == null || pizza.getDescricaoPizza().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira a descrição da pizza.");
        }

        if (pizza.getImagemPizza() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira a imagem da pizza.");
        }

        if (pizza.getValorPizza() == null || pizza.getValorPizza().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Por favor, insira o valor da pizza.");
        }

        try {
            pizzaRepository.save(pizza);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pizza cadastrada com sucesso.");
            response.put("dados", pizza);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao cadastrar pizza.", e);
        }
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
