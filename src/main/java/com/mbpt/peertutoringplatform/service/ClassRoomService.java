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
     * @param title the data transfer object containing classroom details
     * @param  classImage image of class
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
     * @param classRoomDTO the data transfer object containing updated classroom details
     * @return the {@link ClassRoomDTO}  object representing the updated classroom
     */
    ClassRoomDTO updateClassRoomById(ClassRoomDTO classRoomDTO);


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
     * Retrieves a list of classrooms based on a set of optional filters.
     * If all parameters are null, all classrooms will be returned.
     *
     * @param title      the official name of the classroom (optional)
     * @param mentorName the name of the mentor who conducts the classroom's session (optional, supports partial match)
     * @param minCount   the minimum number of students enrolled for the classroom (optional, used as part of a range)
     * @param maxCount   the maximum number of students enrolled for the classroom (optional, used as part of a range)
     * @return a list of {@link ClassRoomDTO} objects that match the provided filter criteria
     */
    List<ClassRoomDTO> findClassRoomsByFilters(String title, String mentorName, Integer minCount, Integer maxCount);
}
