package com.solafides.virtual_pet_reborn.application.exceptions;

public class PetDeletionException extends RuntimeException {

    public PetDeletionException() {
        super();
    }

    public PetDeletionException(String message) {
        super(message);
    }

    public PetDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PetDeletionException(Throwable cause) {
        super(cause);
    }

}
