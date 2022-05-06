package com.api.quickpoll.controller.v2;

import java.net.URI;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.quickpoll.domain.Poll;
import com.api.quickpoll.exception.ResourceNotFoundException;
import com.api.quickpoll.repository.PollRepository;

@RestController
@RequestMapping("/v2")
public class PollControllerV2 {
	
	@Inject
	private PollRepository pollRepository;
	
	@GetMapping("/polls")
	public ResponseEntity<Page<Poll>> getAllPoll(Pageable pageable) {
		Page<Poll> polls = pollRepository.findAll(pageable);
		
		return new ResponseEntity<>(polls, HttpStatus.OK);
	}
	
	@PostMapping("/polls")
	public ResponseEntity<?> createPoll(@Valid @RequestBody Poll poll) {
		poll = pollRepository.save(poll);
		HttpHeaders responseHeaders =  new HttpHeaders();
		
		URI newPoll = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(poll.getPollId())
				.toUri();
		responseHeaders.setLocation(newPoll);
		
		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}
	
	@GetMapping("/polls/{pollId}")
	public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
		return new ResponseEntity<>(verifyPoll(pollId), HttpStatus.OK);
	}
	
	@PutMapping("/polls/{pollId}")
	public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) throws Exception {
		Poll currentPoll = verifyPoll(pollId);
		var newPoll = new Poll();
		BeanUtils.copyProperties(poll, newPoll);
		newPoll.setPollId(currentPoll.getPollId());
		pollRepository.save(newPoll);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/polls/{pollId}")
	public ResponseEntity<?> deletePoll(@PathVariable Long pollId) throws Exception {
		pollRepository.delete(verifyPoll(pollId));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	protected Poll verifyPoll(Long pollId) {
		Optional<Poll> currentPoll = pollRepository.findById(pollId);
		if (!currentPoll.isPresent()) {
			throw new ResourceNotFoundException("Enquete com id " + pollId + " não encontrado");
		}
		return currentPoll.get();
	}

}
