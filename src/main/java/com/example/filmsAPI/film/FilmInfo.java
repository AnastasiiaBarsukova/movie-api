package com.example.filmsAPI.film;

import java.util.List;

import com.example.filmsAPI.actor.ActorInfo;
import com.example.filmsAPI.category.CategoryInfo;

public record FilmInfo(
        Integer id,
        String title,
        Short length,
        Integer releaseYear,
        List<CategoryInfo> categories,
        List<ActorInfo> actors,
        String description
) {}