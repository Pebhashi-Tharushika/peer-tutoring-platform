package com.mbpt.skillmentor.root.service;

import com.mbpt.skillmentor.root.dto.AuditDTO;
import com.mbpt.skillmentor.root.dto.PaymentDTO;
import com.mbpt.skillmentor.root.dto.SessionDTO;
import com.mbpt.skillmentor.root.dto.SessionLiteDTO;

import java.util.List;

/**
 * Service Interface for managing sessions.
 * Provides operations to create and retrieve session records.
 */
public interface SessionService {

    /**
     * Creates a new session.
     *
     * @param sessionDTO the session data transfer object containing session details
     * @return the created {@link SessionDTO } with generated mentor ID
     */
    public abstract SessionLiteDTO createSession(SessionLiteDTO sessionDTO);


    /**
     * Retrieves a session by its ID.
     *
     * @param sessionId the ID of the session to retrieve
     * @return a SessionDTO object representing the mentor
     */
    public abstract SessionDTO getSessionById(Integer sessionId);


    /**
     * Retrieves all sessions.
     *
     * @return a list of {@link SessionDTO} objects representing the sessions
     */
    public abstract List<SessionDTO> getAllSessions();


    /**
     * Retrieves all audits.
     *
     * @return a list of {@link AuditDTO} objects representing the audits
     */
    public abstract List<AuditDTO> getAllAudits();


    /**
     * Retrieves a list of mentor payments within the specified date range.
     *
     * @param startDate the start date of the time period (inclusive)
     * @param endDate   the end date of the time period (inclusive)
     * @return a list of {@link PaymentDTO} instances representing the payments made to mentors
     */
    public abstract List<PaymentDTO> findMentorPayments(String startDate, String endDate);
}
