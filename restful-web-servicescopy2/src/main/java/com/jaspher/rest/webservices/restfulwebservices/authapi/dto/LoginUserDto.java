package com.jaspher.rest.webservices.restfulwebservices.authapi.dto;

public class LoginUserDto {
    private String email;
    
    private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginUserDto [email=" + email + ", password=" + password + "]";
	}
    
    // getters and setters here...
    
    
}