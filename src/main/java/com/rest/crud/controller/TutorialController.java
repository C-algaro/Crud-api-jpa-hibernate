package com.rest.crud.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.crud.model.Tutorial;
import com.rest.crud.repository.TutorialRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/tutorials")
public class TutorialController {
    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping()
    public ResponseEntity<List<Tutorial>> getByTitleTutorial(@RequestParam(required = false) String title) {
        try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			if (title == null)
				tutorialRepository.findAll().forEach(tutorials::add);
			else
				tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

			if (tutorials.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
            tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tutorial> getByIdTutorial(@PathVariable(value = "id") UUID id) {
        Optional<Tutorial> idTutorial = tutorialRepository.findById(id);
        if(idTutorial.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Tutorial> (idTutorial.get(), HttpStatus.OK);
    }

    @GetMapping("/published")
    public ResponseEntity<List<Tutorial>> getPublishedTutorials(@RequestParam boolean published) {
        try {
			List<Tutorial> trueTutorials = tutorialRepository.findByPublished(true);
            List<Tutorial> falseTutorials = tutorialRepository.findByPublished(false);

            if(published == true) {
                if (trueTutorials.isEmpty())
				    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			    return new ResponseEntity<>(trueTutorials, HttpStatus.OK);
            }
            
            if (falseTutorials.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(falseTutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @DeleteMapping()
    public ResponseEntity<List<Tutorial>> deleteAllTutorials() {
        tutorialRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tutorial> deleteByIdTutorial(@PathVariable(value = "id") UUID id) {
        Optional<Tutorial> deleteTutorial = tutorialRepository.findById(id);
        if(deleteTutorial.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        tutorialRepository.delete(deleteTutorial.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Tutorial> createTutorial(@RequestBody @Valid Tutorial tutorial) {
        try {
            Tutorial createdTutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), tutorial.isPublished()));
            return new ResponseEntity<>(createdTutorial, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable(value = "id")UUID id, @RequestBody Tutorial tutorial) {
        Optional<Tutorial> putTutorial = tutorialRepository.findById(id);

		if(putTutorial.isPresent()) {
			Tutorial updateTutorial = putTutorial.get();
			updateTutorial.setTitle(tutorial.getTitle());
			updateTutorial.setDescription(tutorial.getDescription());
			updateTutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(updateTutorial), HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
