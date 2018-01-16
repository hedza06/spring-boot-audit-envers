package com.springboot.starter.audit.audit;

import com.springboot.starter.audit.Application;
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
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@WebAppConfiguration
@ActiveProfiles(profiles = { "no-liquibase" })
public class UserAuditTest {

    private static final String BASE_API = "http://localhost:8080/api/users";

    private TestRestTemplate testRestTemplate;

    @Before
    public void init() {
        this.testRestTemplate = new TestRestTemplate();
    }

    @Test
    public void storeUserTest()
    {
        User user = prepareUser();

        HttpEntity requestEntity = new HttpEntity<>(user);
        ResponseEntity<User> responseEntity = testRestTemplate.exchange(
                BASE_API, HttpMethod.POST, requestEntity, User.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(responseEntity.getBody());

        // check response from server
        User storedUser = responseEntity.getBody();
        assertThat(storedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(storedUser.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    public void updateUserTest()
    {
        // create user.
        User user = prepareUser();
        HttpEntity createRequestEntity = new HttpEntity<>(user);
        ResponseEntity<User> createResponseEntity = testRestTemplate.exchange(
                BASE_API, HttpMethod.POST, createRequestEntity, User.class
        );
        assertNotNull(createResponseEntity.getBody());
        assertThat(createResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Integer userId = createResponseEntity.getBody().getId();

        /*
        |------------------------------------------------------------------------------------------------|
        */

        User userToUpdate = prepareUpdateUser(userId);
        HttpEntity updateRequestEntity = new HttpEntity<>(userToUpdate);

        ParameterizedTypeReference<Map<String, Object>> typeRef =
                new ParameterizedTypeReference<Map<String, Object>>() {};

        ResponseEntity<Map<String, Object>> updateResponseEntity = testRestTemplate.exchange(
                BASE_API + "/" + userId, HttpMethod.PUT, updateRequestEntity, typeRef
        );
        assertNotNull(updateResponseEntity.getBody());
        assertThat(updateResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // check response from server
        Map<String, Object> updatedUser = updateResponseEntity.getBody();
        assertThat(updatedUser.get("id")).isEqualTo(userToUpdate.getId());
        assertThat(updatedUser.get("firstName")).isEqualTo(userToUpdate.getFirstName());
        assertThat(updatedUser.get("lastName")).isEqualTo(userToUpdate.getLastName());
    }

    @Test
    public void deleteUserTest()
    {
        // create user.
        User user = prepareUser();
        HttpEntity createRequestEntity = new HttpEntity<>(user);
        ResponseEntity<User> createResponseEntity = testRestTemplate.exchange(
                BASE_API, HttpMethod.POST, createRequestEntity, User.class
        );
        assertNotNull(createResponseEntity.getBody());
        assertThat(createResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Integer userId = createResponseEntity.getBody().getId();

        // delete already created user.
        HttpEntity deleteRequestEntity = new HttpEntity<>(Void.class);

        ParameterizedTypeReference<Map<String, Object>> typeRef =
                new ParameterizedTypeReference<Map<String, Object>>() {};

        ResponseEntity<Map<String, Object>> deleteResponseEntity = testRestTemplate
                .exchange(BASE_API + "/" + userId, HttpMethod.DELETE, deleteRequestEntity, typeRef);

        assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResponseEntity.getBody().get("message")).isEqualTo("Object successfully deleted.");
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

    /**
     * Method for preparing user for updating.
     *
     * @param id - user identifier to be set.
     * @return prepared User object.
     */
    private User prepareUpdateUser(Integer id)
    {
        User user = new User();
        user.setId(id);
        user.setFirstName("Update 1");
        user.setLastName("Update 2");

        return user;
    }
}
