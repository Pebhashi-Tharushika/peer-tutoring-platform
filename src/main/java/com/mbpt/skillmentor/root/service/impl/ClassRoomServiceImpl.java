package com.mbpt.skillmentor.root.service.impl;

import com.mbpt.skillmentor.root.dto.ClassRoomDTO;
import com.mbpt.skillmentor.root.dto.MentorDTO;
import com.mbpt.skillmentor.root.entity.ClassRoomEntity;
import com.mbpt.skillmentor.root.entity.MentorEntity;
import com.mbpt.skillmentor.root.mapper.ClassRoomEntityDTOMapper;
import com.mbpt.skillmentor.root.mapper.MentorEntityDTOMapper;
import com.mbpt.skillmentor.root.repository.ClassRoomRepository;
import com.mbpt.skillmentor.root.service.ClassRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassRoomServiceImpl implements ClassRoomService {

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Override
    public ClassRoomDTO createClassRoom(ClassRoomDTO classRoomDTO) {
//        // dto -> entity
//        ClassRoomEntity classRoomEntity = ClassRoomEntityDTOMapper.map(classRoomDTO);
//
//        // set mentor entity list
//        if (classRoomDTO.getMentorDTOList() != null && !classRoomDTO.getMentorDTOList().isEmpty()) {
//            List<MentorEntity> mentorEntities = classRoomDTO.getMentorDTOList().stream()
//                    .map(MentorEntityDTOMapper::map)
//                    .collect(Collectors.toList());
//            classRoomEntity.setMentorEntities(mentorEntities);
//        }
//
//        // save new classroom
//        ClassRoomEntity savedEntity = classRoomRepository.save(classRoomEntity);
//
//        // entity -> dto
//        ClassRoomDTO returnedClassRoomDTO = ClassRoomEntityDTOMapper.map(savedEntity);
//
//        // set mentor dto list
//        if (savedEntity.getMentorEntities() != null && !savedEntity.getMentorEntities().isEmpty()) {
//            List<MentorDTO> mentorDTOS = savedEntity.getMentorEntities().stream()
//                    .map(MentorEntityDTOMapper::map)
//                    .collect(Collectors.toList());
//            returnedClassRoomDTO.setMentorDTOList(mentorDTOS);
//        }
//
//        return returnedClassRoomDTO;
return null;
    }

    @Override
    public List<ClassRoomDTO> getAllClassRooms() {
//        return classRoomRepository.findAll()
//                .stream()
//                .map(
//                        entity -> {
//                            ClassRoomDTO classRoomDTO = ClassRoomEntityDTOMapper.map(entity);
//
//                            List<MentorDTO> mentorDTOS = entity.getMentorEntities()
//                                    .stream()
//                                    .map(
//                                            mentorEntity -> {
//                                                MentorDTO mentorDTO = MentorEntityDTOMapper.map(mentorEntity);
//                                                // set classroom ids for mentorDTO
//                                                List<ClassRoomEntity> classRoomEntities = mentorEntity.getClassRoomEntities();
//                                                if (classRoomEntities != null && !classRoomEntities.isEmpty()) {
//                                                    List<Integer> ids = classRoomEntities.stream().map(ClassRoomEntity::getClassRoomId).toList();
//                                                    mentorDTO.setClassRoomIds(ids);
//                                                }
//                                                return mentorDTO;
//                                            }
//                                    )
//                                    .collect(Collectors.toList());
//                            classRoomDTO.setMentorDTOList(mentorDTOS);
//                            return classRoomDTO;
//                        }
//                )
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public ClassRoomDTO getClassRoomById(Integer id) {

//        ClassRoomEntity classRoomEntity = classRoomRepository.findById(id).orElseThrow(
//                () -> new RuntimeException("ClassRoom Not Found"));
//
//        ClassRoomDTO classRoomDTO = ClassRoomEntityDTOMapper.map(classRoomEntity);
//
//        // set mentors list for classRoomDTO
//        List<MentorEntity> mentorEntityList = classRoomEntity.getMentorEntities();
//        if (mentorEntityList != null && !mentorEntityList.isEmpty()) {
//            List<MentorDTO> mentors = mentorEntityList
//                    .stream()
//                    .map(
//                            entity -> {
//                                MentorDTO mentorDTO = MentorEntityDTOMapper.map(entity);
//                                // set classroom ids for mentorDTO
//                                List<ClassRoomEntity> classRoomEntities = entity.getClassRoomEntities();
//                                if (classRoomEntities != null && !classRoomEntities.isEmpty()) {
//                                    classRoomEntities.forEach(e -> mentorDTO.getClassRoomIds().add(e.getClassRoomId()));
//                                }
//                                return mentorDTO;
//                            }
//                    ).collect(Collectors.toList());
//            classRoomDTO.setMentorDTOList(mentors);
//        }
//
//        return classRoomDTO;
        return null;
    }

    @Override
    public ClassRoomDTO updateClassRoomById(ClassRoomDTO classRoomDTO) {

//        ClassRoomEntity classRoomEntity = classRoomRepository.findById(classRoomDTO.getClassRoomId())
//                .orElseThrow(() -> new RuntimeException("ClassRoom Not Found"));
//
//        classRoomEntity.setTitle(classRoomDTO.getTitle());
//        classRoomEntity.setSessionFee(classRoomDTO.getSessionFee());
//        classRoomEntity.setEnrolledStudentCount(classRoomDTO.getEnrolledStudentCount());
//
//        ClassRoomDTO updatedClassRoom = ClassRoomEntityDTOMapper.map(classRoomRepository.save(classRoomEntity));
//
//        List<MentorEntity> mentorEntityList = classRoomEntity.getMentorEntities();
//        List<MentorDTO> mentorDTOList = mentorEntityList
//                .stream()
//                .map(
//                        mentorEntity -> {
//                            MentorDTO mentorDTO = MentorEntityDTOMapper.map(mentorEntity);
//                            // set classroom ids for mentorDTO
//                            List<ClassRoomEntity> classRoomEntities = mentorEntity.getClassRoomEntities();
//                            if (classRoomEntities != null && !classRoomEntities.isEmpty()) {
//                                List<Integer> ids = classRoomEntities.stream().map(ClassRoomEntity::getClassRoomId).toList();
//                                mentorDTO.setClassRoomIds(ids);
//                            }
//                            return mentorDTO;
//                        }
//                ).collect(Collectors.toList());
//        updatedClassRoom.setMentorDTOList(mentorDTOList);
//
//        return updatedClassRoom;
        return null;
    }

    @Override
    public ClassRoomDTO deleteClassRoomById(Integer id) {

//        ClassRoomEntity classRoomEntity = classRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("ClassRoom Not Found"));
//
//        // set mentors list for classRoomDTO
//        ClassRoomDTO classRoomDTO = ClassRoomEntityDTOMapper.map(classRoomEntity);
//
//        List<MentorEntity> mentorEntityList = classRoomEntity.getMentorEntities();// Access the lazy collection before deletion
//        if (mentorEntityList != null && !mentorEntityList.isEmpty()) {
//            List<MentorDTO> mentors = mentorEntityList
//                    .stream()
//                    .map(
//                            entity -> {
//                                MentorDTO mentorDTO = MentorEntityDTOMapper.map(entity);
//                                // set classroom id for mentorDTO
//                                List<ClassRoomEntity> classrooms = entity.getClassRoomEntities();
//                                if (classrooms != null && !classrooms.isEmpty()) {
//                                    List<Integer> ids = classrooms.stream().map(ClassRoomEntity::getClassRoomId).toList();
//                                    mentorDTO.setClassRoomIds(ids);
//                                }
//                                return mentorDTO;
//                            }
//                    ).collect(Collectors.toList());
//            classRoomDTO.setMentorDTOList(mentors);
//
//        }
//
//        classRoomRepository.deleteById(id); //delete classroom
//
//        return classRoomDTO;
        return null;
    }
}
