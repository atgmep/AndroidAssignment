package com.mobile.makefive.repository;

import com.mobile.makefive.entity.TblAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<TblAccount, Integer> {

    Optional<TblAccount> findByUsername(String username);
}
