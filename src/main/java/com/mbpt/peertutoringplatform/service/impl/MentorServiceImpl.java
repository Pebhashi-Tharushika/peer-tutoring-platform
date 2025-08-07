package com.mbpt.peertutoringplatform.service.impl;

import com.mbpt.peertutoringplatform.dto.MentorClassDTO;
import com.mbpt.peertutoringplatform.dto.MentorDTO;
import com.mbpt.peertutoringplatform.dto.MentorProfileDTO;
import com.mbpt.peertutoringplatform.entity.ClassRoomEntity;
import com.mbpt.peertutoringplatform.entity.MentorEntity;
import com.mbpt.peertutoringplatform.exception.ClassRoomException;
import com.mbpt.peertutoringplatform.exception.MentorException;
import com.mbpt.peertutoringplatform.mapper.MentorEntityDTOMapper;
import com.mbpt.peertutoringplatform.repository.ClassRoomRepository;
import com.mbpt.peertutoringplatform.repository.MentorRepository;
import com.mbpt.peertutoringplatform.repository.SessionRepository;
import com.mbpt.peertutoringplatform.service.MentorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public MentorServiceImpl(MentorRepository mentorRepository, ClassRoomRepository classRoomRepository, SessionRepository sessionRepository) {
        this.mentorRepository = mentorRepository;
        this.classRoomRepository = classRoomRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MentorDTO createMentor(MentorDTO mentorDTO) {
        log.info("Creating new mentor...");

        if (mentorDTO == null) {
            log.error("Failed to create mentor: input DTO is null.");
            throw new IllegalArgumentException("Mentor data must not be null.");
        }

        log.debug("MentorDTO received: {}", mentorDTO);

        if (mentorDTO.getMentorId() != null) {
            log.warn("Attempted to create a mentor that already exists with ID: {}", mentorDTO.getMentorId());
            throw new IllegalArgumentException("New Mentor should not already have an ID.");
        }

        MentorEntity mentorEntity = MentorEntityDTOMapper.map(mentorDTO);
        mentorEntity.setPositiveReviews(0);
        mentorEntity.setIsCertified(false);
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
                            return new ClassRoomException("Classroom not found with ID: " + classRoomId);
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

    @Override
    @Transactional(readOnly = true)
    public List<MentorDTO> getAllMentors(String name, String classroom, String profession, Boolean isVerified) {
        log.info("Fetching all mentors...");

        List<MentorEntity> mentorEntityList = mentorRepository.findAllMentors(name, classroom, profession, isVerified);

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
    @Transactional(readOnly = true)
    public MentorDTO findMentorByClerkId(String clerkId) {
        log.info("Fetching mentor by Clerk ID...");
        return mentorRepository.findByClerkMentorId(clerkId)
                .map(mentorEntity -> {
                    log.info("Mentor found: {}", mentorEntity);
                    return MentorEntityDTOMapper.map(mentorEntity);
                })
                .orElseThrow(() -> {
                    log.error("Mentor not found with ID: {} from data-source:{}", clerkId, this.datasource);
                    return new MentorException("Mentor not found with Clerk ID: " + clerkId);
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MentorDTO updateMentorById(MentorDTO mentorDTO) {
        log.info("Updating mentor...");

        if (mentorDTO == null || mentorDTO.getMentorId() == null) {
            log.error("Failed to update mentor: DTO or mentorId is null.");
            throw new IllegalArgumentException("Mentor ID must not be null for update.");
        }

        Integer mentorId = mentorDTO.getMentorId();

        log.debug("Updating classroom with ID: {}", mentorId);


        MentorEntity mentorEntity = mentorRepository.findById(mentorDTO.getMentorId())
                .orElseThrow(() -> {
                    log.error("Failed to update mentor: Mentor not found with ID: {}", mentorId);
                    return new MentorException("Mentor not found with ID: " + mentorId);
                });
        mentorEntity.setFirstName(mentorDTO.getFirstName());
        mentorEntity.setLastName(mentorDTO.getLastName());
        mentorEntity.setEmail(mentorDTO.getEmail());
        mentorEntity.setAddress(mentorDTO.getAddress());
        mentorEntity.setPhoneNumber(mentorEntity.getPhoneNumber());
        mentorEntity.setTitle(mentorDTO.getTitle());
        mentorEntity.setSessionFee(mentorDTO.getSessionFee());
        mentorEntity.setProfession(mentorDTO.getProfession());
        mentorEntity.setSubject(mentorDTO.getSubject());
        mentorEntity.setQualification(mentorDTO.getQualification());
        mentorEntity.setClerkMentorId(mentorDTO.getClerkMentorId());
        mentorEntity.setMentorImage(mentorDTO.getMentorImage());
        mentorEntity.setIsCertified(mentorDTO.getIsCertified());

        MentorEntity updatedEntity = mentorRepository.save(mentorEntity);
        log.info("Updated mentor with ID: {}", mentorId);
        return MentorEntityDTOMapper.map(updatedEntity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MentorDTO deleteMentorByClerkId(String clerkId) {
        log.info("Deleting classroom...");
        MentorEntity mentorEntity = mentorRepository.findByClerkMentorId(clerkId)
                .orElseThrow(() -> {
                    log.error("Failed to delete mentor. Mentor not found with ID: {}", clerkId);
                    return new MentorException("Failed to delete mentor. Mentor not found with Clerk ID: " + clerkId);
                });
        mentorRepository.delete(mentorEntity);
        log.info("Deleted mentor with Clerk ID: {} ", clerkId);
        return MentorEntityDTOMapper.map(mentorEntity);
    }

    @Override
    public MentorProfileDTO getMentorProfile(Integer id) {
        MentorEntity mentor = mentorRepository.findById(id).orElseThrow(() -> {
            log.error("Mentor not found with ID: {}", id);
            return new MentorException("Failed load mentor's profile. Mentor not found with ID: " + id);
        });
        MentorDTO mentorDTO = MentorEntityDTOMapper.map(mentor);
        mentorDTO.setClassRoomIdList(mentor.getClassRoomEntityList().stream().map(ClassRoomEntity::getClassRoomId).collect(Collectors.toList()));
        List<MentorClassDTO> mentorClassDTOS = mentor.getClassRoomEntityList().stream().map(cls -> {
            long count = sessionRepository.countByClassRoomEntity_ClassRoomIdAndMentorEntity_MentorId(cls.getClassRoomId(), mentor.getMentorId());
            return new MentorClassDTO(cls.getTitle(), count);
        }).toList();
        return new MentorProfileDTO(mentorDTO, mentorClassDTOS);
    }

}
