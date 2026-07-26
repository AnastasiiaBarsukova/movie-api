package com.example.filmsAPI.film;

public record FilmInfo(
        Integer id,
        String title,
        Short length,
        Integer releaseYear,
        String categories,
        String description
) {}