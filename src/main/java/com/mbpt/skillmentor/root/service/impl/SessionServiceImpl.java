package com.mbpt.skillmentor.root.service.impl;

import com.mbpt.skillmentor.root.dto.SessionDTO;
import com.mbpt.skillmentor.root.entity.ClassRoomEntity;
import com.mbpt.skillmentor.root.entity.MentorEntity;
import com.mbpt.skillmentor.root.entity.SessionEntity;
import com.mbpt.skillmentor.root.entity.StudentEntity;
import com.mbpt.skillmentor.root.mapper.SessionEntityDTOMapper;
import com.mbpt.skillmentor.root.repository.ClassRoomRepository;
import com.mbpt.skillmentor.root.repository.MentorRepository;
import com.mbpt.skillmentor.root.repository.SessionRepository;
import com.mbpt.skillmentor.root.repository.StudentRepository;
import com.mbpt.skillmentor.root.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public SessionDTO createSession(SessionDTO sessionDTO) {

        Optional<ClassRoomEntity> opClassroomEntity = classRoomRepository.findById(sessionDTO.getClassRoom().getClassRoomId());
        Optional<StudentEntity> opStudentEntity = studentRepository.findById(sessionDTO.getStudent().getStudentId());
        Optional<MentorEntity> opMentorEntity = mentorRepository.findById(sessionDTO.getMentor().getMentorId());

        if (opClassroomEntity.isPresent() && opStudentEntity.isPresent() && opMentorEntity.isPresent()) {
            ClassRoomEntity classRoomEntity = opClassroomEntity.get();
            MentorEntity mentorEntity = opMentorEntity.get();
            StudentEntity studentEntity = opStudentEntity.get();
            SessionEntity sessionEntity = SessionEntityDTOMapper.map(sessionDTO, classRoomEntity, mentorEntity, studentEntity);
            SessionEntity savedSessionEntity = sessionRepository.save(sessionEntity);
            return SessionEntityDTOMapper.map(savedSessionEntity);
        }

        return null;
    }

    @Override
    public SessionDTO getSessionById(Integer sessionId) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        return SessionEntityDTOMapper.map(sessionEntity);
    }

    @Override
    public List<SessionDTO> getAllSessions() {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        return sessionEntities.stream().map(SessionEntityDTOMapper::map).toList();
    }
}
