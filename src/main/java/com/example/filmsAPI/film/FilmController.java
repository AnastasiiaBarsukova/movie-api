package com.example.filmsAPI.film;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public FilmInfo findById(@PathVariable int id) {
        return filmService.findById(id);
    }

    @GetMapping
    public List<FilmInfo> findBy(@RequestParam(required=false) String title,
        @RequestParam(required=false) String category,
        @RequestParam(required=false) Short minLength,
        @RequestParam(required=false) Short maxLength,
        @RequestParam(required=false) Integer releaseYear) {

        FilmFilter filter = new FilmFilter(title, category, minLength, maxLength, releaseYear);
        return filmService.findBy(filter);
    }
}
