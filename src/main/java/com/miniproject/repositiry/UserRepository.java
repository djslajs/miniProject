package com.miniproject.repositiry;

import com.miniproject.domain.LoginUser;
import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<LoginUser, Long> {

    Optional<LoginUser> findByEmailAndPassword(String email, String password);
}
