package com.solafides.virtual_pet_reborn.application.exceptions;

public class PetReadingException extends RuntimeException {

    public PetReadingException() {
        super();
    }

    public PetReadingException(String message) {
        super(message);
    }

    public PetReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PetReadingException(Throwable cause) {
        super(cause);
    }

}
