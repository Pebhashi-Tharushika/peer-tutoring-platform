package com.mbpt.peertutoringplatform.repository;

import com.mbpt.peertutoringplatform.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Integer> {

    @Query("SELECT COUNT(s) FROM SessionEntity s " +
            "WHERE s.classRoomEntity.classRoomId = :classRoomId AND s.mentorEntity.mentorId = :mentorId")
    Integer countByClassRoomIdAndMentorId(@Param("classRoomId") Integer classRoomId, @Param("mentorId") Integer mentorId);
}
