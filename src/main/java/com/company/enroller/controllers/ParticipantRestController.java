package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") String sortOrder) {

		Collection<Participant> participants;

		if ("login".equalsIgnoreCase(sortBy)) {
			participants = participantService.getAllSortedByLogin(sortOrder);
		} else {
			participants = participantService.getAll();
		}

		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}


	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		Participant existingParticipant = participantService.findByLogin(participant.getLogin());

		if (existingParticipant != null) {
			return new ResponseEntity<String>(
					"Unable to create user: " + participant.getLogin() + ", User already exists.",
					HttpStatus.CONFLICT);
		}

		participantService.add(participant);
		return new ResponseEntity<String>(
				"User with login '" + participant.getLogin() + "' has been successfully created.",
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("login") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<String>(
					"Unable to delete. Participant with login " + login + " not found.",
					HttpStatus.NOT_FOUND);
		}

		participantService.delete(participant);
		return new ResponseEntity<String>(
				"User with login '" + login + "' has been deleted.",
				HttpStatus.OK);
	}


	@RequestMapping(value = "/{login}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("login") String login, @RequestBody Participant updatedParticipant) {
		Participant existingParticipant = participantService.findByLogin(login);
		if (existingParticipant == null) {
			return new ResponseEntity<String>(
					"Unable to update the participant. Participant with login " + login + " has not been found.",
					HttpStatus.NOT_FOUND);
		}

		existingParticipant.setPassword(updatedParticipant.getPassword());
		participantService.update(existingParticipant);

		return new ResponseEntity<String>(
				"User with login '" + login + "' has been successfully updated.",
				HttpStatus.OK);
	}

}
