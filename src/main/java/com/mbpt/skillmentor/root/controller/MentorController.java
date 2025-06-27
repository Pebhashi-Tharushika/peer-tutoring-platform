package com.mbpt.skillmentor.root.controller;

import com.mbpt.skillmentor.root.common.Constants;
import com.mbpt.skillmentor.root.dto.MentorDTO;
import com.mbpt.skillmentor.root.service.MentorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/academic")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    public MentorController() {
    }

    @PostMapping(value = "/mentor", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<MentorDTO> createMentor(@Valid @RequestBody MentorDTO mentorDTO) {
        final MentorDTO createdMentor = mentorService.createMentor(mentorDTO);
        return ResponseEntity.ok(createdMentor);
    }

    @GetMapping(value = "/mentor", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<MentorDTO>> getAllMentors(
            @RequestParam(required = false) List<String> firstNames,
            @RequestParam(required = false) List<String> subjects) {
        final List<MentorDTO> mentorDTOS = mentorService.getAllMentors(firstNames, subjects);
        return ResponseEntity.ok(mentorDTOS);
    }

    @GetMapping(value = "/mentor/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<MentorDTO> getMentorById(
            @PathVariable
            @Min(value = 1, message = "Mentor ID must be a positive integer") Integer id) {
        final MentorDTO retrievedMentor = mentorService.findMentorById(id);
        return ResponseEntity.ok(retrievedMentor);
    }

    @PutMapping(value = "/mentor", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<MentorDTO> updateMentor(@Valid @RequestBody MentorDTO mentorDTO) {
        final MentorDTO updatedMentor = mentorService.updateMentorById(mentorDTO);
        return ResponseEntity.ok(updatedMentor);
    }

    @DeleteMapping(value = "/mentor/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<MentorDTO> deleteMentor(
            @Min(value = 1, message = "Mentor ID must be a positive integer")
            @PathVariable Integer id) {
        final MentorDTO deletedMentor = mentorService.deleteMentorById(id);
        return ResponseEntity.ok(deletedMentor);
    }
}
