package com.poll.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poll.model.Option;
import com.poll.model.Poll;
import com.poll.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> 
{
	
	@Query(value = "SELECT v.* from Option o, Vote v WHERE o.POLL_ID = ?1 AND v.OPTION_ID=o.OPTION_ID", nativeQuery = true)
	List<Vote> findByPoll(long pollId);
	
	List<Vote> findAllByOptionInAndOption_Poll(Set<Option> options, Poll poll);
}
