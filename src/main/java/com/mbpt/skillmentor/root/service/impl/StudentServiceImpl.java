package com.mbpt.skillmentor.root.service.impl;

import com.mbpt.skillmentor.root.dto.StudentDTO;
import com.mbpt.skillmentor.root.entity.StudentEntity;
import com.mbpt.skillmentor.root.exception.StudentException;
import com.mbpt.skillmentor.root.mapper.StudentEntityDTOMapper;
import com.mbpt.skillmentor.root.repository.StudentRepository;
import com.mbpt.skillmentor.root.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"studentCache", "allStudentsCache"}, allEntries = true)
    public StudentDTO createStudent(final StudentDTO studentDTO) {
        log.info("Creating new student...");
        if (studentDTO == null) {
            log.error("Failed to create student: input DTO is null.");
            throw new IllegalArgumentException("Student data must not be null.");
        }
        log.debug("StudentDTO received: {}", studentDTO);
        final StudentEntity studentEntity = StudentEntityDTOMapper.map(studentDTO);
        final StudentEntity savedEntity = studentRepository.save(studentEntity);
        log.info("Student created with ID: {}", savedEntity.getStudentId());
        return StudentEntityDTOMapper.map(savedEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "allStudentsCache", key = "'allStudents'")
    public List<StudentDTO> getAllStudents(final List<String> address, final List<Integer> age, final List<Integer> firstNames) {
        log.info("Fetching all students with filters: addresses={}, ages={}, firstNames={}", address, age, firstNames);
        final List<StudentEntity> studentEntities = studentRepository.findAll();
        List<StudentDTO> studentDTOList = studentEntities
                .stream()
                .filter(student -> address == null || address.contains(student.getAddress()))
                .filter(student -> age == null || age.contains(student.getAge()))
                .filter(student -> firstNames == null || firstNames.contains(student.getFirstName()))
                .map(StudentEntityDTOMapper::map)
                .toList();
        log.info("Found {} students after filtering", studentDTOList.size());
        return studentDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "studentCache", key = "#id")
    public StudentDTO findStudentById(final Integer id) {
        log.info("Fetching student by ID: {}", id);
        return studentRepository.findById(id)
                .map(student -> {
                    log.info("Student found: {}", student);
                    return StudentEntityDTOMapper.map(student);
                })
                .orElseThrow(() -> {
                    log.error("Student not found with ID: {}", id);
                    return new StudentException("Student not found with ID: " + id);
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "studentCache", key = "#studentDTO.studentId")
    @CacheEvict(value = "allStudentsCache", allEntries = true)
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
    @CacheEvict(value = {"studentCache", "allStudentsCache"}, allEntries = true)
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
}
