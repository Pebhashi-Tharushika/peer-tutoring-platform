package com.mbpt.peertutoringplatform.service.impl;

import com.mbpt.peertutoringplatform.dto.ClassRoomDTO;
import com.mbpt.peertutoringplatform.dto.MentorDTO;
import com.mbpt.peertutoringplatform.entity.ClassRoomEntity;
import com.mbpt.peertutoringplatform.exception.ClassRoomException;
import com.mbpt.peertutoringplatform.mapper.ClassRoomEntityDTOMapper;
import com.mbpt.peertutoringplatform.mapper.MentorEntityDTOMapper;
import com.mbpt.peertutoringplatform.repository.ClassRoomRepository;
import com.mbpt.peertutoringplatform.service.ClassRoomService;
import com.mbpt.peertutoringplatform.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClassRoomServiceImpl implements ClassRoomService {

    @Value("${spring.datasource.url}")
    private String datasource;

    private final ClassRoomRepository classRoomRepository;
    private final FileService fileService;

    public ClassRoomServiceImpl(ClassRoomRepository classRoomRepository, FileService fileService) {
        this.classRoomRepository = classRoomRepository;
        this.fileService = fileService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassRoomDTO createClassRoom(String title, MultipartFile classImage) {
        log.info("Creating new classroom...");

        if (classImage.isEmpty() || title.isBlank() ) {
            log.error("Failed to create classroom: input data is invalid.");
            throw new IllegalArgumentException("Classroom data are required");
        }
        String imageUrl = fileService.uploadImage(classImage);

        log.debug("image url received: {}", imageUrl);

        ClassRoomEntity classRoomEntity = new ClassRoomEntity(title, 0, imageUrl);

        ClassRoomEntity savedClassroomEntity = classRoomRepository.save(classRoomEntity);

        log.info("Created classroom with ID: {} at data-source: {}", savedClassroomEntity.getClassRoomId(), this.datasource);

        return ClassRoomEntityDTOMapper.map(savedClassroomEntity);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ClassRoomDTO> getAllClassRooms() {
        log.info("Fetching all classrooms...");

        List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAll();

        List<ClassRoomDTO> classRoomDTOList = classRoomEntities.stream()
                .map(entity -> {
                    ClassRoomDTO classRoomDTO = ClassRoomEntityDTOMapper.map(entity);

                    if (entity.getMentorEntity() != null) {
                        MentorDTO mentorDTO = MentorEntityDTOMapper.map(entity.getMentorEntity());
                        classRoomDTO.setMentorDTO(mentorDTO);
                    }

                    return classRoomDTO;
                })
                .collect(Collectors.toList());

        log.info("Found {} classrooms from data-source: {}", classRoomDTOList.size(), this.datasource);

        return classRoomDTOList;
    }


    @Override
    @Transactional(readOnly = true)
    public ClassRoomDTO findClassRoomById(Integer id) {
        log.info("Fetching classroom by ID: {} ...", id);
        return classRoomRepository.findById(id)
                .map(classRoom -> {
                    log.info("Classroom found: {}", classRoom);
                    ClassRoomDTO classRoomDTO = ClassRoomEntityDTOMapper.map(classRoom);
                    MentorDTO mentorDTO = MentorEntityDTOMapper.map(classRoom.getMentorEntity());
                    classRoomDTO.setMentorDTO(mentorDTO);
                    return classRoomDTO;
                })
                .orElseThrow(() -> {
                    log.error("Classroom not found with ID: {} from data-source:{}", id, this.datasource);
                    return new ClassRoomException("Classroom not found with ID: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassRoomDTO> findClassRoomsByFilters(String title, String mentorName, Integer minCount, Integer maxCount) {
        log.info("Searching classroom(s) by filtering...");

        List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAllClassRooms(title, mentorName, minCount, maxCount);

        List<ClassRoomDTO> classRoomDTOList = classRoomEntities.stream()
                .map(classRoomEntity -> {
                    ClassRoomDTO classRoomDTO = ClassRoomEntityDTOMapper.map(classRoomEntity);
                    classRoomDTO.setMentorDTO(MentorEntityDTOMapper.map(classRoomEntity.getMentorEntity()));
                    return classRoomDTO;
                }).toList();

        log.info("Found {} classroom(s) from data-source: {}", classRoomDTOList.size(), this.datasource);
        return classRoomDTOList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassRoomDTO updateClassRoomById(ClassRoomDTO classRoomDTO) {
        log.info("Updating classroom...");

        if (classRoomDTO == null || classRoomDTO.getClassRoomId() == null) {
            log.error("Failed to update classroom: DTO or classroom's ID is null.");
            throw new IllegalArgumentException("Classroom ID must not be null for update.");
        }

        Integer classRoomId = classRoomDTO.getClassRoomId();

        log.debug("Updating classroom with ID: {}", classRoomId);

        ClassRoomEntity classRoomEntity = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> {
                    log.error("Failed to update classroom: Classroom not found with ID: {}", classRoomId);
                    return new ClassRoomException("Classroom not found with ID: " + classRoomId);
                });

        classRoomEntity.setTitle(classRoomDTO.getTitle());
        classRoomEntity.setEnrolledStudentCount(classRoomDTO.getEnrolledStudentCount());
        classRoomEntity.setClassImage(classRoomDTO.getClassImage());

        ClassRoomEntity updatedClassroomEntity = classRoomRepository.save(classRoomEntity);
        log.info("Updated classroom with ID: {}", classRoomId);
        return ClassRoomEntityDTOMapper.map(updatedClassroomEntity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassRoomDTO deleteClassRoomById(Integer id) {
        log.info("Deleting classroom with ID: {} ...", id);
        ClassRoomEntity classRoomEntity = classRoomRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to delete classroom. Classroom not found with ID: {}", id);
                    return new ClassRoomException("Failed to delete classroom. Classroom not found with ID: " + id);
                });
        classRoomRepository.deleteById(id);
        log.info("Deleted classroom with ID: {} ", id);
        return ClassRoomEntityDTOMapper.map(classRoomEntity);
    }


}
