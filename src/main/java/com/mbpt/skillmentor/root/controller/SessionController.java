package com.mbpt.skillmentor.root.controller;

import com.mbpt.skillmentor.root.common.Constants;
import com.mbpt.skillmentor.root.dto.SessionDTO;
import com.mbpt.skillmentor.root.dto.SessionLiteDTO;
import com.mbpt.skillmentor.root.service.SessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/academic")
public class SessionController {

    @Autowired
    private SessionService sessionService;


    @PostMapping(value = "/session", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<SessionLiteDTO> createSession(@Valid @RequestBody SessionLiteDTO sessionDTO) {
        final SessionLiteDTO createdSession = sessionService.createSession(sessionDTO);

        if (createdSession != null) {
            return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<SessionDTO> getSessionById(
            @Min(value = 1, message = "Mentor ID must be a positive integer")
            @PathVariable Integer sessionId) {
        final SessionDTO sessionDTO = sessionService.getSessionById(sessionId);
        if (sessionDTO != null) {
            return new ResponseEntity<>(sessionDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/session", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        final List<SessionDTO> sessions = sessionService.getAllSessions();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

}
