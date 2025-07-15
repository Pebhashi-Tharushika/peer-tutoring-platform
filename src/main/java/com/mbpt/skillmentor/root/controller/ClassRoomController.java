package com.mbpt.skillmentor.root.controller;

import com.mbpt.skillmentor.root.common.Constants;
import com.mbpt.skillmentor.root.dto.ClassRoomDTO;
import com.mbpt.skillmentor.root.service.ClassRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Create a new classroom", description = "Creates a new classroom along with its mentor(s)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @PostMapping(value = "/classroom", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ClassRoomDTO> createClassRoom(
            @Parameter(description = "Classroom details to create", required = true)
            @Valid @RequestBody ClassRoomDTO classRoomDTO) {
        final ClassRoomDTO createdClassRoom = classRoomService.createClassRoom(classRoomDTO);
        return ResponseEntity.ok(createdClassRoom);
    }


    @Operation(summary = "Get all classrooms", description = "Fetches a list of all classrooms with associated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom list retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No classrooms found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @GetMapping(value = "/classroom", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<ClassRoomDTO>> getAllClassRooms() {
        final List<ClassRoomDTO> allClassRooms = classRoomService.getAllClassRooms();
        return ResponseEntity.ok(allClassRooms);
    }


    @Operation(summary = "Get classroom by ID", description = "Fetches a classroom using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid ID"),
            @ApiResponse(responseCode = "404", description = "Classroom not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @GetMapping(value = "/classroom/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ClassRoomDTO> getClassRoomById(
            @Parameter(description = "ID of the classroom to retrieve", required = true)
            @Min(value = 1, message = "Classroom ID must be positive")
            @PathVariable Integer id) {
        final ClassRoomDTO classRoomDTO = classRoomService.findClassRoomById(id);
        return ResponseEntity.ok(classRoomDTO);
    }


    @Operation(summary = "Update classroom", description = "Updates an existing classroom's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "404", description = "Classroom not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @PutMapping(value = "/classroom", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ClassRoomDTO> updateClassRoom(
            @Parameter(description = "Classroom details to update", required = true)
            @Valid @RequestBody ClassRoomDTO classRoomDTO) {
        final ClassRoomDTO updatedClassRoom = classRoomService.updateClassRoomById(classRoomDTO);
        return ResponseEntity.ok(updatedClassRoom);
    }


    @Operation(summary = "Delete classroom by ID", description = "Deletes a classroom and its associated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid ID"),
            @ApiResponse(responseCode = "404", description = "Classroom not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize(Constants.ADMIN_ROLE_PERMISSION)
    @DeleteMapping(value = "/classroom/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ClassRoomDTO> deleteClassRoom(
            @Parameter(description = "ID of the classroom to delete", required = true)
            @Min(value = 1, message = "Classroom ID must be positive")
            @PathVariable Integer id) {
        final ClassRoomDTO deletedClassRoom = classRoomService.deleteClassRoomById(id);
        return ResponseEntity.ok(deletedClassRoom);
    }
}
