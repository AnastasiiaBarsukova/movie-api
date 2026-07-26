package com.example.filmsAPI.common;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, int id) {
        super(resource + " with id " + id + " was not found");
    }
    
    public ResourceNotFoundException(String resource, String name) {
        super(resource + " " + name + " was not found");
    }
}
