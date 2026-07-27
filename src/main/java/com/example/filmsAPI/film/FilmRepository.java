package com.example.filmsAPI.film;

import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import static org.jooq.codegen.maven.example.tables.Actor.ACTOR;
import static org.jooq.codegen.maven.example.tables.Category.CATEGORY;
import static org.jooq.codegen.maven.example.tables.Film.FILM;
import static org.jooq.codegen.maven.example.tables.FilmActor.FILM_ACTOR;
import static org.jooq.codegen.maven.example.tables.FilmCategory.FILM_CATEGORY;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.example.filmsAPI.actor.ActorInfo;
import com.example.filmsAPI.category.CategoryInfo;


@Repository
public class FilmRepository {
    private final DSLContext dsl;
    public FilmRepository (DSLContext dsl){
        this.dsl = dsl;
    }

    private List<CategoryInfo> joinFilmCategory(Condition condition){
        return dsl
                .select(
                        CATEGORY.CATEGORY_ID,
                        CATEGORY.NAME
                )
                .from(FILM_CATEGORY)
                .join(CATEGORY)
                .on(CATEGORY.CATEGORY_ID.eq(
                        FILM_CATEGORY.CATEGORY_ID
                ))
                .where(condition)
                .fetch(record -> new CategoryInfo(
                        record.get(CATEGORY.CATEGORY_ID),
                        record.get(CATEGORY.NAME)
                ));
    }

    private List<ActorInfo> joinFilmActor(Condition condition){
        return dsl
                .select(
                        ACTOR.ACTOR_ID,
                        ACTOR.FIRST_NAME,
                        ACTOR.LAST_NAME
                )
                .from(FILM_ACTOR)
                .join(ACTOR)
                .on(ACTOR.ACTOR_ID.eq(
                        FILM_ACTOR.ACTOR_ID
                ))
                .where(condition)
                .fetch(record -> new ActorInfo(
                        record.get(ACTOR.ACTOR_ID),
                        record.get(ACTOR.FIRST_NAME),
                        record.get(ACTOR.LAST_NAME)
                ));
    }


    public Optional<FilmInfo> getFilmInfoById(int id) {
        var filmOptional = dsl
                .select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        FILM.LENGTH,
                        FILM.RELEASE_YEAR,
                        FILM.DESCRIPTION
                )
                .from(FILM)
                .where(FILM.FILM_ID.eq(id))
                .fetchOptional();

        if (filmOptional.isEmpty()) {
            return Optional.empty();
        }

        List<CategoryInfo> categories = joinFilmCategory(FILM_CATEGORY.FILM_ID.eq(id));

        List<ActorInfo> actors = joinFilmActor(FILM_ACTOR.FILM_ID.eq(id));

        var film = filmOptional.get();

        return Optional.of(new FilmInfo(
                film.get(FILM.FILM_ID),
                film.get(FILM.TITLE),
                film.get(FILM.LENGTH),
                film.get(FILM.RELEASE_YEAR),
                categories,
                actors,
                film.get(FILM.DESCRIPTION)
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

        List<CategoryInfo> categories = joinFilmCategory(condition);
        List<ActorInfo> actors = joinFilmActor(condition);

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
                .where(condition)
                .orderBy(FILM.TITLE.asc())
                .fetch(record -> new FilmInfo(
                        record.get(FILM.FILM_ID),
                        record.get(FILM.TITLE),
                        record.get(FILM.LENGTH),
                        record.get(FILM.RELEASE_YEAR),
                        categories, 
                        actors,
                        record.get(FILM.DESCRIPTION)
                ));
    }
}
