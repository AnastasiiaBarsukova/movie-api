package com.example.filmsAPI.film;

public record FilmFilter(
        String title,
        String category,
        Short minLength,
        Short maxLength,
        Integer releaseYear
) {
}
