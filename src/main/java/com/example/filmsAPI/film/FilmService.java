package com.example.filmsAPI.film;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.filmsAPI.common.ResourceNotFoundException;

@Service
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository){
        this.filmRepository = filmRepository;
    }

    public FilmInfo findById(int id) {
        return filmRepository.getFilmInfoById(id).orElseThrow(() -> new ResourceNotFoundException("Film", id));
    }

    public List<FilmInfo> findBy(FilmFilter filter) {
        return filmRepository.getFilmInfoBy(filter);
    }
}
