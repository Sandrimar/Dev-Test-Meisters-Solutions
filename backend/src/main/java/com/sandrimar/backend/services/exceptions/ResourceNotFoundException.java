package com.sandrimar.backend.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Object id) {
        super("Not found, Id: " + id);
    }
}
