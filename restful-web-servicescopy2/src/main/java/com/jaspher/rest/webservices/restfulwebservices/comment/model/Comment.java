package com.jaspher.rest.webservices.restfulwebservices.comment.model;

import java.security.KeyStore.PrivateKeyEntry;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy.TodoContainer;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	
	@ManyToOne
	@JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	private String content;
	
	@CreationTimestamp
	private LocalDateTime createdAt;

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Todo getTodo() {
		return todo;
	}

	public void setTodo(Todo todo) {
		this.todo = todo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}