package com.mbpt.skillmentor.root.service.impl;

import com.mbpt.skillmentor.root.dto.StudentDTO;
import com.mbpt.skillmentor.root.entity.StudentEntity;
import com.mbpt.skillmentor.root.exception.StudentException;
import com.mbpt.skillmentor.root.mapper.StudentEntityDTOMapper;
import com.mbpt.skillmentor.root.repository.StudentRepository;
import com.mbpt.skillmentor.root.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
//    @CacheEvict(value = {"studentCache", "allStudentsCache"}, allEntries = true)
    public StudentDTO createStudent(final StudentDTO studentDTO) {
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
    @Transactional(rollbackFor = Exception.class)
//    @Cacheable(
//            value = "allStudentsCache",
//            key = "'getAllStudents:' + #address + ':' + #age + ':' + #firstNames"
//    )
    public List<StudentDTO> getAllStudents(final List<String> address, final List<Integer> age, final List<String> firstNames) {
        log.info("Fetching all students with filters: addresses={}, ages={}, firstNames={}", address, age, firstNames);
        final List<StudentEntity> studentEntities = studentRepository.findAll();
        List<StudentDTO> studentDTOList = studentEntities
                .stream()
                .filter(student -> address == null || address.contains(student.getAddress()))
                .filter(student -> age == null || age.contains(student.getAge()))
                .filter(student -> firstNames == null || firstNames.contains(student.getFirstName()))
                .map(StudentEntityDTOMapper::map)
                .toList();
        log.info("Found {} students after filtering from data-source: {}", studentDTOList.size(), this.datasource);
        return studentDTOList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
//    @Cacheable(value = "studentCache", key = "#id")
    public StudentDTO findStudentById(final Integer id) {
        log.info("Fetching student by ID: {}", id);
        return studentRepository.findById(id)
                .map(student -> {
                    log.info("Student found: {}", student);
                    return StudentEntityDTOMapper.map(student);
                })
                .orElseThrow(() -> {
                    log.error("Student not found with ID: {} from data-source:{}", id, this.datasource);
                    return new StudentException("Student not found with ID: " + id);
                });
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
//    @Cacheable(value = "studentCache", key = "#id")
    public StudentDTO findStudentByClerkId(String clerkId) {
        log.info("Fetching student by Clerk ID: {}", clerkId);
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
//    @CachePut(value = "studentCache", key = "#studentDTO.studentId")
//    @CacheEvict(value = "allStudentsCache", allEntries = true)
    public StudentDTO updateStudentById(final StudentDTO studentDTO) {
        log.info("Updating student...");
        if (studentDTO == null || studentDTO.getStudentId() == null) {
            log.error("Failed to update student: DTO or studentId is null.");
            throw new IllegalArgumentException("Student ID must not be null for update.");
        }
        log.debug("Updating student with ID: {}", studentDTO.getStudentId());
        final StudentEntity selectedStudentEntity = studentRepository.findById(studentDTO.getStudentId())
                .orElseThrow(() -> {
                    log.error("Cannot update. Student not found with ID: {}", studentDTO.getStudentId());
                    return new StudentException("Cannot update. Student not found with ID: " + studentDTO.getStudentId());
                });
        selectedStudentEntity.setFirstName(studentDTO.getFirstName());
        selectedStudentEntity.setLastName(studentDTO.getLastName());
        selectedStudentEntity.setEmail(studentDTO.getEmail());
        selectedStudentEntity.setAddress(studentDTO.getAddress());
        selectedStudentEntity.setPhoneNumber(studentDTO.getPhoneNumber());
        selectedStudentEntity.setAge(studentDTO.getAge());

        StudentEntity updated = studentRepository.save(selectedStudentEntity);
        log.info("Student updated with ID: {}", updated.getStudentId());
        return StudentEntityDTOMapper.map(updated);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @CacheEvict(value = {"studentCache", "allStudentsCache"}, allEntries = true)
    public StudentDTO deleteStudentById(final Integer id) {
        log.info("Deleting student with ID: {}", id);
        final StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot delete. Student not found with ID: {}", id);
                    return new StudentException("Cannot delete. Student not found with ID: " + id);
                });
        studentRepository.delete(studentEntity);
        log.info("Student with ID {} deleted successfully", id);
        return StudentEntityDTOMapper.map(studentEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @CacheEvict(value = {"studentCache", "allStudentsCache"}, allEntries = true)
    public StudentDTO deleteStudentByClerkId(String clerkId) {
        log.info("Deleting student with Clerk ID: {}", clerkId);
        final StudentEntity studentEntity = studentRepository.findByClerkStudentId(clerkId)
                .orElseThrow(() -> {
                    log.error("Cannot delete. Student not found with Clerk ID: {}", clerkId);
                    return new StudentException("Cannot delete. Student not found with Clerk ID: " + clerkId);
                });
        studentRepository.delete(studentEntity);
        log.info("Student with Clerk ID {} deleted successfully", clerkId);
        return StudentEntityDTOMapper.map(studentEntity);
    }
}
