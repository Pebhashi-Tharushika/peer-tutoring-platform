package com.mbpt.peertutoringplatform.mapper;


import com.mbpt.peertutoringplatform.entity.MentorEntity;
import com.mbpt.peertutoringplatform.dto.MentorDTO;

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
        mentorDTO.setMentorImage(mentorEntity.getMentorImage());
        mentorDTO.setIsCertified(mentorEntity.getIsCertified());
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
        mentorEntity.setIsCertified(mentorDTO.getIsCertified());
        return mentorEntity;
    }
}
