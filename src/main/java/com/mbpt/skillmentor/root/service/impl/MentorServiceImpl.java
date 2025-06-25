package com.mbpt.skillmentor.root.service.impl;

import com.mbpt.skillmentor.root.dto.ClassRoomDTO;
import com.mbpt.skillmentor.root.dto.MentorDTO;
import com.mbpt.skillmentor.root.entity.ClassRoomEntity;
import com.mbpt.skillmentor.root.entity.MentorEntity;
import com.mbpt.skillmentor.root.mapper.ClassRoomEntityDTOMapper;
import com.mbpt.skillmentor.root.mapper.MentorEntityDTOMapper;
import com.mbpt.skillmentor.root.repository.ClassRoomRepository;
import com.mbpt.skillmentor.root.repository.MentorRepository;
import com.mbpt.skillmentor.root.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MentorServiceImpl implements MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    ClassRoomRepository classRoomRepository;

    @Override
    public MentorDTO createMentor(MentorDTO mentorDTO) {
//        // dto -> entity
//        MentorEntity mentorEntity = MentorEntityDTOMapper.map(mentorDTO);
//
//        // Fetch classrooms of new mentor DTO and set both sides of the relationship
//        if (mentorDTO.getClassRoomIds() != null && !mentorDTO.getClassRoomIds().isEmpty()) {
//            List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAllById(mentorDTO.getClassRoomIds());
//
//            // Set new mentor to its each classroom
//            for (ClassRoomEntity classroom : classRoomEntities) {
//                classroom.getMentorEntities().add(mentorEntity);
//            }
//
//            // Set classrooms to mentor
//            mentorEntity.setClassRoomEntities(classRoomEntities);
//        }
//
//        // save new mentor
//        MentorEntity savedEntity = mentorRepository.save(mentorEntity);
//
//        // entity -> dto
//        MentorDTO savedMentorDTO = MentorEntityDTOMapper.map(savedEntity);
//
//        // map classroom entity list to classroom DTO list
//        if (savedEntity.getClassRoomEntities() != null && !savedEntity.getClassRoomEntities().isEmpty()) {
//            List<ClassRoomDTO> classroomDTOs = savedEntity.getClassRoomEntities().stream()
//                    .map(ClassRoomEntityDTOMapper::map)
//                    .toList();
//            savedMentorDTO.setClassRoomDTOList(classroomDTOs);
//        }
//
//        return savedMentorDTO;
        return null;
    }

    @Override
    public List<MentorDTO> getAllMentors() {
//        List<MentorEntity> mentorEntities = mentorRepository.findAll();
//        return mentorEntities
//                .stream()
//                .map(
//                        entity -> {
//                            // list of classroom entities of each mentor entity -> list of classroom DTO
//                            List<ClassRoomDTO> classRoomDTOS = entity.getClassRoomEntities()
//                                    .stream()
//                                    .map(ClassRoomEntityDTOMapper::map)
//                                    .toList();
//                            MentorDTO mentorDTO = MentorEntityDTOMapper.map(entity);
//                            mentorDTO.setClassRoomDTOList(classRoomDTOS);
//                            return mentorDTO;
//                        }
//                )
//                .toList();
        return null;
    }

    @Override
    public MentorDTO getMentorById(Integer id) {
//        Optional<MentorEntity> optionalMentorEntity = mentorRepository.findById(id);
//        if (optionalMentorEntity.isEmpty()) throw new RuntimeException("Mentor Not Found");
//
//        MentorEntity mentorEntity = optionalMentorEntity.get();
//        MentorDTO mentorDTO = MentorEntityDTOMapper.map(mentorEntity);
//
//        // set classroom DTOs list for mentorDTO
//        List<ClassRoomEntity> classRoomEntities = mentorEntity.getClassRoomEntities();
//        if (!classRoomEntities.isEmpty()) {
//            List<ClassRoomDTO> classRoomDTOS = classRoomEntities.stream().map(ClassRoomEntityDTOMapper::map).collect(Collectors.toList());
//            mentorDTO.setClassRoomDTOList(classRoomDTOS);
//        }
//
//        return mentorDTO;
        return null;
    }

    @Override
    public MentorDTO updateMentorById(MentorDTO mentorDTO) {

//        MentorEntity selectedMentorEntity = mentorRepository.findById(mentorDTO.getMentorId())
//                .orElseThrow(() -> new RuntimeException("Mentor Not Found"));
//
//        // update fields
//        selectedMentorEntity.setFirstName(mentorDTO.getFirstName());
//        selectedMentorEntity.setLastName(mentorDTO.getLastName());
//        selectedMentorEntity.setEmail(mentorDTO.getEmail());
//        selectedMentorEntity.setAddress(mentorDTO.getAddress());
//        selectedMentorEntity.setTitle(mentorDTO.getTitle());
//        selectedMentorEntity.setProfession(mentorDTO.getProfession());
//        selectedMentorEntity.setSubject(mentorDTO.getSubject());
//        selectedMentorEntity.setQualification(mentorDTO.getQualification());
//
//        // Remove mentor from current classrooms
//        List<ClassRoomEntity> currentClassrooms = selectedMentorEntity.getClassRoomEntities();
//        if (currentClassrooms != null && !currentClassrooms.isEmpty()) {
//            currentClassrooms.forEach(classRoom -> classRoom.getMentorEntities().remove(selectedMentorEntity));
//            classRoomRepository.saveAll(currentClassrooms);
//        }
//
//        // Set classrooms to mentor entity and also add mentor into new classrooms
//        if (mentorDTO.getClassRoomIds() != null && !mentorDTO.getClassRoomIds().isEmpty()) {
//            List<ClassRoomEntity> newClassrooms = classRoomRepository.findAllById(mentorDTO.getClassRoomIds());
//
//            selectedMentorEntity.setClassRoomEntities(newClassrooms);
//
//            newClassrooms.forEach(classRoom -> classRoom.getMentorEntities().add(selectedMentorEntity));
//            classRoomRepository.saveAll(newClassrooms);
//        }
//
//        // update mentor
//        MentorEntity updatedMentorEntity = mentorRepository.save(selectedMentorEntity);
//
//        // entity -> dto
//        MentorDTO updatedMentorDTO = MentorEntityDTOMapper.map(updatedMentorEntity);
//
//        // map classroom entity list of mentor entity -> classroom dto list of mentorDTO
//        if (updatedMentorEntity.getClassRoomEntities() != null && !updatedMentorEntity.getClassRoomEntities().isEmpty()) {
//            List<ClassRoomDTO> updatedClassRoomDTOs = updatedMentorEntity.getClassRoomEntities()
//                    .stream()
//                    .map(ClassRoomEntityDTOMapper::map)
//                    .collect(Collectors.toList());
//            updatedMentorDTO.setClassRoomDTOList(updatedClassRoomDTOs);
//
//        }
//        return updatedMentorDTO;
        return null;
    }


    @Override
    public MentorDTO deleteMentorById(Integer id) {
//        MentorEntity mentorEntity = mentorRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Mentor Not Found"));
//
//        List<ClassRoomEntity> classRoomEntities = mentorEntity.getClassRoomEntities();
//
//        // Remove mentor from classroom side
//        if (classRoomEntities != null && !classRoomEntities.isEmpty()) {
//            classRoomEntities.forEach(classRoom -> classRoom.getMentorEntities().remove(mentorEntity));
//            classRoomRepository.saveAll(classRoomEntities);
//        }
//
//        // entity -> dto
//        MentorDTO mentorDTO = MentorEntityDTOMapper.map(mentorEntity);
//
//        // set classroom DTO list for mentorDTO
//        if (classRoomEntities != null && !classRoomEntities.isEmpty()) {
//            List<ClassRoomDTO> classRoomDTOS = classRoomEntities.stream().map(ClassRoomEntityDTOMapper::map).collect(Collectors.toList());
//            mentorDTO.setClassRoomDTOList(classRoomDTOS);
//        }
//
//        // delete mentor
//        mentorRepository.deleteById(id);
//
//
//        return mentorDTO;
        return null;
    }
}
