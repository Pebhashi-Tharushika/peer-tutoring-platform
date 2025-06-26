package com.mbpt.skillmentor.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorDTO {
    private Integer mentorId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String title;
    private String profession;
    private String subject;
    private String qualification;
    private Integer classRoomId;
}
