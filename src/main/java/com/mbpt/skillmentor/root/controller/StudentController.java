package com.mbpt.skillmentor.root.controller;

import com.mbpt.skillmentor.root.common.Constants;
import com.mbpt.skillmentor.root.dto.StudentDTO;
import com.mbpt.skillmentor.root.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/academic")
@Tag(name = "Student Management", description = "APIs for managing students")
public class StudentController {

    @Autowired
    StudentService studentService;


    @Operation(summary = "Create a new student", description = "Accepts a student JSON and creates a new student record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/student", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> createStudent(
            @Parameter(description = "Student details to create", required = true)
            @Valid @RequestBody StudentDTO studentDTO) {
        final StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.ok(createdStudent);
    }


    @Operation(summary = "Get all students", description = "Retrieves all students with optional filters for address, age, and first name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No students found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/student", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<StudentDTO>> getAllStudents(
            @Parameter(description = "Filter by address") @RequestParam(required = false) List<String> address,
            @Parameter(description = "Filter by age") @RequestParam(required = false) List<Integer> age,
            @Parameter(description = "Filter by first name") @RequestParam(required = false) List<String> firstNames) {
        final List<StudentDTO> studentsList = studentService.getAllStudents(address, age, firstNames);
        return ResponseEntity.ok(studentsList);
    }


    @Operation(summary = "Get student by ID", description = "Fetches a student by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/student/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> getStudentById(
            @Parameter(description = "ID of the student to fetch", required = true)
            @Min(value = 1, message = "Student ID must be a positive integer") @PathVariable Integer id) {
        final StudentDTO retrievedStudent = studentService.findStudentById(id);
        return ResponseEntity.ok(retrievedStudent);
    }


    @Operation(summary = "Update a student", description = "Updates an existing student based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student data"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/student", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> updateStudent(
            @Parameter(description = "Student data to update", required = true)
            @Valid @RequestBody StudentDTO studentDTO) {
        final StudentDTO updatedStudent = studentService.updateStudentById(studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }


    @Operation(summary = "Delete a student", description = "Deletes a student by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(value = "/student/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> deleteStudentById(
            @Parameter(description = "ID of the student to delete", required = true)
            @Min(value = 1, message = "Mentor ID must be a positive integer") @PathVariable Integer id) {
        final StudentDTO deletedStudent = studentService.deleteStudentById(id);
        return ResponseEntity.ok(deletedStudent);
    }
}
