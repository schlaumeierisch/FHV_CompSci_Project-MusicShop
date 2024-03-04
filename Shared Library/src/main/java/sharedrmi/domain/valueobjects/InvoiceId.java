package sharedrmi.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class InvoiceId implements Serializable {

    private final long invoiceId;

    public InvoiceId() {
        this.invoiceId = Instant.now().toEpochMilli();
    }

    public InvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

}
