package com.mbpt.skillmentor.root.service;

import com.mbpt.skillmentor.root.dto.ClassRoomDTO;

import java.util.List;

/**
 * Service Interface for managing classrooms.
 * Provides operations to create, retrieve, modify classroom details.
 */

public interface ClassRoomService {
     /**
     * Create a new classroom records.
     *
     * @param classRoomDTO the data transfer object containing classroom details
     * @return the created {@link ClassRoomDTO } with generated classroom ID
     */
    ClassRoomDTO createClassRoom(ClassRoomDTO classRoomDTO);


    /**
     * Retrieves all classrooms, optionally filtered by age.
     *
     * @return a list of ClassRoomDTO objects representing the classrooms
     */
    List<ClassRoomDTO> getAllClassRooms();


    /**
     * Retrieves a classroom by classroom ID.
     *
     * @param id the ID of the classroom to retrieve
     * @return a ClassRoomDTO object representing the classroom
     */
    ClassRoomDTO findClassRoomById(Integer id);


    /**
     * Updates an existing classroom's details.
     *
     * @param classRoomDTO the data transfer object containing updated classroom details
     * @return a ClassRoomDTO object representing the updated classroom
     */
    ClassRoomDTO updateClassRoomById(ClassRoomDTO classRoomDTO);


    /**
     * Deletes a classroom by their ID.
     *
     * @param id the ID of the classroom to delete
     * @return a ClassRoomDTO object representing the deleted classroom
     */
    ClassRoomDTO deleteClassRoomById(Integer id);

}
