package sharedrmi.application.exceptions;

import java.io.Serializable;

public class InvoiceNotFoundException extends Exception implements Serializable {

    public InvoiceNotFoundException(String message) {
        super(message);
    }

}