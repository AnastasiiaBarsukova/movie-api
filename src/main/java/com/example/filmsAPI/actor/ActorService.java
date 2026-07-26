package com.example.filmsAPI.actor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.filmsAPI.common.ResourceNotFoundException;

@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository){
        this.actorRepository = actorRepository;
    }

    public ActorInfo getActorInfoBy(int id) {
        return actorRepository.getActorBy(id).orElseThrow(() -> new ResourceNotFoundException("Actor", id));
    }

    public List<ActorInfo> getAllActors() {
        return actorRepository.getAllActors();
    }

    public ActorInfo addActor(ActorInfo actorInfo) {
        return actorRepository.addActor(actorInfo);
    }

    public void deleteActorBy(int id) {
        actorRepository.deleteActorBy(id);
    }
}
