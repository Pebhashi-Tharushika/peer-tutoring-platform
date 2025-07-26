package com.mbpt.skillmentor.root.repository;

import com.mbpt.skillmentor.root.entity.MentorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<MentorEntity, Integer> {

    /**
     * Finds a mentor by their clerk mentor ID.
     *
     * @param clerkMentorId the generated ID for the mentor by Clerk
     * @return an Optional containing the MentorEntity if found, otherwise empty
     */
    Optional<MentorEntity> findByClerkMentorId(String clerkMentorId);
}
