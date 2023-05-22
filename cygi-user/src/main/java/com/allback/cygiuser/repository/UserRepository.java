package com.allback.cygiuser.repository;

import com.allback.cygiuser.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {
  Page<Users> findAll(Pageable pageable);

  Users findUsersByUuid(String uuid);



}
