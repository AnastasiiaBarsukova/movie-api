package com.example.filmsAPI.actor;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import static org.jooq.codegen.maven.example.tables.Actor.ACTOR;
import org.springframework.stereotype.Repository;

@Repository
public class ActorRepository {
    private final DSLContext dsl;

    public ActorRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<ActorInfo> getActorBy(int id){
        return dsl.select(ACTOR.ACTOR_ID, ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                  .from(ACTOR)
                  .where(ACTOR.ACTOR_ID.eq(id))
                  .fetchOptional(record -> new ActorInfo(record.get(ACTOR.ACTOR_ID), record.get(ACTOR.FIRST_NAME), record.get(ACTOR.LAST_NAME)));
    
    }

    public List<ActorInfo> getAllActors(){
        return dsl.select(ACTOR.ACTOR_ID, ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                  .from(ACTOR)
                  .fetch(record -> new ActorInfo(record.get(ACTOR.ACTOR_ID), record.get(ACTOR.FIRST_NAME), record.get(ACTOR.LAST_NAME)));
    }

    public ActorInfo addActor(ActorInfo actor){
        return dsl.insertInto(ACTOR, ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                  .values(actor.firstName(), actor.lastName())
                  .returningResult(
                    ACTOR.ACTOR_ID,
                    ACTOR.FIRST_NAME, 
                    ACTOR.LAST_NAME
                    )
                  .fetchSingle(record -> new ActorInfo(record.get(ACTOR.ACTOR_ID), record.get(ACTOR.FIRST_NAME), record.get(ACTOR.LAST_NAME)));
    }

    public void deleteActorBy(int id){
        dsl.delete(ACTOR).where(ACTOR.ACTOR_ID.eq(id)).execute();
    }

}
