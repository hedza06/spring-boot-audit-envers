package com.springboot.starter.audit.repository;

import com.springboot.starter.audit.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // write some custom methods...

}
