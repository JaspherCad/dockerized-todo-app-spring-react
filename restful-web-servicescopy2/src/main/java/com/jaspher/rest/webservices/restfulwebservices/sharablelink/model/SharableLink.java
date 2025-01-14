package com.jaspher.rest.webservices.restfulwebservices.sharablelink.model;

import java.security.KeyStore.PrivateKeyEntry;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
public class SharableLink {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id")
    private TodoContainer container; // Link to the TodoContainer
    
    

	public TodoContainer getContainer() {
		return container;
	}

	public void setContainer(TodoContainer container) {
		this.container = container;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	
	
}