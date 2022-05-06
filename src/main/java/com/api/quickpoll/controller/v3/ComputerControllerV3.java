package com.api.quickpoll.controller.v3;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.quickpoll.domain.Vote;
import com.api.quickpoll.dto.OptionCount;
import com.api.quickpoll.dto.VoteResult;
import com.api.quickpoll.repository.VoteRepository;

@RestController
@RequestMapping("/v3")
public class ComputerControllerV3 {
	
	@Inject
	VoteRepository voteRepository;
	
	@GetMapping("/comptesult")
	public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
		VoteResult voteResult = new VoteResult();
		int totaVotes = 0;
		Iterable<Vote> allVotes = voteRepository.findByPoll(pollId);
		Map<Long, OptionCount> tempMap = new HashMap<Long, OptionCount>();
		
		for (var v : allVotes) {
			totaVotes++;
			if (!tempMap.containsKey(v.getOption().getId())) {
				OptionCount optionCount = new OptionCount();
				optionCount.setOptionId(v.getOption().getId());
				tempMap.put(v.getOption().getId(), optionCount);
			}
			tempMap.get(v.getOption().getId()).setCount(tempMap.get(v.getOption().getId()).getCount() + 1);
		}
		voteResult.setTotalVotes(totaVotes);
		voteResult.setResults(tempMap.values());
		
		return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
		
	}
}
