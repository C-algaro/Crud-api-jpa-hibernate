package com.rest.crud.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.crud.model.Tutorial;

public interface TutorialRepository extends JpaRepository <Tutorial, UUID> {
    List<Tutorial> findByPublished(boolean published);
    List<Tutorial> findByTitleContaining(String title);
}
