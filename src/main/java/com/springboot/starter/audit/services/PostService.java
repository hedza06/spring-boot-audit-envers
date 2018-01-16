package com.springboot.starter.audit.services;

import com.springboot.starter.audit.domain.Post;
import com.springboot.starter.audit.repository.PostRepository;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Description(value = "Post service layer dependency.")
@Service
@Transactional
public class PostService {

    private PostRepository postRepository;

    /**
     * Constructor / dependency injector.
     * @param postRepository - post repository.
     */
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Method for saving new post object.
     *
     * @param post - object to be stored.
     * @return stored post object.
     */
    public Post save(Post post) {
        return this.postRepository.save(post);
    }
}
