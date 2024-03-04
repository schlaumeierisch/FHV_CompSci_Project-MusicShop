package at.fhv.musicshopfx.domain;

import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.domain.enums.MediumType;

import java.math.BigDecimal;

public class InvoiceLineItem {

    private final String albumTitle;
    private final MediumType mediumType;
    private final BigDecimal price;
    private final int quantity;
    private final String minus;
    private final int returnQuantity;
    private final String plus;
    private final int returnedQuantity;
    private final InvoiceLineItemDTO invoiceLineItemDTO;

    public InvoiceLineItem(String albumTitle, MediumType mediumType, BigDecimal price, int quantity, int returnQuantity, int returnedQuantity, InvoiceLineItemDTO invoiceLineItemDTO) {
        this.albumTitle = albumTitle;
        this.mediumType = mediumType;
        this.price = price;
        this.quantity = quantity;
        this.minus = "-";
        this.returnQuantity = returnQuantity;
        this.plus = "+";
        this.returnedQuantity = returnedQuantity;
        this.invoiceLineItemDTO = invoiceLineItemDTO;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public MediumType getMediumType() {
        return mediumType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMinus() {
        return minus;
    }

    public int getReturnQuantity() {
        return returnQuantity;
    }

    public String getPlus() {
        return plus;
    }

    public int getReturnedQuantity() {
        return returnedQuantity;
    }

    public InvoiceLineItemDTO getInvoiceLineItemDTO() {
        return invoiceLineItemDTO;
    }
}
