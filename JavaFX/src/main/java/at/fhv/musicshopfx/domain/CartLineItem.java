package at.fhv.musicshopfx.domain;

import sharedrmi.application.dto.CartLineItemDTO;
import sharedrmi.domain.enums.MediumType;

import java.math.BigDecimal;

public class CartLineItem {

    private final String name;
    private final MediumType medium;
    private final int stock;
    private final int quantity;
    private final BigDecimal price;
    private final String minus;
    private final String plus;
    private final String x;
    private CartLineItemDTO cartLineItemDTO;

    public CartLineItem(String name, MediumType medium, int stock, int quantity, BigDecimal price, String minus, String plus, String x, CartLineItemDTO cartLineItemDTO) {
        this.name = name;
        this.medium = medium;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.minus = minus;
        this.plus = plus;
        this.x = x;
        this.cartLineItemDTO = cartLineItemDTO;
    }

    public String getName() {
        return name;
    }

    public MediumType getMedium() {
        return medium;
    }

    public int getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getMinus() {return minus; }

    public String getPlus() {
        return plus;
    }

    public String getX() {
        return x;
    }

    public CartLineItemDTO getLineItemDTO() {
        return cartLineItemDTO;
    }
}
