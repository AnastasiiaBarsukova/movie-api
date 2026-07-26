package com.example.filmsAPI.category;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import static org.jooq.codegen.maven.example.tables.Category.CATEGORY;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {
    private final DSLContext dsl;

    public CategoryRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<CategoryInfo> getCategoryBy(int id){
        return dsl.select(CATEGORY.CATEGORY_ID, CATEGORY.NAME)
                  .from(CATEGORY)
                  .where(CATEGORY.CATEGORY_ID.eq(id))
                  .fetchOptional(record -> new CategoryInfo(record.get(CATEGORY.CATEGORY_ID), record.get(CATEGORY.NAME)));
    }

    public List<CategoryInfo> getAllCategories(){
        return dsl.select(CATEGORY.CATEGORY_ID, CATEGORY.NAME)
                  .from(CATEGORY)
                  .fetch(record -> new CategoryInfo(record.get(CATEGORY.CATEGORY_ID), record.get(CATEGORY.NAME)));
    }

    public CategoryInfo addCategory(CategoryInfo category){
        return dsl.insertInto(CATEGORY, CATEGORY.NAME).values(category.name())
                  .returningResult(
                    CATEGORY.CATEGORY_ID,
                    CATEGORY.NAME
                    ).fetchSingle(record -> new CategoryInfo(record.get(CATEGORY.CATEGORY_ID), record.get(CATEGORY.NAME)));
    }

    public void deleteCategoryBy(int id){
        dsl.delete(CATEGORY).where(CATEGORY.CATEGORY_ID.eq(id)).execute();
    }
}
