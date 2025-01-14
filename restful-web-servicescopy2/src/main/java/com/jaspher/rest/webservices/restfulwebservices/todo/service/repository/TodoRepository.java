package com.jaspher.rest.webservices.restfulwebservices.todo.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy.TodoContainer;


public interface TodoRepository extends JpaRepository<Todo, Integer>{
	
	List<Todo> findByUsername(String username);
    List<Todo> findByUserId(Integer userId);
    Optional<Todo> findByIdAndUsername(int id, String username);
    List<Todo> findByTodoContainer(TodoContainer container);

	void deleteByTodoContainer(TodoContainer todoContainer);

}
