package br.com.pizzaria.services;

import br.com.pizzaria.entity.OrderItem;
import br.com.pizzaria.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderItem salvarItemPedido(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
