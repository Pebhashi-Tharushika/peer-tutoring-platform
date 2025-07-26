package com.mbpt.skillmentor.root.mapper;


import com.mbpt.skillmentor.root.dto.MentorDTO;
import com.mbpt.skillmentor.root.entity.MentorEntity;

public class MentorEntityDTOMapper {

    public static MentorDTO map(MentorEntity mentorEntity) {
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setMentorId(mentorEntity.getMentorId());
        mentorDTO.setClerkMentorId(mentorEntity.getClerkMentorId());
        mentorDTO.setFirstName(mentorEntity.getFirstName());
        mentorDTO.setLastName(mentorEntity.getLastName());
        mentorDTO.setEmail(mentorEntity.getEmail());
        mentorDTO.setAddress(mentorEntity.getAddress());
        mentorDTO.setPhoneNumber(mentorEntity.getPhoneNumber());
        mentorDTO.setTitle(mentorEntity.getTitle());
        mentorDTO.setSessionFee(mentorEntity.getSessionFee());
        mentorDTO.setProfession(mentorEntity.getProfession());
        mentorDTO.setSubject(mentorEntity.getSubject());
        mentorDTO.setQualification(mentorEntity.getQualification());
        mentorDTO.setMentorImage(mentorDTO.getMentorImage());
        return mentorDTO;
    }

    public static MentorEntity map(MentorDTO mentorDTO) {
        MentorEntity mentorEntity = new MentorEntity();
        mentorEntity.setMentorId(mentorDTO.getMentorId());
        mentorEntity.setClerkMentorId(mentorDTO.getClerkMentorId());
        mentorEntity.setFirstName(mentorDTO.getFirstName());
        mentorEntity.setLastName(mentorDTO.getLastName());
        mentorEntity.setEmail(mentorDTO.getEmail());
        mentorEntity.setAddress(mentorDTO.getAddress());
        mentorEntity.setPhoneNumber(mentorDTO.getPhoneNumber());
        mentorEntity.setTitle(mentorDTO.getTitle());
        mentorEntity.setSessionFee(mentorDTO.getSessionFee());
        mentorEntity.setProfession(mentorDTO.getProfession());
        mentorEntity.setSubject(mentorDTO.getSubject());
        mentorEntity.setQualification(mentorDTO.getQualification());
        mentorEntity.setMentorImage(mentorDTO.getMentorImage());
        return mentorEntity;
    }
}
