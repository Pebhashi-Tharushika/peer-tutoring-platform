package com.mbpt.peertutoringplatform.service.impl;

import com.mbpt.peertutoringplatform.dto.LiteMentorDTO;
import com.mbpt.peertutoringplatform.dto.MentorClassDTO;
import com.mbpt.peertutoringplatform.dto.MentorDTO;
import com.mbpt.peertutoringplatform.dto.MentorProfileDTO;
import com.mbpt.peertutoringplatform.entity.ClassRoomEntity;
import com.mbpt.peertutoringplatform.entity.MentorEntity;
import com.mbpt.peertutoringplatform.exception.ResourceNotFoundException;
import com.mbpt.peertutoringplatform.mapper.MentorEntityDTOMapper;
import com.mbpt.peertutoringplatform.repository.ClassRoomRepository;
import com.mbpt.peertutoringplatform.repository.MentorRepository;
import com.mbpt.peertutoringplatform.repository.SessionRepository;
import com.mbpt.peertutoringplatform.service.FileService;
import com.mbpt.peertutoringplatform.service.MentorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MentorServiceImpl implements MentorService {

    @Value("${spring.datasource.url}")
    private String datasource;

    private final MentorRepository mentorRepository;

    private final ClassRoomRepository classRoomRepository;

    private final SessionRepository sessionRepository;

    private final FileService fileService;

    public MentorServiceImpl(MentorRepository mentorRepository, ClassRoomRepository classRoomRepository, SessionRepository sessionRepository, FileService fileService) {
        this.mentorRepository = mentorRepository;
        this.classRoomRepository = classRoomRepository;
        this.sessionRepository = sessionRepository;
        this.fileService = fileService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MentorDTO createMentor(LiteMentorDTO liteMentorDTO, MultipartFile image) {
        log.info("Creating new mentor...");

        if (image == null || image.isEmpty() || liteMentorDTO == null) {
            log.error("Failed to create mentor: input data is invalid.");
            throw new IllegalArgumentException("Mentor data are required.");
        }

        String imageUrl = fileService.uploadImage(image, "mentors");

        log.debug("image url received: {}", imageUrl);

        MentorDTO mentorDTO = getMentorDTO(liteMentorDTO, imageUrl);

        MentorEntity mentorEntity = MentorEntityDTOMapper.map(mentorDTO);

        MentorEntity savedMentor = mentorRepository.save(mentorEntity);

        List<Integer> classRoomIdList = mentorDTO.getClassRoomIdList();
        if (classRoomIdList == null || classRoomIdList.isEmpty()) {
            log.error("Failed to create mentor: list of classroom's ID is null or empty.");
            throw new IllegalArgumentException("At least one classroom ID must be provided.");
        }
        classRoomIdList.forEach(classRoomId -> {
            if (classRoomId != null) {
                ClassRoomEntity classRoomEntity = classRoomRepository.findById(classRoomId)
                        .orElseThrow(() -> {
                            log.error("Classroom not found with ID: {}", classRoomId);
                            return new ResourceNotFoundException("Classroom not found with ID: " + classRoomId);
                        });
                classRoomEntity.setMentorEntity(mentorEntity);
                classRoomRepository.save(classRoomEntity);
            }
        });


        log.info("Mentor created with ID: {} at data-source: {}", savedMentor.getMentorId(), this.datasource);

        MentorDTO savedMentorDTO = MentorEntityDTOMapper.map(savedMentor);
        savedMentorDTO.setClassRoomIdList(classRoomIdList);
        return savedMentorDTO;
    }

    private static MentorDTO getMentorDTO(LiteMentorDTO liteMentorDTO, String imageUrl) {
        MentorDTO mentorDTO = new MentorDTO();

        mentorDTO.setFirstName(liteMentorDTO.getFirstName());
        mentorDTO.setLastName(liteMentorDTO.getLastName());
        mentorDTO.setAddress(liteMentorDTO.getAddress());
        mentorDTO.setEmail(liteMentorDTO.getEmail());
        mentorDTO.setPhoneNumber(liteMentorDTO.getPhoneNumber());
        mentorDTO.setProfession(liteMentorDTO.getProfession());
        mentorDTO.setQualification(liteMentorDTO.getQualification());
        mentorDTO.setSubject(liteMentorDTO.getSubject());
        mentorDTO.setSessionFee(liteMentorDTO.getSessionFee());
        mentorDTO.setTitle(liteMentorDTO.getTitle());
        mentorDTO.setClassRoomIdList(liteMentorDTO.getClassRoomIdList());
        mentorDTO.setMentorImage(imageUrl);
        mentorDTO.setPositiveReviews(0);
        mentorDTO.setIsCertified(false);
        return mentorDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MentorDTO> getAllMentors() {
        log.info("Fetching all mentors...");

        List<MentorEntity> mentorEntityList = mentorRepository.findAll();

        List<MentorDTO> mentorDTOList = mentorEntityList.stream().map(mentorEntity -> {
            MentorDTO mentorDTO = MentorEntityDTOMapper.map(mentorEntity);
            List<Integer> classroomIdList = mentorEntity.getClassRoomEntityList().stream()
                    .map(ClassRoomEntity::getClassRoomId).toList();
            mentorDTO.setClassRoomIdList(classroomIdList);
            return mentorDTO;
        }).toList();

        log.info("Found {} classrooms from data-source: {}", mentorDTOList.size(), this.datasource);

        return mentorDTOList;
    }

    @Override
    public MentorProfileDTO getMentorProfile(Integer id) {
        MentorEntity mentor = mentorRepository.findById(id).orElseThrow(() -> {
            log.error("Mentor not found with ID: {}", id);
            return new ResourceNotFoundException("Failed load mentor's profile. Mentor not found with ID: " + id);
        });
        MentorDTO mentorDTO = MentorEntityDTOMapper.map(mentor);
        mentorDTO.setClassRoomIdList(mentor.getClassRoomEntityList().stream().map(ClassRoomEntity::getClassRoomId).collect(Collectors.toList()));
        List<MentorClassDTO> mentorClassDTOS = mentor.getClassRoomEntityList().stream().map(cls -> {
            Integer count = sessionRepository.countByClassRoomIdAndMentorId(cls.getClassRoomId(), mentor.getMentorId());
            return new MentorClassDTO(cls.getTitle(), count);
        }).toList();
        return new MentorProfileDTO(mentorDTO, mentorClassDTOS);
    }

}
