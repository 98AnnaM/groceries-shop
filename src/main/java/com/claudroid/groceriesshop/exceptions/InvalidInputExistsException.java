package com.claudroid.groceriesshop.exceptions;

public class InvalidInputExistsException extends RuntimeException  {

    private String message;

    public InvalidInputExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
