package com.mbpt.skillmentor.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionLiteDTO {
    private Integer sessionId;
    private Integer studentId;
    private Integer classRoomId;
    private Integer mentorId;
    private String topic;
    private Instant startTime;
    private Instant endTime;
}
