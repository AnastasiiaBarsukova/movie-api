package com.example.filmsAPI.film;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.Records.mapping;
import static org.jooq.codegen.maven.example.tables.Actor.ACTOR;
import static org.jooq.codegen.maven.example.tables.Category.CATEGORY;
import static org.jooq.codegen.maven.example.tables.Film.FILM;
import static org.jooq.codegen.maven.example.tables.FilmActor.FILM_ACTOR;
import static org.jooq.codegen.maven.example.tables.FilmCategory.FILM_CATEGORY;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import org.springframework.stereotype.Repository;

import com.example.filmsAPI.actor.ActorInfo;
import com.example.filmsAPI.category.CategoryInfo;


@Repository
public class FilmRepository {
    private final DSLContext dsl;
    public FilmRepository (DSLContext dsl){
        this.dsl = dsl;
    }

    private Field<List<CategoryInfo>> joinFilmCategory(){
        return multiset(
                        select(
                                CATEGORY.CATEGORY_ID,
                                CATEGORY.NAME
                        )
                        .from(FILM_CATEGORY)
                        .join(CATEGORY)
                        .on(CATEGORY.CATEGORY_ID.eq(
                                FILM_CATEGORY.CATEGORY_ID
                        ))
                        .where(FILM_CATEGORY.FILM_ID.eq(
                                FILM.FILM_ID
                        ))
                        .orderBy(CATEGORY.NAME.asc())
                )
                .convertFrom(result ->
                        result.map(mapping(CategoryInfo::new))
                )
                .as("categories");
    }

    private Field<List<ActorInfo>> joinFilmActor(){
        return multiset( 
                        select(
                                ACTOR.ACTOR_ID,
                                ACTOR.FIRST_NAME,
                                ACTOR.LAST_NAME
                        )
                        .from(FILM_ACTOR)
                        .join(ACTOR)
                        .on(ACTOR.ACTOR_ID.eq(
                                FILM_ACTOR.ACTOR_ID
                        ))
                        .where(FILM_ACTOR.FILM_ID.eq(
                                FILM.FILM_ID
                        ))
                        .orderBy(
                                ACTOR.LAST_NAME.asc(),
                                ACTOR.FIRST_NAME.asc()
                        )
                )
                .convertFrom(result ->
                        result.map(mapping(ActorInfo::new))
                )
                .as("actors");
        }


    public Optional<FilmInfo> getFilmInfoById(int id) {
        Field<List<CategoryInfo>> categories = joinFilmCategory();
        Field<List<ActorInfo>> actors = joinFilmActor();

        return dsl
                .select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        FILM.LENGTH,
                        FILM.RELEASE_YEAR.cast(Integer.class),
                        categories,
                        actors,
                        FILM.DESCRIPTION
                )
                .from(FILM)
                .where(FILM.FILM_ID.eq(id))
                .fetchOptional(mapping(FilmInfo::new));
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
                condition = condition.andExists(
                        selectOne()
                                .from(FILM_CATEGORY)
                                .join(CATEGORY)
                                .on(CATEGORY.CATEGORY_ID.eq(
                                        FILM_CATEGORY.CATEGORY_ID
                                ))
                                .where(FILM_CATEGORY.FILM_ID.eq(
                                        FILM.FILM_ID
                                ))
                                .and(CATEGORY.NAME.equalIgnoreCase(filter.category()))
                );
        }

        Field<List<CategoryInfo>> categories = joinFilmCategory();
        Field<List<ActorInfo>> actors =joinFilmActor();
                        

        return dsl
                .select(
                    FILM.FILM_ID,
                    FILM.TITLE,
                    FILM.LENGTH,
                    FILM.RELEASE_YEAR.cast(Integer.class),     
                    categories,
                    actors,
                    FILM.DESCRIPTION
            )
            .from(FILM)
            .where(condition)
            .orderBy(FILM.TITLE.asc())
            .fetch(mapping(FilmInfo::new));
        }

        public Integer insertFilm(CreateFilmRequest film) {
                return dsl
                        .insertInto(FILM)
                        .set(FILM.TITLE, film.title().trim())
                        .set(FILM.LENGTH, film.length())
                        .set(FILM.RELEASE_YEAR, film.releaseYear())
                        .set(FILM.DESCRIPTION, film.description())
                        .returningResult(FILM.FILM_ID)
                        .fetchOptional(FILM.FILM_ID)
                        .orElseThrow(() -> new IllegalStateException());
        }

        public void insertFilmCategories(Integer filmId, Collection<Integer> categoryIds) {
                if (categoryIds == null || categoryIds.isEmpty()) {
                        return;
                }

                var records = categoryIds.stream()
                        .filter(Objects::nonNull)
                        .distinct()
                        .map(categoryId -> {
                        var record = dsl.newRecord(FILM_CATEGORY);

                        record.set(
                                FILM_CATEGORY.FILM_ID,
                                filmId
                        );

                        record.set(
                                FILM_CATEGORY.CATEGORY_ID,
                                categoryId
                        );

                        return record;
                        })
                        .toList();

                if (!records.isEmpty()) {
                        dsl.batchInsert(records).execute();
                }
        }

        public void insertFilmActors(Integer filmId, Collection<Integer> actorIds) {
                if (actorIds == null || actorIds.isEmpty()) {
                        return;
                }

                var records = actorIds.stream()
                        .filter(Objects::nonNull)
                        .distinct()
                        .map(actorId -> {
                                var record = dsl.newRecord(FILM_ACTOR);
                                record.set(
                                        FILM_ACTOR.FILM_ID,
                                        filmId
                                );
                                record.set(
                                        FILM_ACTOR.ACTOR_ID,
                                        actorId
                                );
                                return record;
                        })
                        .toList();

                if (!records.isEmpty()) {
                        dsl.batchInsert(records).execute();
                }
        }
}
