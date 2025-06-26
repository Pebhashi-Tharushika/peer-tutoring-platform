package com.mbpt.skillmentor.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDTO {
    private Integer sessionId;
    private ClassRoomDTO classRoomDTO;
    private MentorDTO mentorDTO;
    private StudentDTO studentDTO;
    private String topic;
    private Instant startTime;
    private Instant endTime;
}
