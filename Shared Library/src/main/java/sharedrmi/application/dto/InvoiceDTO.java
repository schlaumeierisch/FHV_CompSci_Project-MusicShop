package sharedrmi.application.dto;

import lombok.Builder;
import lombok.Getter;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.CustomerData;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
public class InvoiceDTO implements Serializable {

    private final InvoiceId invoiceId;
    private final List<InvoiceLineItemDTO> invoiceLineItems;
    private final PaymentMethod paymentMethod;
    private final LocalDate date;
    private final CustomerData customerData;

    @Builder
    public InvoiceDTO(InvoiceId invoiceId, List<InvoiceLineItemDTO> invoiceLineItems, PaymentMethod paymentMethod, LocalDate date, CustomerData customerData) {
        this.invoiceId = invoiceId;
        this.invoiceLineItems = invoiceLineItems;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.customerData = customerData;
    }
}
