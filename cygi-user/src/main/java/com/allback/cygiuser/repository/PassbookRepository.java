package com.allback.cygiuser.repository;

import com.allback.cygiuser.entity.Passbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassbookRepository<T extends Passbook> extends JpaRepository<T, Long> {
    Passbook save(Passbook passbook);
}
