package com.mbpt.skillmentor.root.service;

import com.mbpt.skillmentor.root.dto.MentorDTO;
import com.mbpt.skillmentor.root.exception.MentorException;

import java.util.List;

/**
 * Service Interface for managing mentors.
 * Provides operations to create, retrieve, modify mentor records.
 */

public interface MentorService {

    /**
     * Create a new mentor records.
     *
     * @param mentorDTO the data transfer object containing mentor details
     * @return the created {@link MentorDTO } with generated mentor ID
     */
    MentorDTO createMentor(MentorDTO mentorDTO) throws MentorException;


    /**
     * Retrieves all mentors, optionally filtered by age.
     *
     * @param firstNames first name of the mentors to retrieve
     * @param subjects   subject of the mentors to retrieve
     * @return a list of MentorDTO objects representing the mentors
     */
    List<MentorDTO> getAllMentors(List<String> firstNames, List<String> subjects);


    /**
     * Retrieves a mentor by mentor ID.
     *
     * @param id the ID of the mentor to retrieve
     * @return a MentorDTO object representing the mentor
     */
    MentorDTO findMentorById(Integer id) throws MentorException;


    /**
     * Updates an existing mentor's details.
     *
     * @param mentorDTO the data transfer object containing updated mentor details
     * @return a MentorDTO object representing the updated mentor
     */
    MentorDTO updateMentorById(MentorDTO mentorDTO) throws MentorException;


    /**
     * Deletes a mentor by their ID.
     *
     * @param id the ID of the mentor to delete
     * @return a MentorDTO object representing the deleted mentor
     */
    MentorDTO deleteMentorById(Integer id) throws MentorException;

}
