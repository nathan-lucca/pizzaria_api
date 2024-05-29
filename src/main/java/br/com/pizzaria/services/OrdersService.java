package br.com.pizzaria.services;

import br.com.pizzaria.entity.Orders;
import br.com.pizzaria.model.OrdersDTO;
import br.com.pizzaria.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.util.List;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    public Orders salvarPedido(OrdersDTO orderDTO) {
        Orders order = new Orders();
        order.setIdUsers(orderDTO.getUserId());
        order.setDataPedido(orderDTO.getDataPedido().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        order.setStatusPedido(orderDTO.getStatusPedido());

        try {
            return ordersRepository.save(order);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao salvar pedido no banco de dados.", e);
        }
    }

    public Orders getOrdersById(Long orderId) {
        if (orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do pedido não pode ser nulo.");
        }

        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));
    }

    public List<Orders> getOrdersByIdUsers(Long userId) {
        List<Orders> orders = ordersRepository.findAllByIdUsers(userId);
        orders.forEach(order -> order.getOrderItems().size());

        return orders;
    }

    public Orders atualizarStatusPedido(Long orderId, String status) {
        if (orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do pedido não pode ser nulo.");
        }

        Orders order = getOrdersById(orderId);
        order.setStatusPedido(status);

        return ordersRepository.save(order);
    }
}
