package com.mbpt.skillmentor.root.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorDTO {
    private Integer mentorId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String title;
    private String profession;
    private String subject;
    private String qualification;

    @JsonProperty("classrooms") // classrooms is the key of the corresponding json property which has list of ClassRoomDTO as its value
    private List<ClassRoomDTO> classRoomDTOList;
    private List<Integer> classRoomIds = new ArrayList<>();
}
