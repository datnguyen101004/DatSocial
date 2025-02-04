package com.example.Backend.service.Impl;

import com.example.Backend.entity.VerifyCode;
import com.example.Backend.repository.VerifyCodeRepository;
import com.example.Backend.service.VerifyCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerifyCodeImpl implements VerifyCodeService {
    private final VerifyCodeRepository verifyCodeRepository;

    @Override
    public void saveCode(String email, String code) {
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setEmail(email);
        verifyCode.setCode(code);
        verifyCode.setExpireAt(LocalDateTime.now().plusSeconds(60));
        verifyCodeRepository.save(verifyCode);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        VerifyCode verifyCode = verifyCodeRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));
        if (verifyCode.getCode().equals(code) && !verifyCode.getExpireAt().isBefore(LocalDateTime.now()) && !verifyCode.isUsed()) {
            verifyCode.setUsed(true);
            verifyCodeRepository.save(verifyCode);
            return true;
        }
        return false;
    }
}
