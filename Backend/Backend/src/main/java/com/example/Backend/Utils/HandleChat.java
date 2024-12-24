package com.example.Backend.Utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HandleChat {

    public String generateRoomId(Long senderId, Long receiverId) {
        return senderId < receiverId ? senderId + "_" + receiverId : receiverId + "_" + senderId;
    }
}
