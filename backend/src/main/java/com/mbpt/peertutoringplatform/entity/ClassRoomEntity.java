package com.mbpt.peertutoringplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classrooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomEntity {

    public ClassRoomEntity(String title, Integer enrolledStudentCount, String classImage) {
        this.title = title;
        this.enrolledStudentCount = enrolledStudentCount;
        this.classImage = classImage;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_room_id")
    private Integer classRoomId;

    @NotBlank(message = "Classroom title must not be blank")
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull(message = "Enrolled student count must not be null")
    @Column(name = "enrolled_student_count", nullable = false)
    private Integer enrolledStudentCount;

    @NotBlank(message = "Class image URL must not be blank")
    @Column(name = "class_image", nullable = false)
    private String classImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", referencedColumnName = "mentor_id")
    private MentorEntity mentorEntity;

    @OneToMany(mappedBy = "classRoomEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionEntity> sessionEntityList = new ArrayList<>();

    @Override
    public String toString() {
        return "ClassRoomEntity{" +
                "classRoomId=" + classRoomId +
                ", title='" + title + '\'' +
                ", enrolledStudentCount=" + enrolledStudentCount +
                ", classImage='" + classImage + '\'' +
                ", mentorID='" + mentorEntity.getMentorId() + '\'' +
                ", sessionEntityList=" + sessionEntityList +
                '}';
    }
}
