package com.solafides.virtual_pet_reborn.application.exceptions;

public class UserReadingException extends RuntimeException {

    public UserReadingException() {
        super();
    }

    public UserReadingException(String message) {
        super(message);
    }

    public UserReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserReadingException(Throwable cause) {
        super(cause);
    }

}
