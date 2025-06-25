package com.mbpt.skillmentor.root.service;

import com.mbpt.skillmentor.root.dto.SessionDTO;

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
    public abstract SessionDTO createSession(SessionDTO sessionDTO);


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
     * @return a list of SessionDTO objects representing the sessions
     */
    public abstract List<SessionDTO> getAllSessions();
}
