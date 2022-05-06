package com.api.quickpoll.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.quickpoll.domain.Poll;

public interface PollRepository extends PagingAndSortingRepository<Poll, Long> {

}
