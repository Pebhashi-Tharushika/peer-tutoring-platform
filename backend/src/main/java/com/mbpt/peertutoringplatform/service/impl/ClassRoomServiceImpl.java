package com.mbpt.peertutoringplatform.service.impl;

import com.mbpt.peertutoringplatform.dto.ClassRoomDTO;
import com.mbpt.peertutoringplatform.dto.MentorDTO;
import com.mbpt.peertutoringplatform.entity.ClassRoomEntity;
import com.mbpt.peertutoringplatform.entity.MentorEntity;
import com.mbpt.peertutoringplatform.exception.ResourceNotFoundException;
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

        if (classImage == null || classImage.isEmpty() || title == null || title.isBlank()) {
            log.error("Failed to create classroom: input data is invalid.");
            throw new IllegalArgumentException("Classroom data are required");
        }
        String imageUrl = fileService.uploadImage(classImage, "classrooms");

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

        if (id == null) throw new IllegalArgumentException("Classroom id is required");

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
                    return new ResourceNotFoundException("Classroom not found with ID: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassRoomDTO> getClassroomsWithoutMentor() {
        log.info("Fetching classrooms without an assigned mentor...");

        List<ClassRoomDTO> classRoomDTOS = classRoomRepository.findAllByMentorEntityIsNull()
                .stream().map(ClassRoomEntityDTOMapper::map).toList();

        log.info("Found {} classrooms without an assigned mentor from data-source: {}", classRoomDTOS.size(), this.datasource);

        return classRoomDTOS;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassRoomDTO updateClassRoomById(Integer id, String title, MultipartFile classImage, String imageUrl) {
        log.info("Updating classroom with id: {} ...", id);

        if (id == null || title == null) throw new IllegalArgumentException("Classroom id and title are required");

        ClassRoomEntity classRoomEntity = classRoomRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to update classroom: Classroom not found with ID: {}", id);
                    return new ResourceNotFoundException("Classroom not found with ID: " + id);
                });

        classRoomEntity.setTitle(title);

        if (classImage != null && !classImage.isEmpty()) {
            String newImageUrl = fileService.uploadImage(classImage, "classrooms");
            classRoomEntity.setClassImage(newImageUrl);
        } else if (imageUrl != null && !imageUrl.isBlank()) {
            classRoomEntity.setClassImage(imageUrl);
        }

        ClassRoomEntity updatedClassroomEntity = classRoomRepository.save(classRoomEntity);

        log.info("Updated classroom with ID: {}", id);

        ClassRoomDTO classRoomDTO = ClassRoomEntityDTOMapper.map(updatedClassroomEntity);
        MentorEntity assignedMentor = updatedClassroomEntity.getMentorEntity();
        if (assignedMentor != null) classRoomDTO.setMentorDTO(MentorEntityDTOMapper.map(assignedMentor));

        return classRoomDTO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassRoomDTO deleteClassRoomById(Integer id) {
        log.info("Deleting classroom with ID: {} ...", id);

        if (id == null) throw new IllegalArgumentException("Classroom id is required");

        ClassRoomEntity classRoomEntity = classRoomRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to delete classroom. Classroom not found with ID: {}", id);
                    return new ResourceNotFoundException("Failed to delete classroom. Classroom not found with ID: " + id);
                });

        MentorEntity mentorEntity = classRoomEntity.getMentorEntity();
        if (mentorEntity.getClassRoomEntityList().size() == 1) {
            String errorMessage = String.format("Failed to delete classroom. Please delete or assign the classroom to mentor with ID: %S, before deleting classroom",
                    mentorEntity.getMentorId());
            throw new IllegalArgumentException(errorMessage);
        }

        classRoomRepository.deleteById(id);
        log.info("Deleted classroom with ID: {} ", id);
        return ClassRoomEntityDTOMapper.map(classRoomEntity);
    }


}
