package com.mbpt.peertutoringplatform.controller;

import com.mbpt.peertutoringplatform.common.Constants;
import com.mbpt.peertutoringplatform.dto.MentorDTO;
import com.mbpt.peertutoringplatform.dto.MentorProfileDTO;
import com.mbpt.peertutoringplatform.service.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/academic")
@Tag(name = "Mentor Management", description = "Endpoints for managing mentors and their related data")
public class MentorController {

    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }


    @Operation(summary = "Create a new mentor", description = "Creates a new mentor profile along with classroom associations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid mentor data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @PostMapping(value = "/mentors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMentor(
            @Parameter(description = "Mentor details to create", required = true)
            @Valid @RequestBody MentorDTO mentorDTO) {
        MentorDTO savedDTO = mentorService.createMentor(mentorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
    }

    @Operation(summary = "Get details of all mentors", description = "Fetches a list of all registered mentors with associated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentors retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No mentors found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @GetMapping(value = "/mentors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MentorDTO>> getAllMentors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String classroom,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) Boolean isVerified
    ) {
        final List<MentorDTO> allMentors = mentorService.getAllMentors(name, classroom, profession, isVerified);
        return ResponseEntity.status(HttpStatus.OK).body(allMentors);
    }


    @Operation(summary = "Get mentor by Clerk ID", description = "Retrieves a single mentor using their unique Clerk ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid mentor Clerk ID"),
            @ApiResponse(responseCode = "404", description = "Mentor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @GetMapping(value = "/mentors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MentorDTO> getMentorByClerkId(
            @Parameter(description = "Clerk ID of the mentor to retrieve", required = true)
            @PathVariable @NotBlank(message = "Mentor Clerk ID must not be blank") String id) {
        MentorDTO mentor = mentorService.findMentorByClerkId(id);
        return ResponseEntity.status(HttpStatus.OK).body(mentor);
    }



    @Operation(summary = "Retrieve mentor profile by mentor ID", description = "Retrieve mentor profile details and its associated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor Profile retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid mentor ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Mentor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @GetMapping("/mentors/profile/{id}")
    public ResponseEntity<MentorProfileDTO> getMentorProfile(@PathVariable Integer id) {
        MentorProfileDTO mentorProfile = mentorService.getMentorProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(mentorProfile);
    }


}
