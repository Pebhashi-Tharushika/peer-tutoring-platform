package com.mbpt.skillmentor.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditDTO {
    private Integer sessionId;
    private Integer studentId;
    private String studentFirstName;
    private String studentLastName;
    private String studentEmail;
    private String studentPhoneNumber;
    private String classTitle;
    private Integer mentorId;
    private String mentorFirstName;
    private String mentorLastName;
    private String mentorPhoneNumber;
    private Double fee;
    private Instant startTime;
    private Instant endTime;
    private String topic;
}
