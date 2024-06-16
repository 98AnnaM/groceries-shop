package com.claudroid.groceriesshop.exceptions;

public class ProductNameExistsException extends RuntimeException  {

    private String message;

    public ProductNameExistsException(String message) {
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
