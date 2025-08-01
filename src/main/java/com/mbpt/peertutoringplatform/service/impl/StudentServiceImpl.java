package com.mbpt.peertutoringplatform.service.impl;

import com.mbpt.peertutoringplatform.dto.StudentDTO;
import com.mbpt.peertutoringplatform.entity.StudentEntity;
import com.mbpt.peertutoringplatform.exception.StudentException;
import com.mbpt.peertutoringplatform.mapper.StudentEntityDTOMapper;
import com.mbpt.peertutoringplatform.repository.StudentRepository;
import com.mbpt.peertutoringplatform.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Value("${spring.datasource.url}")
    private String datasource;

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating new student...");

        if (studentDTO == null) {
            log.error("Failed to create student: input DTO is null.");
            throw new IllegalArgumentException("Student data must not be null.");
        }

        log.debug("StudentDTO received: {}", studentDTO);

        // First check if student already exists by clerk ID
        try {
            Optional<StudentEntity> existingStudent = studentRepository.findByClerkStudentId(studentDTO.getClerkStudentId());
            if (existingStudent.isPresent()) {
                log.info("Student already exists with clerk ID: {}", studentDTO.getClerkStudentId());
                return StudentEntityDTOMapper.map(existingStudent.get());
            }

            final StudentEntity studentEntity = StudentEntityDTOMapper.map(studentDTO);
            final StudentEntity savedEntity = studentRepository.save(studentEntity);
            log.info("Student created with ID: {} at data-source: {}", savedEntity.getStudentId(), this.datasource);
            return StudentEntityDTOMapper.map(savedEntity);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while creating student: {}", e.getMessage());
            // Retry finding the student in case it was created concurrently
            return studentRepository.findByClerkStudentId(studentDTO.getClerkStudentId())
                    .map(StudentEntityDTOMapper::map)
                    .orElseThrow(() -> new StudentException("Failed to create student due to data integrity violation"));
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        log.info("Fetching all students...");
        List<StudentEntity> studentEntities = studentRepository.findAll();

        List<StudentDTO> studentDTOList = studentEntities.stream()
                .map(StudentEntityDTOMapper::map)
                .collect(Collectors.toList());

        log.info("Found {} students from data-source: {}", studentDTOList.size(), this.datasource);
        return studentDTOList;
    }


    @Override
    @Transactional(readOnly = true)
    public StudentDTO findStudentByClerkId(String clerkId) {
        log.info("Fetching student by Clerk ID: {} ...", clerkId);
        return studentRepository.findByClerkStudentId(clerkId)
                .map(student -> {
                    log.info("Student found: {}", student);
                    return StudentEntityDTOMapper.map(student);
                })
                .orElseThrow(() -> {
                    log.error("Student not found with Clerk ID: {} from data-source:{}", clerkId, this.datasource);
                    return new StudentException("Student not found with Clerk ID: " + clerkId);
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentDTO updateStudentById(final StudentDTO studentDTO) {
        log.info("Updating student...");
        if (studentDTO == null || studentDTO.getStudentId() == null) {
            log.error("Failed to update student: DTO or student's ID is null.");
            throw new IllegalArgumentException("Student ID must not be null for update.");
        }
        log.debug("Updating student with ID: {}", studentDTO.getStudentId());
        StudentEntity selectedStudentEntity = studentRepository.findById(studentDTO.getStudentId())
                .orElseThrow(() -> {
                    log.error("Failed to update student: Student not found with ID: {}", studentDTO.getStudentId());
                    return new StudentException("Student not found with ID: " + studentDTO.getStudentId());
                });
        selectedStudentEntity.setFirstName(studentDTO.getFirstName());
        selectedStudentEntity.setLastName(studentDTO.getLastName());
        selectedStudentEntity.setEmail(studentDTO.getEmail());
        selectedStudentEntity.setAddress(studentDTO.getAddress());
        selectedStudentEntity.setPhoneNumber(studentDTO.getPhoneNumber());
        selectedStudentEntity.setAge(studentDTO.getAge());

        StudentEntity updated = studentRepository.save(selectedStudentEntity);
        log.info("Updated student with ID: {}", updated.getStudentId());
        return StudentEntityDTOMapper.map(updated);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentDTO deleteStudentByClerkId(String clerkId) {
        log.info("Deleting student with Clerk ID: {} ...", clerkId);
        StudentEntity studentEntity = studentRepository.findByClerkStudentId(clerkId)
                .orElseThrow(() -> {
                    log.error("Failed to delete student. Student not found with Clerk ID: {}", clerkId);
                    return new StudentException("Failed to delete student. Student not found with Clerk ID: " + clerkId);
                });
        studentRepository.delete(studentEntity);
        log.info("Deleted student with Clerk ID: {} ", clerkId);
        return StudentEntityDTOMapper.map(studentEntity);
    }
}
