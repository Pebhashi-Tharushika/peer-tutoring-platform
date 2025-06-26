package com.mbpt.skillmentor.root.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sessions")
public class LiteSessionEntity {
    @Id
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "class_room_id")
    private Integer classRoomId;

    @Column(name = "mentor_id")
    private Integer mentorId;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;
}
