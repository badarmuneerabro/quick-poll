package com.poll.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.poll.model.Poll;
import com.poll.model.Vote;
import com.poll.repository.PollRepository;
import com.poll.repository.VoteRepository;

@RestController
@RequestMapping(value = "/polls/{id}/votes")
public class VoteController 
{
	@Autowired
	private VoteRepository voteRepository;
	
	@Autowired
	private PollRepository pollRepository;
	
	@PostMapping("/")
	public ResponseEntity<?> createVote(@RequestBody Vote vote)
	{
		voteRepository.save(vote);
		
		HttpHeaders headers = new HttpHeaders();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(vote.getId()).toUri();
		
		headers.setLocation(uri);
		
		return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Vote>> getAllVotesForPoll(@PathVariable("id") long pollId)
	{
		Optional<Poll> optional = pollRepository.findById(pollId);
		Poll poll = optional.orElse(null);
		
		
		System.out.println("\n\n\nPoll ID:" + pollId);
		return new ResponseEntity<List<Vote>>(voteRepository.findAllByOptionInAndOption_Poll(poll.getOptions(), poll), HttpStatus.OK);
	}
	
	
}
