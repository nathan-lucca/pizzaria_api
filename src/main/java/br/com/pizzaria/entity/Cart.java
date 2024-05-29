package br.com.pizzaria.entity;

import lombok.Data;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCart;

    private Long idUsers;

    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }

    public void clearItems() {
        for (CartItem item : items) {
            item.setCart(null);
        }
        items.clear();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "idCart=" + idCart +
                ", idUsers=" + idUsers +
                ", itemsSize=" + items.size() +
                '}';
    }
}