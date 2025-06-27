package com.mbpt.skillmentor.root.controller;

import com.mbpt.skillmentor.root.common.Constants;
import com.mbpt.skillmentor.root.dto.ClassRoomDTO;
import com.mbpt.skillmentor.root.service.ClassRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/academic")
public class ClassRoomController {

    @Autowired
    private ClassRoomService classRoomService;

    public ClassRoomController() {
    }

    @PostMapping(value = "/classroom", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ClassRoomDTO> createClassRoom(@Valid @RequestBody ClassRoomDTO classRoomDTO) {
        final ClassRoomDTO createdClassRoom = classRoomService.createClassRoom(classRoomDTO);
        return ResponseEntity.ok(createdClassRoom);
    }

    @GetMapping(value = "/classroom", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<List<ClassRoomDTO>> getAllClassRooms() {
        final List<ClassRoomDTO> allClassRooms = classRoomService.getAllClassRooms();
        return ResponseEntity.ok(allClassRooms);
    }

    @GetMapping(value = "/classroom/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ClassRoomDTO> getClassRoomById(
            @Min(value = 1, message = "Classroom ID must be positive")
            @PathVariable Integer id) {
        final ClassRoomDTO classRoomDTO = classRoomService.findClassRoomById(id);
        return ResponseEntity.ok(classRoomDTO);
    }

    @PutMapping(value = "/classroom", consumes = Constants.APPLICATION_JSON, produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ClassRoomDTO> updateClassRoom(@Valid @RequestBody ClassRoomDTO classRoomDTO) {
        final ClassRoomDTO updatedClassRoom = classRoomService.updateClassRoomById(classRoomDTO);
        return ResponseEntity.ok(updatedClassRoom);
    }

    @DeleteMapping(value = "/classroom/{id}", produces = Constants.APPLICATION_JSON)
    public ResponseEntity<ClassRoomDTO> deleteClassRoom(
            @Min(value = 1, message = "Classroom ID must be positive")
            @PathVariable Integer id) {
        final ClassRoomDTO deletedClassRoom = classRoomService.deleteClassRoomById(id);
        return ResponseEntity.ok(deletedClassRoom);
    }
}
