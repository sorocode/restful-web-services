package com.sorocode.rest.webservices.restful_web_services.user;

import com.sorocode.rest.webservices.restful_web_services.jpa.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJPAResource {
    private UserDaoService service;
    private UserRepository respository;

    public UserJPAResource(UserDaoService service, UserRepository respository) {
        this.service = service;
        this.respository = respository;
    }

    // GET /users
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return respository.findAll();
    }

    //http://localhost:8080/users

    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUserById(@PathVariable int id) {
        Optional<User> user = respository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("id:"+id);
        }
        EntityModel<User> entityModel = EntityModel.of(user.get());

        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }

    // Post /users
    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser=respository.save(user);

        // location - /users/4
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // Delete /users/{id}
    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        respository.deleteById(id);
    }
}
