package com.mbpt.skillmentor.root.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionEntity {

    @Id
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;

    @ManyToOne()
    @JoinColumn(name = "class_room_id", referencedColumnName = "class_room_id")
    private ClassRoomEntity classRoomEntity;

    @ManyToOne()
    @JoinColumn(name = "mentor_id", referencedColumnName = "mentor_id")
    private MentorEntity mentorEntity;

    @ManyToOne()
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private StudentEntity studentEntity;

    @Column(name = "topic")
    private String topic;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;
}
