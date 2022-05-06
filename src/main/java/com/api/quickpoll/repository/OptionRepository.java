package com.api.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;

import com.api.quickpoll.domain.Option;

public interface OptionRepository extends CrudRepository<Option, Long> {

}
