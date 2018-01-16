package com.springboot.starter.audit.repository;

import com.springboot.starter.audit.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // write custom query methods...

}
