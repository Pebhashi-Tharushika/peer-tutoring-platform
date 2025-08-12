package com.mbpt.peertutoringplatform.service;

import com.mbpt.peertutoringplatform.dto.ClassRoomDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service Interface for managing classrooms.
 * Provides operations to create, retrieve, modify classroom details.
 */

public interface ClassRoomService {
    /**
     * Create a new classroom records.
     *
     * @param title      the title of the classroom
     * @param classImage the image of the classroom
     * @return the created {@link ClassRoomDTO } with generated classroom ID
     */
    ClassRoomDTO createClassRoom(String title, MultipartFile classImage);


    /**
     * Retrieves all classrooms.
     *
     * @return a list of all {@link ClassRoomDTO} objects representing the classrooms
     */
    List<ClassRoomDTO> getAllClassRooms();


    /**
     * Updates an existing classroom's details.
     *
     * @param id         the ID of the classroom to delete
     * @param title      the new title of the classroom
     * @param classImage the new classroom image
     * @param imageUrl   the url of the new classroom image
     * @return the {@link ClassRoomDTO}  object representing the updated classroom
     */
    ClassRoomDTO updateClassRoomById(Integer id, String title, MultipartFile classImage, String imageUrl);


    /**
     * Deletes a classroom by their ID.
     *
     * @param id the ID of the classroom to delete
     * @return the {@link ClassRoomDTO} object representing the deleted classroom
     */
    ClassRoomDTO deleteClassRoomById(Integer id);


    /**
     * Retrieves a classroom by its unique ID.
     *
     * @param id the unique identifier of the classroom
     * @return a {@link ClassRoomDTO} object matching the provided ID
     */
    ClassRoomDTO findClassRoomById(Integer id);

    /**
     * Retrieves all classrooms that have not been assigned a mentor yet.
     * A classroom is considered unassigned if its mentor entity is {@code null}.
     *
     * @return a list of {@link ClassRoomDTO} objects representing classrooms without an assigned mentor
     */
    List<ClassRoomDTO> getClassroomsWithoutMentor();
}
