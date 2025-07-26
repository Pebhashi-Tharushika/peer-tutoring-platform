package com.mbpt.skillmentor.root.service.impl;

import com.mbpt.skillmentor.root.common.Constants;
import com.mbpt.skillmentor.root.dto.AuditDTO;
import com.mbpt.skillmentor.root.dto.PaymentDTO;
import com.mbpt.skillmentor.root.dto.SessionDTO;
import com.mbpt.skillmentor.root.dto.SessionLiteDTO;
import com.mbpt.skillmentor.root.entity.LiteSessionEntity;
import com.mbpt.skillmentor.root.entity.SessionEntity;
import com.mbpt.skillmentor.root.entity.StudentEntity;
import com.mbpt.skillmentor.root.mapper.AuditEntityDTOMapper;
import com.mbpt.skillmentor.root.mapper.LiteSessionEntityDTOMapper;
import com.mbpt.skillmentor.root.mapper.SessionEntityDTOMapper;
import com.mbpt.skillmentor.root.mapper.StudentEntityDTOMapper;
import com.mbpt.skillmentor.root.repository.LiteSessionRepository;
import com.mbpt.skillmentor.root.repository.SessionRepository;
import com.mbpt.skillmentor.root.service.SessionService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {


    private final SessionRepository sessionRepository;

    private final LiteSessionRepository liteSessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository, LiteSessionRepository liteSessionRepository) {
        this.sessionRepository = sessionRepository;
        this.liteSessionRepository = liteSessionRepository;
    }

    @Override
    public SessionLiteDTO createSession(final SessionLiteDTO sessionDTO) {
        if (sessionDTO == null) {
            throw new IllegalArgumentException("Session data must not be null.");
        }
        LiteSessionEntity sessionEntity = LiteSessionEntityDTOMapper.map(sessionDTO);
        LiteSessionEntity savedEntity = liteSessionRepository.save(sessionEntity);
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

    @Override
    public List<SessionDTO> getAllSessionsByStudentClerkId(String studentClerkId) {
        List<Object> rawResults = sessionRepository.findSessionsByStudentClerkId(studentClerkId);

        if (rawResults == null || rawResults.isEmpty()) {
            return Collections.emptyList();
        }

        return rawResults.stream().map(obj -> {
            SessionEntity sessionEntity = (SessionEntity) obj;
            System.out.println(sessionEntity);
            return SessionEntityDTOMapper.map(sessionEntity);
        }).toList();

    }

    @Override
    public SessionDTO updateSessionStatus(Integer sessionId, Constants.SessionStatus sessionStatus) {
        return null;
    }

    @Override
    public List<AuditDTO> getAllAudits() {
        final List<SessionEntity> sessionEntityList = sessionRepository.findAll();
        return sessionEntityList.stream().map(AuditEntityDTOMapper::map).toList();
    }

    @Override
    public List<PaymentDTO> findMentorPayments(String startDate, String endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null.");
        }
        List<Object> rawResults = sessionRepository.findMentorPayments(startDate, endDate);
        if (rawResults == null || rawResults.isEmpty()) {
            return Collections.emptyList();
        }
        return rawResults.stream().map(obj -> {
            Object[] row = (Object[]) obj;
            Integer mentorId = (Integer) row[0];
            String mentorName = (String) row[1];
            Double totalFee = (Double) row[2];
            return new PaymentDTO(mentorId, mentorName, totalFee);
        }).toList();
    }
}
