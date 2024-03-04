package sharedrmi.application.exceptions;

import java.io.Serializable;

public class AlbumNotFoundException extends Exception implements Serializable {

    public AlbumNotFoundException(String message) {
        super(message);
    }
}