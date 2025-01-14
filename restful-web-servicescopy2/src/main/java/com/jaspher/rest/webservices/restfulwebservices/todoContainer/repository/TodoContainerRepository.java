package com.jaspher.rest.webservices.restfulwebservices.todoContainer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy.TodoContainer;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

import java.util.Optional;

@Repository
public interface TodoContainerRepository extends CrudRepository<TodoContainer, Long> {
    List<TodoContainer> findByUsername(String fullName);

}