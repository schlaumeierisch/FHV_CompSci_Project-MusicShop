package sharedrmi.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class ShoppingCartDTO implements Serializable {

    private final String ownerId;
    private final List<CartLineItemDTO> cartLineItems;

    @Builder
    public ShoppingCartDTO(String ownerId, List<CartLineItemDTO> cartLineItems) {
        this.ownerId = ownerId;
        this.cartLineItems = cartLineItems;
    }

}
