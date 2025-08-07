package com.mbpt.peertutoringplatform.controller;

import com.mbpt.peertutoringplatform.common.Constants;
import com.mbpt.peertutoringplatform.dto.ClassRoomDTO;
import com.mbpt.peertutoringplatform.service.ClassRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequestMapping("/academic")
@Tag(name = "Classroom Management", description = "Endpoints for managing classrooms and their relationships")
public class ClassRoomController {


    private final ClassRoomService classRoomService;

    public ClassRoomController(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }


    @Operation(summary = "Create a new classroom", description = "Accepts a Classroom JSON and creates a new classroom record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid classroom data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @PostMapping(value = "/classroom", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassRoomDTO> createClassRoom(
            @RequestParam("title") String title,
            @RequestParam("class_image") MultipartFile classImage
    ) {
        System.out.println(title);
        System.out.println(classImage.getOriginalFilename());
        System.out.println(classImage);
        ClassRoomDTO createdClassRoom = classRoomService.createClassRoom(title, classImage);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdClassRoom);
    }


    @Operation(summary = "Get all classrooms", description = "Fetches a list of all classrooms with associated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom list retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No classrooms found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @GetMapping(value = "/classroom", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClassRoomDTO>> getAllClassRooms() {
        final List<ClassRoomDTO> allClassRooms = classRoomService.getAllClassRooms();
        return ResponseEntity.status(HttpStatus.OK).body(allClassRooms);
    }


    @Operation(
            summary = "Search classrooms by ID or title",
            description = "Fetches a list of classrooms that match the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom/Classrooms retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid classroom data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "No classrooms found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @PreAuthorize(Constants.STUDENT_ROLE_PERMISSION)
    @GetMapping(value = "/classroom/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassRoomDTO> getClassRoomById(
            @Parameter(description = "ID of the classroom to retrieve", required = true)
            @Min(value = 1, message = "Classroom ID must be positive value")
            @PathVariable @NotNull(message = "classroom ID must not be null") Integer id
    ) {
        ClassRoomDTO classroom = classRoomService.findClassRoomById(id);
        return ResponseEntity.status(HttpStatus.OK).body(classroom);
    }


    @Operation(summary = "Update classroom", description = "Updates an existing classroom's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid classroom data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Classroom not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @PutMapping(value = "/classroom", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassRoomDTO> updateClassRoom(
            @Parameter(description = "Classroom details to update", required = true)
            @Valid @RequestBody ClassRoomDTO classRoomDTO) {
        ClassRoomDTO updatedClassRoom = classRoomService.updateClassRoomById(classRoomDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedClassRoom);
    }


    @Operation(summary = "Delete classroom by ID", description = "Deletes a classroom and its associated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid classroom ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Classroom not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @DeleteMapping(value = "/classroom/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassRoomDTO> deleteClassRoom(
            @Parameter(description = "ID of the classroom to delete", required = true)
            @Min(value = 1, message = "Classroom ID must be positive")
            @PathVariable Integer id) {
        ClassRoomDTO deletedClassRoom = classRoomService.deleteClassRoomById(id);
        return ResponseEntity.status(HttpStatus.OK).body(deletedClassRoom);
    }
}
