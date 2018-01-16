package com.springboot.starter.audit.audit;

import com.springboot.starter.audit.Application;
import com.springboot.starter.audit.domain.Post;
import com.springboot.starter.audit.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@ActiveProfiles(profiles = { "no-liquibase" })
public class PostAuditTest {

    private static final String BASE_API_POST = "http://localhost:8080/api/posts";
    private static final String BASE_API_USER = "http://localhost:8080/api/users";

    private TestRestTemplate testRestTemplate;

    @Before
    public void init() {
        this.testRestTemplate = new TestRestTemplate();
    }

    @Test
    public void storePostTest()
    {
        // create user.
        User user = prepareUser();
        HttpEntity requestEntity = new HttpEntity<>(user);
        ResponseEntity<User> responseEntity = testRestTemplate.exchange(
                BASE_API_USER, HttpMethod.POST, requestEntity, User.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(responseEntity.getBody());
        user.setId(responseEntity.getBody().getId());

        // store post
        Post post = preparePost(user);
        HttpEntity postRequestEntity = new HttpEntity<>(post);
        ResponseEntity<Post> postResponseEntity = testRestTemplate.exchange(
                BASE_API_POST, HttpMethod.POST, postRequestEntity, Post.class
        );

        assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(postResponseEntity.getBody());
        assertThat(postResponseEntity.getBody().getTitle()).isEqualTo(post.getTitle());
        assertThat(postResponseEntity.getBody().getUser().getId()).isEqualTo(post.getUser().getId());
    }

    @Test
    public void updatePostTest()
    {
        // create user.
        User user = prepareUser();
        HttpEntity requestEntity = new HttpEntity<>(user);
        ResponseEntity<User> responseEntity = testRestTemplate.exchange(
                BASE_API_USER, HttpMethod.POST, requestEntity, User.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(responseEntity.getBody());
        user.setId(responseEntity.getBody().getId());

        // store post
        Post post = preparePost(user);
        HttpEntity postRequestEntity = new HttpEntity<>(post);
        ResponseEntity<Post> postResponseEntity = testRestTemplate.exchange(
                BASE_API_POST, HttpMethod.POST, postRequestEntity, Post.class
        );
        assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(postResponseEntity.getBody());

        // update post.
        Integer postId = postResponseEntity.getBody().getId();
        Post postToBeUpdated = prepareUpdatePost(postId, user);

        HttpEntity updatePostRequest = new HttpEntity<>(postToBeUpdated);
        ParameterizedTypeReference<Map<String, Object>> typeRef =
                new ParameterizedTypeReference<Map<String, Object>>() {};

        ResponseEntity<Map<String, Object>> updatePostResponseEntity = testRestTemplate.exchange(
                BASE_API_POST + "/" + postId, HttpMethod.PUT, updatePostRequest, typeRef
        );

        assertThat(updatePostResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(updatePostResponseEntity.getBody());
        assertThat(updatePostResponseEntity.getBody().get("id")).isEqualTo(postToBeUpdated.getId());
        assertThat(updatePostResponseEntity.getBody().get("title")).isEqualTo(postToBeUpdated.getTitle());
    }

    /**
     * Method for preparing Post object.
     *
     * @param user - provided user object
     * @return prepared Post object
     */
    private Post preparePost(User user)
    {
        Post post = new Post();
        post.setTitle("Post title");
        post.setUser(user);

        return post;
    }

    /**
     * Method for preparing Post object for updating.
     *
     * @param id - post identifier.
     * @param user - user dependency.
     * @return prepared Post object for update.
     */
    private Post prepareUpdatePost(Integer id, User user)
    {
        Post post = new Post();
        post.setId(id);
        post.setTitle("Updated title");
        post.setUpdatedAt(new Date());
        post.setUser(user);

        return post;
    }

    /**
     * Method for preparing user for storing.
     *
     * @return prepare User object.
     */
    private User prepareUser()
    {
        User user = new User();
        user.setFirstName("Heril");
        user.setLastName("Muratovic");

        return user;
    }

}
