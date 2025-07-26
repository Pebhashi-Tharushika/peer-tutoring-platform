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
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/academic")
@Tag(name = "Student Management", description = "APIs for managing students")
public class StudentController {


    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

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
        log.info("Create Student......");
        return ResponseEntity.ok(createdStudent);
    }


    @Operation(summary = "Get all students", description = "Retrieves all students with optional filters for address, age, and first name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No students found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @GetMapping(value = "/student", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<StudentDTO>> getAllStudents(
            @Parameter(description = "Filter by address") @RequestParam(required = false) List<String> address,
            @Parameter(description = "Filter by age") @RequestParam(required = false) List<Integer> age,
            @Parameter(description = "Filter by first name") @RequestParam(required = false) List<String> firstNames) {
        final List<StudentDTO> studentsList = studentService.getAllStudents(address, age, firstNames);
        log.info("Get All Students......");
        return ResponseEntity.ok(studentsList);
    }


    @Operation(summary = "Get student by Clerk ID", description = "Fetches a student by their unique Clerk ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student Clerk ID"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @GetMapping(value = "/student/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> getStudentByClerkId(
            @Parameter(description = "Student Clerk ID of the student to fetch", required = true)
            @NotBlank(message = "Student Clerk ID must not be blank") @PathVariable String id) {
        final StudentDTO retrievedStudent = studentService.findStudentByClerkId(id);
        log.info("Find Student id:" + id + "from server......");
        return ResponseEntity.ok(retrievedStudent);
    }


    @Operation(summary = "Update a student", description = "Updates an existing student based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student data"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @PutMapping(value = "/student", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> updateStudent(
            @Parameter(description = "Student data to update", required = true)
            @Valid @RequestBody StudentDTO studentDTO) {
        final StudentDTO updatedStudent = studentService.updateStudentById(studentDTO);
        log.info("Update details of student with id:" + studentDTO.getStudentId());
        return ResponseEntity.ok(updatedStudent);
    }


    @Operation(summary = "Delete a student", description = "Deletes a student by their Clerk ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student Clerk ID"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @DeleteMapping(value = "/student/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<StudentDTO> deleteByStudentClerkId(
            @Parameter(description = "Student Clerk ID of the student to delete", required = true)
            @NotBlank(message = "Student Clerk ID must not be blank") @PathVariable String id) {
        final StudentDTO deletedStudent = studentService.deleteStudentByClerkId(id);
        log.info("Delete Student with Clerk id:" + id + "from server......");
        return ResponseEntity.ok(deletedStudent);
    }
}