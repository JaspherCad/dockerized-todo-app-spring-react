package com.jaspher.rest.webservices.restfulwebservices.todo.model;

public class TodoCustomResponse {

	
	private Todo todo;
	private int containerId;
	private String ownerOfTodo;
	
	public TodoCustomResponse(Todo todo, int containerId, String ownerOfTodo) {
		this.todo=todo;
		this.containerId=containerId;
		this.ownerOfTodo = ownerOfTodo;
	}

	public Todo getTodo() {
		return todo;
	}
	
	

	public String getOwnerOfTodo() {
		return ownerOfTodo;
	}

	public void setOwnerOfTodo(String ownerOfTodo) {
		this.ownerOfTodo = ownerOfTodo;
	}

	public void setTodo(Todo todo) {
		this.todo = todo;
	}

	public int getContainerId() {
		return containerId;
	}

	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}
	
	
	
}
