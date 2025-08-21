package com.mbpt.peertutoringplatform.repository;

import com.mbpt.peertutoringplatform.entity.MentorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorRepository extends JpaRepository<MentorEntity, Integer> {

}
