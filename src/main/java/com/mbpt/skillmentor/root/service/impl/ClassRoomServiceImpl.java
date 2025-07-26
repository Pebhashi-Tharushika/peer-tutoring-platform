package com.mbpt.skillmentor.root.service.impl;

import com.mbpt.skillmentor.root.dto.ClassRoomDTO;
import com.mbpt.skillmentor.root.dto.MentorDTO;
import com.mbpt.skillmentor.root.entity.ClassRoomEntity;
import com.mbpt.skillmentor.root.exception.ClassRoomException;
import com.mbpt.skillmentor.root.mapper.ClassRoomEntityDTOMapper;
import com.mbpt.skillmentor.root.mapper.MentorEntityDTOMapper;
import com.mbpt.skillmentor.root.repository.ClassRoomRepository;
import com.mbpt.skillmentor.root.service.ClassRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassRoomServiceImpl implements ClassRoomService {


    private final ClassRoomRepository classRoomRepository;

    public ClassRoomServiceImpl(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    @Override
    public ClassRoomDTO createClassRoom(ClassRoomDTO classRoomDTO) {
        final ClassRoomEntity classRoomEntity = ClassRoomEntityDTOMapper.map(classRoomDTO);
        final ClassRoomEntity savedEntity = classRoomRepository.save(classRoomEntity);
        return ClassRoomEntityDTOMapper.map(savedEntity);
    }

    @Override
    public List<ClassRoomDTO> getAllClassRooms() {
        final List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAll();
        return classRoomEntities
                .stream()
                .map(
                        entity -> {
                            final ClassRoomDTO classRoomDTO = ClassRoomEntityDTOMapper.map(entity);
                            if (entity.getMentor() != null) {
                                final MentorDTO mentorDTO = MentorEntityDTOMapper.map(entity.getMentor());
                                classRoomDTO.setMentorDTO(mentorDTO);
                            }
                            return classRoomDTO;
                        }
                ).collect(Collectors.toList());
    }

    @Override
    public ClassRoomDTO findClassRoomById(Integer id) {
        final Optional<ClassRoomEntity> classRoomEntity = classRoomRepository.findById(id);
        if (classRoomEntity.isEmpty()) {
            throw new ClassRoomException("ClassRoom not found");
        }
        ClassRoomEntity classRoom = classRoomEntity.get();
        ClassRoomDTO classRoomDTO = ClassRoomEntityDTOMapper.map(classRoom);
        if (classRoom.getMentor() != null) {
            MentorDTO mentorDTO = MentorEntityDTOMapper.map(classRoom.getMentor());
            classRoomDTO.setMentorDTO(mentorDTO);
        }
        return classRoomDTO;
    }

    @Override
    public ClassRoomDTO updateClassRoomById(ClassRoomDTO classRoomDTO) {
        final Optional<ClassRoomEntity> classRoomEntity = classRoomRepository.findById(classRoomDTO.getClassRoomId());
        if (classRoomEntity.isEmpty()) {
            throw new ClassRoomException("ClassRoom not found");
        }
        final ClassRoomEntity updatedEntity = classRoomEntity.get();
        updatedEntity.setTitle(classRoomDTO.getTitle());
        updatedEntity.setEnrolledStudentCount(classRoomDTO.getEnrolledStudentCount());
        final ClassRoomEntity savedEntity = classRoomRepository.save(updatedEntity);
        return ClassRoomEntityDTOMapper.map(savedEntity);
    }

    @Override
    public ClassRoomDTO deleteClassRoomById(Integer id) {
        final Optional<ClassRoomEntity> classRoomEntity = classRoomRepository.findById(id);
        if (classRoomEntity.isEmpty()) {
            throw new ClassRoomException("ClassRoom not found");
        }
        classRoomRepository.deleteById(id);
        return ClassRoomEntityDTOMapper.map(classRoomEntity.get());
    }
}
