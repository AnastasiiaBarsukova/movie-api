package com.example.filmsAPI.film;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository){
        this.filmRepository = filmRepository;
    }

    public FilmInfo findById(int id) {
        return filmRepository.getFilmInfoById(id).orElseThrow(() -> new FilmNotFoundException(id));
    }

    public List<FilmInfo> findBy(FilmFilter filter) {
        return filmRepository.getFilmInfoBy(filter);
    }

    public void saveFilm(FilmInfo film) {
        return filmRepository.postFilm(film);
    }
}
