package io.github.mezk.repository;

import org.springframework.data.repository.CrudRepository;

import io.github.mezk.model.User;

public interface UserRepository extends CrudRepository<User, Integer>{
}
