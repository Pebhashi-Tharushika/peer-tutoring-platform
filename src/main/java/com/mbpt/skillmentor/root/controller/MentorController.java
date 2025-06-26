package com.mbpt.skillmentor.root.controller;

import com.mbpt.skillmentor.root.dto.MentorDTO;
import com.mbpt.skillmentor.root.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @PostMapping
    public ResponseEntity<MentorDTO> createMentor(@RequestBody MentorDTO mentorDTO) {
        MentorDTO createdMentor = mentorService.createMentor(mentorDTO);
        return new ResponseEntity<>(createdMentor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MentorDTO>> getAllMentors(@RequestParam List<String> firstNames, @RequestParam List<String> subjects) {
        return new ResponseEntity<>(mentorService.getAllMentors(firstNames,subjects), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorDTO> getMentorById(@PathVariable Integer id) {
        MentorDTO retrievedMentor = mentorService.getMentorById(id);
        return new ResponseEntity<>(retrievedMentor, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MentorDTO> updateMentor(@RequestBody MentorDTO mentorDTO) {
        MentorDTO updatedMentor = mentorService.updateMentorById(mentorDTO);
        return new ResponseEntity<>(updatedMentor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MentorDTO> deleteMentor(@PathVariable Integer id) {
        MentorDTO deletedMentor = mentorService.deleteMentorById(id);
        return new ResponseEntity<>(deletedMentor, HttpStatus.OK);
    }
}
