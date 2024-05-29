package br.com.pizzaria.controller;

import br.com.pizzaria.entity.OrderItem;
import br.com.pizzaria.services.OrderItemService;
import br.com.pizzaria.validates.OrderItemValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orderItems")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderItemValidationService orderItemValidationService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> criarItemPedido(@RequestBody OrderItem orderItem) {
        orderItemValidationService.validarItemPedido(orderItem);

        try {
            OrderItem savedOrderItem = orderItemService.salvarItemPedido(orderItem);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item de pedido cadastrado com sucesso.");
            response.put("data", savedOrderItem);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao cadastrar um item em algum pedido.", e);
        }
    }
}
