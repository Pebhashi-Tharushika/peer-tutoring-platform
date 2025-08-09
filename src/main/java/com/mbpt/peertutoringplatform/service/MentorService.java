package com.mbpt.peertutoringplatform.service;

import com.mbpt.peertutoringplatform.dto.MentorDTO;
import com.mbpt.peertutoringplatform.dto.MentorProfileDTO;

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
    MentorDTO createMentor(MentorDTO mentorDTO);


    /**
     * Retrieves a list of mentors filtered by optional criteria such as name, classroom title, profession and
     * verification status.
     *
     * @param name       the name (or partial name) of the mentor to search for (optional)
     * @param classroom  the title of the classroom conducted by the mentor (optional)
     * @param profession the profession of the mentor (optional)
     * @param isVerified whether the mentor is certified (optional)
     * @return a list of {@link MentorDTO} objects that match the given filters
     */
    List<MentorDTO> getAllMentors(String name, String classroom, String profession, Boolean isVerified);


    /**
     * Retrieves a mentor by mentor Clerk ID.
     *
     * @param clerkId the ID generated for the mentor by Clerk, to retrieve
     * @return a {@link MentorDTO } object representing the mentor
     */
    MentorDTO findMentorByClerkId(String clerkId);


    MentorProfileDTO getMentorProfile(Integer id);
}
