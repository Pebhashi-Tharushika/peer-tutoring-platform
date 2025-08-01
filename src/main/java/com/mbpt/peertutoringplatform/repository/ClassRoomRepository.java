package com.mbpt.peertutoringplatform.repository;

import com.mbpt.peertutoringplatform.entity.ClassRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoomEntity, Integer> {


    @Query(value = "SELECT DISTINCT c.* FROM classrooms c " +
            "JOIN mentors m ON m.id = c.mentor_id " +
            "WHERE (:mentorName IS NULL OR LOWER(m.first_name) LIKE LOWER(CONCAT('%', :mentorName, '%')) " +
            "       OR LOWER(m.last_name) LIKE LOWER(CONCAT('%', :mentorName, '%'))) " +
            "AND (:title IS NULL OR c.title = :title) " +
            "AND (:minCount IS NULL OR c.enrolled_student_count BETWEEN :minCount AND :maxCount) ",
            nativeQuery = true)
    List<ClassRoomEntity> findAllClassRooms(@Param("title") String title,
                                            @Param("mentorName") String mentorName,
                                            @Param("minCount") Integer minCount,
                                            @Param("maxCount") Integer maxCount);
}
