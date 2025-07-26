package com.mbpt.skillmentor.root.mapper;

import com.mbpt.skillmentor.root.dto.ClassRoomDTO;
import com.mbpt.skillmentor.root.entity.ClassRoomEntity;

public class ClassRoomEntityDTOMapper {

    public static ClassRoomEntity map(ClassRoomDTO classRoomDTO) {
        ClassRoomEntity classRoomEntity = new ClassRoomEntity();
        classRoomEntity.setClassRoomId(classRoomDTO.getClassRoomId());
        classRoomEntity.setTitle(classRoomDTO.getTitle());
        classRoomEntity.setEnrolledStudentCount(classRoomDTO.getEnrolledStudentCount());
        classRoomEntity.setClassImage(classRoomDTO.getClassImage());
        return classRoomEntity;
    }

    public static ClassRoomDTO map(ClassRoomEntity classRoomEntity) {
        ClassRoomDTO classRoomDTO = new ClassRoomDTO();
        classRoomDTO.setClassRoomId(classRoomEntity.getClassRoomId());
        classRoomDTO.setTitle(classRoomEntity.getTitle());
        classRoomDTO.setEnrolledStudentCount(classRoomEntity.getEnrolledStudentCount());
        classRoomDTO.setClassImage(classRoomEntity.getClassImage());
        return classRoomDTO;
    }
}
