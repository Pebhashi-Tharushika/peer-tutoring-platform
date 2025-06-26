package com.mbpt.skillmentor.root.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "classrooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_room_id")
    private Integer classRoomId;

    @Column(name = "title")
    private String title;

    @Column(name = "session_fee")
    private Double sessionFee;

    @Column(name = "enrolled_student_count")
    private Integer enrolledStudentCount;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", referencedColumnName = "mentor_id")
    private MentorEntity mentor;

}
