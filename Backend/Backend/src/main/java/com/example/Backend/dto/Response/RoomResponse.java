package com.example.Backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private String roomId;
    private List<Long> userIds;
    private List<MessageResponse> messages;
}
