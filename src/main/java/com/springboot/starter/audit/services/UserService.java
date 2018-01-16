package com.springboot.starter.audit.services;

import com.springboot.starter.audit.domain.User;
import com.springboot.starter.audit.repository.UserRepository;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Description(value = "Service layer of User Resource.")
@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    /**
     * Constructor / dependency injector.
     * @param userRepository - repository layer dependency.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method for saving user.
     *
     * @param user - object to be stored.
     * @return stored User object.
     */
    public User save(User user) {
        return this.userRepository.save(user);
    }

    /**
     * Method for deleting user object.
     *
     * @param id - user object identifier.
     */
    public void delete(Integer id) {
        this.userRepository.delete(id);
    }
}
