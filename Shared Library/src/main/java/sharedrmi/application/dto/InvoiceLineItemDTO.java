package sharedrmi.application.dto;

import lombok.Builder;
import lombok.Getter;
import sharedrmi.domain.enums.MediumType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InvoiceLineItemDTO implements Serializable {

    private MediumType mediumType;
    private String name;
    private int quantity;
    private BigDecimal price;
    private int returnedQuantity;

    public InvoiceLineItemDTO() {
    }

    @Builder
    public InvoiceLineItemDTO(MediumType mediumType, String name, int quantity, BigDecimal price, int returnedQuantity) {
        this.mediumType = mediumType;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.returnedQuantity = returnedQuantity;
    }

    public static List<InvoiceLineItemDTO> createFromCartLineItemDTOs(List<CartLineItemDTO> cartLineItemDTOs){
        return cartLineItemDTOs
                .stream()
                .map(cartLineItemDTO ->
                        InvoiceLineItemDTO.builder()
                                .mediumType(cartLineItemDTO.getMediumType())
                                .name(cartLineItemDTO.getName())
                                .quantity(cartLineItemDTO.getQuantity())
                                .price(cartLineItemDTO.getPrice())
                                .returnedQuantity(0).build())
                .collect(Collectors.toList());
    }
}
