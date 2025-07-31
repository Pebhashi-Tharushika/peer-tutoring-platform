package com.mbpt.peertutoringplatform.service.impl;

import com.mbpt.peertutoringplatform.dto.MentorDTO;
import com.mbpt.peertutoringplatform.entity.ClassRoomEntity;
import com.mbpt.peertutoringplatform.entity.MentorEntity;
import com.mbpt.peertutoringplatform.exception.ClassRoomException;
import com.mbpt.peertutoringplatform.exception.MentorException;
import com.mbpt.peertutoringplatform.mapper.MentorEntityDTOMapper;
import com.mbpt.peertutoringplatform.repository.ClassRoomRepository;
import com.mbpt.peertutoringplatform.repository.MentorRepository;
import com.mbpt.peertutoringplatform.service.MentorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MentorServiceImpl implements MentorService {

    @Value("${spring.datasource.url}")
    private String datasource;

    private final MentorRepository mentorRepository;

    private final ClassRoomRepository classRoomRepository;

    public MentorServiceImpl(MentorRepository mentorRepository, ClassRoomRepository classRoomRepository) {
        this.mentorRepository = mentorRepository;
        this.classRoomRepository = classRoomRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @CacheEvict(value = {"mentorCache", "allMentorsCache"}, allEntries = true)
    public MentorDTO createMentor(MentorDTO mentorDTO) {
        log.info("Creating new mentor...");
        if (mentorDTO == null) {
            log.error("Failed to create mentor: input DTO is null.");
            throw new IllegalArgumentException("Mentor data must not be null.");
        }
        log.debug("MentorDTO received: {}", mentorDTO);

        final MentorEntity mentorEntity = MentorEntityDTOMapper.map(mentorDTO);
        final MentorEntity savedMentor = mentorRepository.save(mentorEntity);
        List<Integer> classRoomIdList = mentorDTO.getClassRoomIdList();
        if (classRoomIdList != null && !classRoomIdList.isEmpty()) {
            classRoomIdList.forEach(classRoomId -> {
                if (Objects.nonNull(classRoomId)) {
                    final ClassRoomEntity classRoomEntity = classRoomRepository.findById(classRoomId)
                            .orElseThrow(() -> new ClassRoomException("Classroom not found with ID: " + classRoomId));
                    classRoomEntity.setMentorEntity(mentorEntity);
                    classRoomRepository.save(classRoomEntity);
                }
            });

        }
        log.info("Mentor created with ID: {} at data-source: {}", savedMentor.getMentorId(), this.datasource);
        final MentorDTO savedMentorDTO = MentorEntityDTOMapper.map(savedMentor);
        savedMentorDTO.setClassRoomIdList(classRoomIdList);
        return savedMentorDTO;
    }

    @Override
    public List<MentorDTO> getAllMentors(List<String> firstNames, List<String> subjects) {
        return mentorRepository.findAll().stream()
                .filter(mentor -> firstNames == null || firstNames.isEmpty() || firstNames.contains(mentor.getFirstName()))
                .filter(mentor -> subjects == null || subjects.isEmpty() || subjects.contains(mentor.getSubject()))
                .map(MentorEntityDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public MentorDTO findMentorById(Integer id) throws MentorException {
        return mentorRepository.findById(id)
                .map(MentorEntityDTOMapper::map)
                .orElseThrow(() -> new MentorException("Mentor not found with ID: " + id));
    }

    @Override
    public MentorDTO findMentorByClerkId(String clerkId) throws MentorException {
        return mentorRepository.findByClerkMentorId(clerkId)
                .map(MentorEntityDTOMapper::map)
                .orElseThrow(() -> new MentorException("Mentor not found with Clerk ID: " + clerkId));
    }

    @Override
    public MentorDTO updateMentorById(MentorDTO mentorDTO) throws MentorException {
        MentorEntity mentorEntity = mentorRepository.findById(mentorDTO.getMentorId())
                .orElseThrow(() -> new MentorException("Cannot update. Mentor not found with ID: " + mentorDTO.getMentorId()));
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
        final MentorEntity updatedEntity = mentorRepository.save(mentorEntity);
        return MentorEntityDTOMapper.map(updatedEntity);
    }


    @Override
    public MentorDTO deleteMentorById(Integer id) throws MentorException {
        final MentorEntity mentorEntity = mentorRepository.findById(id)
                .orElseThrow(() -> new MentorException("Cannot delete. Mentor not found with ID: " + id));
        mentorRepository.deleteById(id);
        return MentorEntityDTOMapper.map(mentorEntity);
    }

    @Override
    public MentorDTO deleteMentorByClerkId(String clerkId) throws MentorException {
        final MentorEntity mentorEntity = mentorRepository.findByClerkMentorId(clerkId)
                .orElseThrow(() -> new MentorException("Cannot delete. Mentor not found with Clerk ID: " + clerkId));
        mentorRepository.delete(mentorEntity);
        return MentorEntityDTOMapper.map(mentorEntity);
    }

}
