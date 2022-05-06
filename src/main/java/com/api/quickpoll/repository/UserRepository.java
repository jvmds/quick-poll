package com.api.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;

import com.api.quickpoll.domain.User;

public interface UserRepository  extends CrudRepository<User, Long> {
	
	public User findByUsername(String username);

}
