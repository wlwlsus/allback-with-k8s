package com.allback.cygiuser.repository;

import com.allback.cygiuser.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {
    List<Users> findAll();
    Users findUserByUuid(String uuid);

}
