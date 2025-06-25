package com.mbpt.skillmentor.root.mapper;

import com.mbpt.skillmentor.root.dto.SessionDTO;
import com.mbpt.skillmentor.root.entity.ClassRoomEntity;
import com.mbpt.skillmentor.root.entity.MentorEntity;
import com.mbpt.skillmentor.root.entity.SessionEntity;
import com.mbpt.skillmentor.root.entity.StudentEntity;

public class SessionEntityDTOMapper {

    public static SessionDTO map(SessionEntity sessionEntity) {
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSessionId(sessionEntity.getSessionId());
        sessionDTO.setClassRoom(ClassRoomEntityDTOMapper.map(sessionEntity.getClassRoomEntity()));
        sessionDTO.setMentor(MentorEntityDTOMapper.map(sessionEntity.getMentorEntity()));
        sessionDTO.setStudent(StudentEntityDTOMapper.map(sessionEntity.getStudentEntity()));
        sessionDTO.setStartTime(sessionEntity.getStartTime());
        sessionDTO.setEndTime(sessionEntity.getEndTime());
        return sessionDTO;
    }

    public static SessionEntity map(SessionDTO sessionDTO, ClassRoomEntity classRoomEntity, MentorEntity mentorEntity, StudentEntity studentEntity) {
        return new SessionEntity(
                sessionDTO.getSessionId(),
                classRoomEntity,
                mentorEntity,
                studentEntity,
                sessionDTO.getStartTime(),
                sessionDTO.getEndTime()
        );
    }
}
