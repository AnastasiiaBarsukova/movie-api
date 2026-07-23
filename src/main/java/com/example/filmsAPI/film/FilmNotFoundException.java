package com.example.filmsAPI.film;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(int id) {
        super("Film with id " + id + " was not found");
    }
    
    public FilmNotFoundException(String name) {
        super("Film " + name + " was not found");
    }
}
