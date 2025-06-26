package com.mbpt.skillmentor.root.service.impl;

import com.mbpt.skillmentor.root.dto.SessionDTO;
import com.mbpt.skillmentor.root.dto.SessionLiteDTO;
import com.mbpt.skillmentor.root.entity.LiteSessionEntity;
import com.mbpt.skillmentor.root.entity.SessionEntity;
import com.mbpt.skillmentor.root.mapper.LiteSessionEntityDTOMapper;
import com.mbpt.skillmentor.root.mapper.SessionEntityDTOMapper;
import com.mbpt.skillmentor.root.repository.*;
import com.mbpt.skillmentor.root.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private LiteSessionRepository liteSessionRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public SessionLiteDTO createSession(SessionLiteDTO sessionDTO) {
        final LiteSessionEntity liteSessionEntity = LiteSessionEntityDTOMapper.map(sessionDTO);
        final LiteSessionEntity savedEntity = liteSessionRepository.save(liteSessionEntity);
        return LiteSessionEntityDTOMapper.map(savedEntity);
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
