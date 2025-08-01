package com.mbpt.peertutoringplatform.repository;

import com.mbpt.peertutoringplatform.entity.MentorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<MentorEntity, Integer> {

    /**
     * Finds a mentor by their clerk mentor ID.
     *
     * @param clerkMentorId the generated ID for the mentor by Clerk
     * @return an Optional containing the MentorEntity if found, otherwise empty
     */
    Optional<MentorEntity> findByClerkMentorId(String clerkMentorId);


    @Query(value = "SELECT DISTINCT m.* FROM mentors m " +
            "LEFT JOIN classrooms c ON m.id = c.mentor_id " +
            "WHERE (:name IS NULL OR LOWER(m.first_name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "       OR LOWER(m.last_name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:profession IS NULL OR m.profession = :profession) " +
            "AND (:classroomTitle IS NULL OR c.title = :classroomTitle) " +
            "AND (:isCertified IS NULL OR m.is_certified = :isCertified)",
            nativeQuery = true)
    List<MentorEntity> findAllMentors(@Param("name") String name,
                                      @Param("classroomTitle") String classroomTitle,
                                      @Param("profession") String profession,
                                      @Param("isCertified") Boolean isCertified);

}
