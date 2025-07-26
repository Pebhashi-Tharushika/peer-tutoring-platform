package com.mbpt.skillmentor.root.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomDTO {
    @JsonProperty("class_room_id")
    private Integer classRoomId;

    @NotBlank(message = "Title must not be blank")
    @JsonProperty("title")
    private String title;

    @NotNull(message = "Enrolled student count must not be null")
    @JsonProperty("enrolled_student_count")
    private Integer enrolledStudentCount;

    @NotBlank(message = "class_image must not be blank")
    @JsonProperty("class_image")
    private String classImage;

    @JsonProperty("mentor")
    private MentorDTO mentorDTO;
}
