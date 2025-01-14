package com.jaspher.rest.webservices.restfulwebservices.todo.accesslog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaspher.rest.webservices.restfulwebservices.accesslog.model.TodoAccessLog;
import com.jaspher.rest.webservices.restfulwebservices.sharablelink.model.SharableLink;
import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy.TodoContainer;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

import java.util.Optional;

@Repository
public interface TodoAccessLogRepository extends JpaRepository<TodoAccessLog, Long> {
	//TODO: Fetch logs by the owner of the todos
	//TODO: Fetch logs by the user who accessed the todos
	//TODO: Fetch logs by the owner of the todos

	
    List<TodoAccessLog> findByTodo_Username(String username);

	List<TodoAccessLog> findByAccessedBy(String accessedBy);

	List<TodoAccessLog> findByTodo_Id(Long todoId); //first enter todo entity then go to its id
	
    List<TodoAccessLog> findByActionType(String actionType);
    
    List<TodoAccessLog> findByTodo_Id(Integer todoId);
    List<TodoAccessLog> deleteByTodo_Id(Integer todoId);
    
    
    
    
    List<TodoAccessLog> findByTodo_UsernameAndTodo_TodoContainer_Id(String username, Long containerId);
    List<TodoAccessLog> findByAccessedByAndTodo_TodoContainer_Id(String accessedBy, Long containerId);
    List<TodoAccessLog> findByActionTypeAndTodo_TodoContainer_Id(String actionType, Long containerId);
    

    List<TodoAccessLog> findByTodo_TodoContainer_Id(Long containerId);

    List<TodoAccessLog> findByContainerIdd(Long containerId);

    @Modifying
    @Transactional
    void deleteByTodo(Todo todo);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM TodoAccessLog tal WHERE tal.todo.id = :todoId")
    void deleteAccessLogsByTodoId(@Param("todoId") int todoId);
}