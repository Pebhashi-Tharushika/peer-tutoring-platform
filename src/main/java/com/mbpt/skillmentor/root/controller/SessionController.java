package com.mbpt.skillmentor.root.controller;

import com.mbpt.skillmentor.root.dto.SessionDTO;
import com.mbpt.skillmentor.root.dto.SessionLiteDTO;
import com.mbpt.skillmentor.root.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;


    @PostMapping
    public ResponseEntity<SessionLiteDTO> createSession(@RequestBody SessionLiteDTO sessionDTO) {
        final SessionLiteDTO createdSession = sessionService.createSession(sessionDTO);

        if (createdSession != null) {
            return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Integer sessionId) {
        SessionDTO sessionDTO = sessionService.getSessionById(sessionId);
        if (sessionDTO != null) {
            return new ResponseEntity<>(sessionDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        List<SessionDTO> sessions = sessionService.getAllSessions();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

}
