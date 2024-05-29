package br.com.pizzaria.controller;

import br.com.pizzaria.entity.Orders;
import br.com.pizzaria.model.OrdersDTO;
import br.com.pizzaria.repository.UserRepository;
import br.com.pizzaria.services.CartService;
import br.com.pizzaria.services.OrdersService;
import br.com.pizzaria.validates.OrdersValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersValidationService ordersValidationService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarPedido(@RequestBody OrdersDTO orderDTO) {
        ordersValidationService.validarPedido(orderDTO);

        try {
            Orders order = ordersService.salvarPedido(orderDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido criado com sucesso.");
            response.put("data", order);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao criar pedido.", e);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPedidosPorUsuario(@PathVariable("userId") Long userId) {
        try {
            List<Orders> orders = ordersService.getOrdersByIdUsers(userId);

            return ResponseEntity.status(HttpStatus.OK).body(orders);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao conseguir pedidos.", e);
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> atualizarStatusPedido(@PathVariable Long orderId, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status não pode ser nulo.");
        }

        try {
            Orders order = ordersService.atualizarStatusPedido(orderId, status);

            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao atualizar status do pedido.", e);
        }
    }

    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarPedido(@RequestBody OrdersDTO orderDTO) {
        try {
            if (orderDTO.getOrdersId() == null || orderDTO.getUserId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "ID do pedido ou ID do usuário não podem ser nulos.");
            }

            Orders order = ordersService.atualizarStatusPedido(orderDTO.getOrdersId(), "Pago");

            if (!userRepository.existsById(orderDTO.getUserId())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
            }

            cartService.limparCarrinho(orderDTO.getUserId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido finalizado com sucesso.");
            response.put("data", order);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getReason());
            errorResponse.put("details", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erro ao finalizar pedido.");
            errorResponse.put("details", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
