package br.com.pizzaria.validates;

import br.com.pizzaria.model.OrdersDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.apache.logging.log4j.util.Strings;

@Service
public class OrdersValidationService {
    public void validarPedido(OrdersDTO orderDTO) {
        if (orderDTO.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado.");
        }

        if (orderDTO.getDataPedido() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data do Pedido não encontrada.");
        }

        if (Strings.isBlank(orderDTO.getStatusPedido())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status do Pedido não encontrado.");
        }
    }
}
