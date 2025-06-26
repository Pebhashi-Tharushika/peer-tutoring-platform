package com.mbpt.skillmentor.root.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomDTO {
    @JsonProperty("id")
    private Integer classRoomId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("enrolled_student_count")
    private Integer enrolledStudentCount;

    @JsonProperty("mentor")
    private MentorDTO mentorDTO;
}
