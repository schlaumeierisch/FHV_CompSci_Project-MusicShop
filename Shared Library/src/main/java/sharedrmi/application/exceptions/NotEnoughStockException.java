package sharedrmi.application.exceptions;

import java.io.Serializable;

public class NotEnoughStockException extends Exception implements Serializable {
    public NotEnoughStockException(String message) {
        super(message);
    }
}
