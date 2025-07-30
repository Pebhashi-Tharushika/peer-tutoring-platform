package com.mbpt.peertutoringplatform.controller;

import com.mbpt.peertutoringplatform.exception.MentorException;
import com.mbpt.peertutoringplatform.common.Constants;
import com.mbpt.peertutoringplatform.dto.MentorDTO;
import com.mbpt.peertutoringplatform.service.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(value = "/academic")
@Tag(name = "Mentor Management", description = "Endpoints for managing mentors and their related data")
public class MentorController {


    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }


    @Operation(summary = "Create a new mentor", description = "Creates a mentor along with subject and classroom associations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid mentor data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @PostMapping(value = "/mentor", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<?> createMentor(
            @Parameter(description = "Mentor details to be created", required = true)
            @Valid @RequestBody MentorDTO mentorDTO) {
        final MentorDTO savedDTO = mentorService.createMentor(mentorDTO);
        return ResponseEntity.ok(savedDTO);
    }


    @Operation(summary = "Get mentor by Clerk ID", description = "Retrieves a single mentor using their unique Clerk ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid mentor Clerk ID"),
            @ApiResponse(responseCode = "404", description = "Mentor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @GetMapping(value = "/mentor/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<?> getMentorByClerkId(
            @Parameter(description = "Clerk ID of the mentor to retrieve", required = true)
            @PathVariable @NotBlank(message = "Mentor Clerk ID must not be blank") String id) {
        final MentorDTO mentor;
        try {
            mentor = mentorService.findMentorByClerkId(id);
        } catch (MentorException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(mentor);
    }


    @Operation(summary = "Update a mentor", description = "Updates an existing mentor's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid mentor data"),
            @ApiResponse(responseCode = "404", description = "Mentor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @PutMapping(value = "/mentor", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<?> updateMentor(
            @Parameter(description = "Mentor details to update", required = true)
            @Valid @RequestBody MentorDTO mentorDTO) {
        final MentorDTO mentor;
        try {
            mentor = mentorService.updateMentorById(mentorDTO);
        } catch (MentorException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(mentor);
    }


    @Operation(summary = "Delete mentor by Clerk ID", description = "Deletes a mentor by their unique Clerk ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid mentor Clerk ID"),
            @ApiResponse(responseCode = "404", description = "Mentor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @DeleteMapping(value = "/mentor/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<?> deleteMentorByClerkId(
            @Parameter(description = "Clerk ID of the mentor to delete", required = true)
            @NotBlank(message = "Mentor Clerk ID must not be blank") @PathVariable String id) {
        final MentorDTO mentor;
        try {
            mentor = mentorService.deleteMentorByClerkId(id);
        } catch (MentorException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(mentor);
    }
}
