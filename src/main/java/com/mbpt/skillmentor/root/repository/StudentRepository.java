package com.mbpt.skillmentor.root.repository;

import com.mbpt.skillmentor.root.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {

    /**
     * Finds a student by their clerk student ID.
     *
     * @param clerkStudentId the generated ID for the student by Clerk
     * @return an Optional containing the StudentEntity if found, otherwise empty
     */
    Optional<StudentEntity> findByClerkStudentId(String clerkStudentId);

}
