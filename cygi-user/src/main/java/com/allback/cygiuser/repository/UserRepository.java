package com.allback.cygiuser.repository;

import com.allback.cygiuser.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository<T extends Users> extends JpaRepository<T, Long> {
//    유저 정보 저장
    Users save(Users users);

//    Users findOneByuuid(String id);

    Users findOneByemail(String toString);
}
