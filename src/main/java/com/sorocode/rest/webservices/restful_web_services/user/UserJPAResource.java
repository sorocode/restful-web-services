package com.sorocode.rest.webservices.restful_web_services.user;

import com.sorocode.rest.webservices.restful_web_services.jpa.PostRepository;
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
    private UserRepository respository;
    private PostRepository postRepository;
    public UserJPAResource(UserDaoService service, UserRepository respository,PostRepository postRepository) {
        this.respository = respository;
        this.postRepository=postRepository;
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

    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id) {
        Optional<User> user = respository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("id:"+id);
        }
        return user.get().getPosts();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Post> CreatePostForUser(@PathVariable int id,@Valid @RequestBody Post post) {
        Optional<User> user = respository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("id:"+id);
        }
        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
