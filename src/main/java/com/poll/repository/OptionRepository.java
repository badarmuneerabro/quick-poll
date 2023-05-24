package com.poll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poll.model.Option;

public interface OptionRepository extends JpaRepository<Option, Long>
{

}
