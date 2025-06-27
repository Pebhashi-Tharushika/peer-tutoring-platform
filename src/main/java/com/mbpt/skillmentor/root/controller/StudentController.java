package com.mbpt.skillmentor.root.controller;

import com.mbpt.skillmentor.root.common.Constants;
import com.mbpt.skillmentor.root.dto.StudentDTO;
import com.mbpt.skillmentor.root.service.StudentService;
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
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping(value = "/student", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        final StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping(value = "/student", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<StudentDTO>> getAllStudents(
            @RequestParam(required = false) List<String> address,
            @RequestParam(required = false) List<Integer> age,
            @RequestParam(required = false) List<Integer> firstNames) {
        final List<StudentDTO> studentsList = studentService.getAllStudents(address, age, firstNames);
        return ResponseEntity.ok(studentsList);
    }

    @GetMapping(value = "/student/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> getStudentById(
            @Min(value = 1, message = "Mentor ID must be a positive integer")
            @PathVariable Integer id) {
        final StudentDTO retrievedStudent = studentService.findStudentById(id);
        return ResponseEntity.ok(retrievedStudent);
    }

    @PutMapping(value = "/student", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> updateStudent(@Valid @RequestBody StudentDTO studentDTO) {
        final StudentDTO updatedStudent = studentService.updateStudentById(studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping(value = "/student/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> deleteStudentById(
            @Min(value = 1, message = "Mentor ID must be a positive integer")
            @PathVariable Integer id) {
        final StudentDTO deletedStudent = studentService.deleteStudentById(id);
        return ResponseEntity.ok(deletedStudent);
    }
}
