package com.springboot.starter.audit.resources;

import com.springboot.starter.audit.domain.Post;
import com.springboot.starter.audit.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostResource {

    private PostService postService;

    /**
     * Constructor / dependency injector.
     * @param postService - service layer dependency.
     */
    public PostResource(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> storePost(@Valid @RequestBody Post post)
    {
        Post storedPost = this.postService.save(post);
        return Optional.ofNullable(storedPost)
                .map(savedPost -> new ResponseEntity<>(savedPost, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePost(@PathVariable(value = "id") Integer id, @RequestBody Post post)
    {
        Map<String, Object> response = new HashMap<>(2);
        if (id == null)
        {
            response.put("status", 400);
            response.put("message", "Missing path variable id");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // generate update object.
        post.setId(id);
        Post updatedPost = this.postService.save(post);
        return Optional.ofNullable(updatedPost)
                .map(savedPost -> new ResponseEntity<>(savedPost, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
