package com.poll.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poll.dto.VoteResult;
import com.poll.model.Option;
import com.poll.model.Poll;
import com.poll.model.Vote;
import com.poll.repository.PollRepository;
import com.poll.repository.VoteRepository;

@RestController
@RequestMapping(value = "/result/{id}")
public class ResultController 
{
	@Autowired
	private VoteRepository voteRepository;
	
	@Autowired
	private PollRepository pollRepository;
	
	@GetMapping("/")
	public ResponseEntity<?> getResult(@PathVariable("id") long pollId)
	{
		Poll poll = pollRepository.findById(pollId).orElse(null);
		
		List<Vote> votes = voteRepository.findAllByOptionInAndOption_Poll(poll.getOptions(), poll);
		
		Map<Long, VoteResult> result = new HashMap<>();
		
		for(Option o : poll.getOptions())
		{
			result.put(o.getId(), new VoteResult(o));
		}
		
		for(Vote v : votes)
		{
			VoteResult voteResult = result.get(v.getOption().getId());
			if(voteResult != null)
			{
				voteResult.updateCount();
			}
		}
		
		
		return new ResponseEntity<>(result.values(), HttpStatus.OK);
		
	}
}
