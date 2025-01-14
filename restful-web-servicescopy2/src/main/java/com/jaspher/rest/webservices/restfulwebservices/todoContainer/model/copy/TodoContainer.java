package com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jaspher.rest.webservices.restfulwebservices.sharablelink.model.SharableLink;
import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class TodoContainer {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;  // The title of the TODO container

    private String username;  // The owner of the TODO container

    @OneToMany(mappedBy = "todoContainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos = new ArrayList<>();

    
    @OneToMany(mappedBy = "container", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharableLink> sharableLinks = new ArrayList<>(); // Links to share this container
    //from sharable link, we mapped it by using the class of TodoContainer container;
    
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Todo> getTodos() {
		return todos;
	}

	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}
    
    
    
    
    
    

}