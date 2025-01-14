package com.jaspher.rest.webservices.restfulwebservices.accesslog.model;

import java.time.LocalDateTime;

import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TodoAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;  // The specific Todo being accessed

    private Integer todoIdd;
    private Long containerIdd;
    private String accessedBy;  // The user who accessed the Todo

    private LocalDateTime accessedAt;  // The timestamp of access

    private String actionType;  // The type of action (VIEW, UPDATE, DELETE, etc.)

    private String details;  // Optional: Additional details about the action

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public Long getContainerIdd() {
		return containerIdd;
	}

	public void setContainerIdd(Long containerIdd) {
		this.containerIdd = containerIdd;
	}

	public Integer getTodoIdd() {
		return todoIdd;
	}

	public void setTodoIdd(Integer todoIdd) {
		this.todoIdd = todoIdd;
	}

	public Todo getTodo() {
		return todo;
	}

	public void setTodo(Todo todo) {
		this.todo = todo;
	}

	public String getAccessedBy() {
		return accessedBy;
	}

	public void setAccessedBy(String accessedBy) {
		this.accessedBy = accessedBy;
	}

	public LocalDateTime getAccessedAt() {
		return accessedAt;
	}

	public void setAccessedAt(LocalDateTime accessedAt) {
		this.accessedAt = accessedAt;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

    // Getters and Setters
    
}