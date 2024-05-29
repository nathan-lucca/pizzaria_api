package br.com.pizzaria.validates;

import br.com.pizzaria.entity.OrderItem;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderItemValidationService {
    public void validarItemPedido(OrderItem orderItem) {
        if (orderItem.getPizza() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pizza não encontrada.");
        }

        if (orderItem.getQuantPizza() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade de pizza deve ser maior que 0.");
        }
    }
}
