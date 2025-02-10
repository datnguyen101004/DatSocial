package com.example.Backend.repository;

import com.example.Backend.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long> {
    Optional<VerifyCode> findByEmail(String email);

    Optional<VerifyCode> findByCode(String code);
}
