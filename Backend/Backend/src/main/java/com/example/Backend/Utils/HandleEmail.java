package com.example.Backend.Utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HandleEmail {
    public String createCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append((int) (Math.random() * 10));
        }
        return code.toString();
    }
}
