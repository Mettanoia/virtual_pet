package com.solafides.virtual_pet_reborn.application.exceptions;

public class UserManagmentException extends RuntimeException {

    public UserManagmentException() {
        super();
    }

    public UserManagmentException(String message) {
        super(message);
    }

    public UserManagmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserManagmentException(Throwable cause) {
        super(cause);
    }

}
