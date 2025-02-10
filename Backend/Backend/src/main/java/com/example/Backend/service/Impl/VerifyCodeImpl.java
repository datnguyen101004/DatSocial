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
        verifyCode.setExpireAt(LocalDateTime.now().plusSeconds(60*5));
        verifyCodeRepository.save(verifyCode);
    }

    @Override
    public String verifyCode(String code) {
        VerifyCode verifyCode = verifyCodeRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Invalid code"));
        if (verifyCode.getCode().equals(code) && !verifyCode.getExpireAt().isBefore(LocalDateTime.now()) && !verifyCode.isUsed()) {
            verifyCode.setUsed(true);
            verifyCodeRepository.save(verifyCode);
            return verifyCode.getEmail();
        }
        return "";
    }
}
