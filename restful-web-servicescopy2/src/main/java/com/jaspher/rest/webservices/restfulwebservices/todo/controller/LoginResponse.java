package com.jaspher.rest.webservices.restfulwebservices.todo.controller;

public class LoginResponse {
    private String token;

    private long expiresIn;
    
    private String username;

    public String getToken() {
        return token;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public void setToken(String token) {
		this.token = token;
	}

 // Getters and setters...
    
    
}