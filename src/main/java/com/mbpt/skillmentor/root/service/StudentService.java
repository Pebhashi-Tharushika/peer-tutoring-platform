package com.mbpt.skillmentor.root.service;

import com.mbpt.skillmentor.root.dto.StudentDTO;

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
     * @param address the address to filter students by (optional)
     * @param age     the age to filter students by (optional)
     * @return a list of StudentDTO objects representing the students
     */
    List<StudentDTO> getAllStudents(List<String> address, List<Integer> age, List<String> firstNames);


    /**
     * Retrieves a student by student ID.
     *
     * @param id the ID of the student to retrieve
     * @return a StudentDTO object representing the student
     */
    StudentDTO findStudentById(Integer id);

    /**
     * Retrieves a student by Clerk ID.
     *
     * @param clerkId the ID generated for the student by the clerk, to retrieve
     * @return a StudentDTO object representing the student
     */
    StudentDTO findStudentByClerkId(String clerkId);

    /**
     * Updates an existing student's details.
     *
     * @param studentDTO the data transfer object containing updated student details
     * @return a StudentDTO object representing the updated student
     */
    StudentDTO updateStudentById(StudentDTO studentDTO);


    /**
     * Deletes a student by their ID.
     *
     * @param id the ID of the student to delete
     * @return a StudentDTO object representing the deleted student
     */
    StudentDTO deleteStudentById(Integer id);


    /**
     * Delete a student by their Clerk ID
     *
     * @param clerkId the ID generated for the student by the clerk, to retrieve
     * @return a StudentDTO object representing the deleted student
     */
    StudentDTO deleteStudentByClerkId(String clerkId);

}
