package com.example.filmsAPI.actor;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/actors")
public class ActorController {
    private final ActorService actorService;

    public ActorController(ActorService actorService){
        this.actorService = actorService;
    }

    @GetMapping("/{id}")
    public ActorInfo getActorBy(@PathVariable int id) {
        return actorService.getActorInfoBy(id);
    }
    
    @GetMapping()
    public List<ActorInfo> getAllActors() {
        return actorService.getAllActors();
    }

    @PostMapping()
    public ActorInfo addActor(@RequestParam String firstName, @RequestParam String lastName) {
        ActorInfo actor = new ActorInfo(null, firstName, lastName);
        return actorService.addActor(actor);
    }

    @DeleteMapping("/{id}")
    public void addActor(@PathVariable int id) {
        actorService.deleteActorBy(id);
    }

}
