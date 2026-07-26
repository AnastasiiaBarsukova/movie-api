package com.example.filmsAPI.film;

import java.util.List;

public record CreateFilmRequest(
    String title,
    String description,
    Integer releaseYear,
    Short length,
    Short languageId,
    List<Short> categoryIds,
    List<Integer> actorIds
){
    
}
