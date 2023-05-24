package com.poll.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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

import com.poll.model.Option;
import com.poll.model.Poll;
import com.poll.repository.PollRepository;

@RestController
@RequestMapping("/polls")
public class PollController 
{
	@Autowired
	private PollRepository pollRepository;
	
	@GetMapping("/")
	public ResponseEntity<List<Poll>> getAllPolls()
	{
		return new ResponseEntity<List<Poll>>(pollRepository.findAll(), HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createPoll(@RequestBody Poll poll)
	{
		for(Option option : poll.getOptions())
		{
			option.setPoll(poll);
		}
		pollRepository.save(poll);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
								   .path("/{id}")
								   .buildAndExpand(poll.getId())
								   .toUri();
		
		responseHeaders.setLocation(uri);
		
		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{pollId}")
	public ResponseEntity<Poll> getPoll(@PathVariable("pollId") long pollId)
	{
		Optional<Poll> optional = pollRepository.findById(pollId);
		Poll poll = optional.get();
		
		System.out.println("\n\n\n\n" + poll + "\n\n\n\n");
		return new ResponseEntity<Poll>(poll, HttpStatus.OK);
		
	}
	
	
	@DeleteMapping("/{pollId}")
	public ResponseEntity<?> deletePoll(@PathVariable("pollId") long pollId)
	{
		pollRepository.deleteById(pollId);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/{pollId}")
	public ResponseEntity<Poll> updatePoll(@PathVariable("pollId") long pollId, @RequestBody Poll poll)
	{
		Poll updatedPoll = pollRepository.save(poll);
		
		return new ResponseEntity<Poll>(updatedPoll, HttpStatus.OK);
	}
	
	
	

}
