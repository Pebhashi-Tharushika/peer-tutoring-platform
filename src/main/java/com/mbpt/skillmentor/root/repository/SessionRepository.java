package com.mbpt.skillmentor.root.repository;

import com.mbpt.skillmentor.root.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Integer> {

    @Query(value = "SELECT s.* FROM sessions s " +
            "JOIN students stu " +
            "ON s.student_id = stu.student_id " +
            "WHERE stu.clerk_student_id=:studentClerkId", nativeQuery = true)
    List<Object> findSessionsByStudentClerkId(@Param("studentClerkId") String studentClerkId);

    @Query(value = "SELECT m.mentor_id AS mentorId, " +
            "CONCAT(m.first_name, ' ', m.last_name) AS mentorName, " +
            "SUM(m.session_fee) AS totalFee " +
            "FROM sessions s  JOIN mentors m " +
            "ON s.mentor_id = m.mentor_id " +
            "WHERE s.start_time BETWEEN :startTime AND :endTime " +
            "GROUP BY m.mentor_id;", nativeQuery = true)
    List<Object> findMentorPayments(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
