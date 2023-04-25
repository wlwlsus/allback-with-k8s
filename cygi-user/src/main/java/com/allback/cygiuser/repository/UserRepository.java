package com.allback.cygiuser.repository;

import com.allback.cygiuser.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
