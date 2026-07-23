package com.example.filmsAPI.film;

import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import static org.jooq.codegen.maven.example.tables.Category.CATEGORY;
import static org.jooq.codegen.maven.example.tables.Film.FILM;
import static org.jooq.codegen.maven.example.tables.FilmCategory.FILM_CATEGORY;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;


@Repository
public class FilmRepository {
    private final DSLContext dsl;
    public FilmRepository (DSLContext dsl){
        this.dsl = dsl;
    }

    public Optional<FilmInfo> getFilmInfoById(int id){
        return dsl.select(FILM.FILM_ID, FILM.TITLE, FILM.LENGTH, FILM.RELEASE_YEAR, CATEGORY.NAME, FILM.DESCRIPTION)
                  .from(FILM)
                  .join(FILM_CATEGORY).on(FILM_CATEGORY.FILM_ID.eq(FILM.FILM_ID))
                  .join(CATEGORY).on(CATEGORY.CATEGORY_ID.eq(FILM_CATEGORY.CATEGORY_ID))
                  .where(FILM.FILM_ID.eq(id)).fetchOptional(record -> new FilmInfo(
                        record.get(FILM.FILM_ID),
                        record.get(FILM.TITLE),
                        record.get(FILM.LENGTH),
                        record.get(FILM.RELEASE_YEAR),
                        record.get(CATEGORY.NAME),
                        record.get(FILM.DESCRIPTION)
                ));
    }

    public List<FilmInfo> getFilmInfoBy(FilmFilter filter){
        Condition condition = DSL.noCondition();

        if (filter.title() != null && !filter.title().isBlank()) {
            condition = condition.and(
                    FILM.TITLE.containsIgnoreCase(filter.title().trim())
            );
        }

        if (filter.minLength() != null) {
            condition = condition.and(
                    FILM.LENGTH.ge(filter.minLength())
            );
        }

        if (filter.maxLength() != null) {
            condition = condition.and(
                    FILM.LENGTH.le(filter.maxLength())
            );
        }

        if (filter.releaseYear() != null) {
            condition = condition.and(
                    FILM.RELEASE_YEAR.eq(filter.releaseYear())
            );
        }

        if (filter.category() != null && !filter.category().isBlank()) {
            condition = condition.and(
                    CATEGORY.NAME.equalIgnoreCase(filter.category())
            );
        }

        return dsl
                .select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        FILM.LENGTH,
                        FILM.RELEASE_YEAR,
                        CATEGORY.NAME,
                        FILM.DESCRIPTION
                )
                .from(FILM)
                .join(FILM_CATEGORY).on(FILM_CATEGORY.FILM_ID.eq(FILM.FILM_ID))
                .join(CATEGORY).on(CATEGORY.CATEGORY_ID.eq(FILM_CATEGORY.CATEGORY_ID))
                .where(condition)
                .orderBy(FILM.TITLE.asc())
                .fetch(record -> new FilmInfo(
                        record.get(FILM.FILM_ID),
                        record.get(FILM.TITLE),
                        record.get(FILM.LENGTH),
                        record.get(FILM.RELEASE_YEAR),
                        record.get(CATEGORY.NAME),
                        record.get(FILM.DESCRIPTION)
                ));
    }
}
