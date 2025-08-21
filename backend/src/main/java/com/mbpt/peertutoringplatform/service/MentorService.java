package com.mbpt.peertutoringplatform.service;

import com.mbpt.peertutoringplatform.dto.LiteMentorDTO;
import com.mbpt.peertutoringplatform.dto.MentorDTO;
import com.mbpt.peertutoringplatform.dto.MentorProfileDTO;
import org.springframework.web.multipart.MultipartFile;

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
     * @param image     the image of the mentor
     * @return the created {@link MentorDTO } with generated mentor ID
     */
    MentorDTO createMentor(LiteMentorDTO mentorDTO, MultipartFile image);


    /**
     * Retrieves a list of mentors
     *
     * @return a list of {@link MentorDTO} objects that match the given filters
     */
    List<MentorDTO> getAllMentors();

    MentorProfileDTO getMentorProfile(Integer id);
}
