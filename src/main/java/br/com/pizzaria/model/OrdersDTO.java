package br.com.pizzaria.model;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class OrdersDTO {
    private Long ordersId;
    private Long userId;
    private Date dataPedido;
    private String statusPedido;
    private List<OrderItemDTO> orderItems;
}
