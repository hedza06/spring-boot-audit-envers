package com.springboot.starter.audit.resources;

import com.springboot.starter.audit.domain.User;
import com.springboot.starter.audit.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private UserService userService;

    /**
     * Constructor / dependency injector.
     * @param userService - user service layer dependency.
     */
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> storeUser(@Valid @RequestBody User user)
    {
        User storedUser = this.userService.save(user);
        return Optional.ofNullable(storedUser)
                .map(usr -> new ResponseEntity<>(usr, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") Integer id, @RequestBody User user)
    {
        if (id == null)
        {
            Map<String, Object> errorMap = new HashMap<>(2);
            errorMap.put("status", 400);
            errorMap.put("message", "Missing id parameter.");

            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }

        // update user.
        user.setId(id);
        User updatedUser = this.userService.save(user);
        return Optional.ofNullable(updatedUser)
                .map(updUser -> new ResponseEntity<>(updUser, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Integer id)
    {
        Map<String, Object> response = new HashMap<>(2);
        if (id == null)
        {
            response.put("status", 400);
            response.put("message", "Missing id parameter.");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        this.userService.delete(id);
        response.put("status", 200);
        response.put("message", "Object successfully deleted.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
