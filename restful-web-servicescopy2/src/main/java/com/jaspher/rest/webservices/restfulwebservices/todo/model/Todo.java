package com.jaspher.rest.webservices.restfulwebservices.todo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jaspher.rest.webservices.restfulwebservices.accesslog.model.TodoAccessLog;
import com.jaspher.rest.webservices.restfulwebservices.comment.model.Comment;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy.TodoContainer;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Todo {

	public Todo() {
		
	}
	
	public Todo(String username, String description, LocalDate targetDate, boolean done) {
		super();
		this.username = username;
		this.description = description;
		this.targetDate = targetDate;
		this.done = done;
	}
//	{
//		ID
//		USERNAME
//		DESCRIPTION
//		TARGETDATE
//		DONE
//	}
	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

	private String username;
	
	private String description;
	
	private LocalDate targetDate;
	private boolean done;
	
	 @ManyToOne
	 @JoinColumn(name = "container_id")
	 @JsonIgnore // Prevents infinite recursion
	private TodoContainer todoContainer;
	 
	 
	 @JsonIgnore // Prevents infinite recursion
	 @OneToMany(mappedBy = "todo", cascade = CascadeType.PERSIST, orphanRemoval = false)
	    private List<TodoAccessLog> accessLogs = new ArrayList<>();
	 //ah, if we dleted todo, it will not remove the accessLogs
	 //
	 
	 
	 @JsonIgnore
	 @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
	 private List<Comment> comments = new ArrayList<Comment>();

	public int getId() {
		return id;	
	}
	

	public TodoContainer getTodoContainer() {
		return todoContainer;
	}

	
	

	public List<TodoAccessLog> getAccessLogs() {
		return accessLogs;
	}

	public void setAccessLogs(List<TodoAccessLog> accessLogs) {
		this.accessLogs = accessLogs;
	}

	public void setTodoContainer(TodoContainer todoContainer) {
		this.todoContainer = todoContainer;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	
	
	
	@Override
	public String toString() {
		return "Todo [id=" + id + ", username=" + username + ", description=" + description + ", targetDate="
				+ targetDate + ", done=" + done + "]";
	}

}