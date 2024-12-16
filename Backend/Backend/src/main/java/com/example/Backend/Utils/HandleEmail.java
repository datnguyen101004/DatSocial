package com.example.Backend.Utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HandleEmail {
    public String createCode() {
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += (int) (Math.random() * 10);
        }
        return code;
    }
}
