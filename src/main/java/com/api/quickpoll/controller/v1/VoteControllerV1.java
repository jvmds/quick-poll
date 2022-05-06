package com.api.quickpoll.controller.v1;

import java.net.URI;

import javax.inject.Inject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.quickpoll.domain.Vote;
import com.api.quickpoll.repository.VoteRepository;

@RestController
@RequestMapping("/v1")
public class VoteControllerV1 {
	
	@Inject
	private VoteRepository voteRepository;
	
	@PostMapping("/polls/{pollId}/votes")
	public ResponseEntity<?> createVote(@RequestBody Vote vote, @PathVariable Long pollId) {
		vote = voteRepository.save(vote);
		HttpHeaders newHeaders = new HttpHeaders();
		URI newUri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(vote.getId())
				.toUri();
		newHeaders.setLocation(newUri);
		return new ResponseEntity<>(null, newHeaders, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/polls/{pollId}/votes")
	public Iterable<Vote> getVote(@PathVariable Long pollId) {
		return voteRepository.findByPoll(pollId);
	}
}
