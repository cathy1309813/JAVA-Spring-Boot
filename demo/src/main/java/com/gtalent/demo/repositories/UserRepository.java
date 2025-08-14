package com.gtalent.demo.repositories;

import com.gtalent.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    //select * from users where username = xxx;
    Optional<User> findByUsername(String username);  //自行寫第一個方法
    Optional<User> findByEmail(String email);  //自行寫第二個方法
}
