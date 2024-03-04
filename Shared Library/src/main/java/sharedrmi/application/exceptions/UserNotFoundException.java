package sharedrmi.application.exceptions;

import java.io.Serializable;

public class UserNotFoundException extends Exception implements Serializable {
    public UserNotFoundException(String message) {
        super(message);
    }
}
