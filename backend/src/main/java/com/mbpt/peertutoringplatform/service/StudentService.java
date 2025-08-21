package com.mbpt.peertutoringplatform.service;

import com.mbpt.peertutoringplatform.dto.StudentDTO;

import java.util.List;

/**
 * Service Interface for managing students.
 * Provides operations to create, retrieve, modify student records.
 */

public interface StudentService {

    /**
     * Create a new student records.
     *
     * @param studentDTO the data transfer object containing student details
     * @return the created {@link StudentDTO } with generated student ID
     */
    StudentDTO createStudent(StudentDTO studentDTO);


    /**
     * Retrieves all students, optionally filtered by age.
     *
     * @return a list of StudentDTO objects representing the students
     */
    List<StudentDTO> getAllStudents();

    /**
     * Retrieves a student by Clerk ID.
     *
     * @param clerkId the ID generated for the student by the clerk, to retrieve
     * @return a StudentDTO object representing the student
     */
    StudentDTO findStudentByClerkId(String clerkId);

}
