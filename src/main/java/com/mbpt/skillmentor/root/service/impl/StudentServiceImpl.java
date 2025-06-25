package com.mbpt.skillmentor.root.service.impl;

import com.mbpt.skillmentor.root.dto.StudentDTO;
import com.mbpt.skillmentor.root.entity.StudentEntity;
import com.mbpt.skillmentor.root.mapper.StudentEntityDTOMapper;
import com.mbpt.skillmentor.root.repository.StudentRepository;
import com.mbpt.skillmentor.root.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        return StudentEntityDTOMapper.map(studentRepository.save(StudentEntityDTOMapper.map(studentDTO)));
    }

    @Override
    public List<StudentDTO> getAllStudents(List<String> address, List<Integer> age) {
        return studentRepository.findAll()
                .stream()
                .filter(entity -> address == null || address.contains(entity.getAddress()))
                .filter(entity -> age == null || age.contains(entity.getAge()))
                .map(StudentEntityDTOMapper::map)
                .toList();
    }

    @Override
    public StudentDTO getStudentById(Integer id) {
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(id);
        if (optionalStudentEntity.isEmpty()) throw new RuntimeException("Student Not Found");
        return StudentEntityDTOMapper.map(optionalStudentEntity.get());
    }

    @Override
    public StudentDTO updateStudentById(StudentDTO studentDTO) {
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(studentDTO.getStudentId());

        if (optionalStudentEntity.isEmpty()) throw new RuntimeException("Student Not Found");

        StudentEntity selectedStudentEntity = optionalStudentEntity.get();
        selectedStudentEntity.setFirstName(studentDTO.getFirstName());
        selectedStudentEntity.setLastName(studentDTO.getLastName());
        selectedStudentEntity.setEmail(studentDTO.getEmail());
        selectedStudentEntity.setAddress(studentDTO.getAddress());
        selectedStudentEntity.setPhoneNumber(studentDTO.getPhoneNumber());
        selectedStudentEntity.setAge(studentDTO.getAge());

        return StudentEntityDTOMapper.map(studentRepository.save(selectedStudentEntity));
    }

    @Override
    public StudentDTO deleteStudentById(Integer id) {
        Optional<StudentEntity> optionalStudentEntity = studentRepository.findById(id);
        if (optionalStudentEntity.isEmpty()) throw new RuntimeException("Student Not Found");
        studentRepository.deleteById(id);
        return StudentEntityDTOMapper.map(optionalStudentEntity.get());
    }
}
