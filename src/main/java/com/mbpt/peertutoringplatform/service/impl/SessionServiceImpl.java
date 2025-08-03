package com.mbpt.peertutoringplatform.service.impl;

import com.mbpt.peertutoringplatform.common.Constants;
import com.mbpt.peertutoringplatform.dto.AuditDTO;
import com.mbpt.peertutoringplatform.dto.PaymentDTO;
import com.mbpt.peertutoringplatform.dto.SessionDTO;
import com.mbpt.peertutoringplatform.dto.SessionLiteDTO;
import com.mbpt.peertutoringplatform.entity.LiteSessionEntity;
import com.mbpt.peertutoringplatform.entity.SessionEntity;
import com.mbpt.peertutoringplatform.exception.SessionException;
import com.mbpt.peertutoringplatform.mapper.AuditEntityDTOMapper;
import com.mbpt.peertutoringplatform.mapper.LiteSessionEntityDTOMapper;
import com.mbpt.peertutoringplatform.mapper.SessionEntityDTOMapper;
import com.mbpt.peertutoringplatform.repository.LiteSessionRepository;
import com.mbpt.peertutoringplatform.repository.SessionRepository;
import com.mbpt.peertutoringplatform.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

    @Value("${spring.datasource.url}")
    private String datasource;

    private final SessionRepository sessionRepository;

    private final LiteSessionRepository liteSessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository, LiteSessionRepository liteSessionRepository) {
        this.sessionRepository = sessionRepository;
        this.liteSessionRepository = liteSessionRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SessionLiteDTO createSession(SessionLiteDTO sessionDTO) {
        log.info("Creating new session...");
        if (sessionDTO == null) {
            log.error("Failed to create session: input DTO is null.");
            throw new IllegalArgumentException("Session data must not be null.");
        }
        log.debug("ClassRoomDTO received: {}", sessionDTO);
        LiteSessionEntity sessionEntity = LiteSessionEntityDTOMapper.map(sessionDTO);
        LiteSessionEntity savedEntity = liteSessionRepository.save(sessionEntity);
        log.info("Created classroom with ID: {} at data-source: {}", savedEntity.getSessionId(), this.datasource);
        return LiteSessionEntityDTOMapper.map(savedEntity);
    }


    @Override
    @Transactional(readOnly = true)
    public SessionDTO getSessionById(Integer sessionId) {
        log.info("Fetching session by ID...");
        return sessionRepository.findById(sessionId)
                .map(session -> {
                    log.info("Session found: {}", session);
                    return SessionEntityDTOMapper.map(session);
                })
                .orElseThrow(() -> {
                    log.error("Session not found with ID: {} from data-source:{}", sessionId, this.datasource);
                    return new SessionException("Session not found with ID: " + sessionId);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDTO> getAllSessions() {
        log.info("Fetching all sessions...");
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        List<SessionDTO> sessionDTOS = sessionEntities.stream().map(SessionEntityDTOMapper::map).toList();
        log.info("Found {} sessions from data-source: {}", sessionDTOS.size(), this.datasource);
        return sessionDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDTO> getAllSessionsByStudentClerkId(String studentClerkId) {
        log.info("Fetching session by student Clerk ID...");
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        List<SessionDTO> sessionDTOS = sessionEntities.stream()
                .filter(sessionEntity -> sessionEntity.getStudentEntity().getClerkStudentId().equals(studentClerkId))
                .map(SessionEntityDTOMapper::map).toList();
        log.info("Found {} sessions of student with Clerk ID: {} from data-source: {}", sessionDTOS.size(), studentClerkId, this.datasource);
        return sessionDTOS;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SessionDTO updateSessionStatus(Integer sessionId, Constants.SessionStatus sessionStatus) {
        log.info("Updating session status by session ID...");

        if (sessionId == null || sessionStatus == null) {
            log.error("Failed to update session status: session ID or session status is null.");
            throw new IllegalArgumentException("session ID and session status must not be null for update the status.");
        }

        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(() -> {
            log.error("Failed to update session status: session not found with ID: {}", sessionId);
            return new SessionException("session not found with ID: " + sessionId);
        });

        sessionEntity.setSessionStatus(sessionStatus);
        SessionEntity updatedEntity = sessionRepository.save(sessionEntity);

        log.info("Updated status of the session with ID: {}", sessionId);
        return SessionEntityDTOMapper.map(updatedEntity);
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
