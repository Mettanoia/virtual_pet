package com.solafides.virtual_pet_reborn.application.exceptions;

public class PetCreationException extends RuntimeException {

    public PetCreationException() {
        super();
    }

    public PetCreationException(String message) {
        super(message);
    }

    public PetCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PetCreationException(Throwable cause) {
        super(cause);
    }

}
