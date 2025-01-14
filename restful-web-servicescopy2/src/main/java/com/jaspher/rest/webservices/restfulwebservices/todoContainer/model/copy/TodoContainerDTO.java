package com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy;

import java.util.List;

import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;

public class TodoContainerDTO {
	private Long containerId;
    private String containerTitle;
    private List<Todo> todos;
	private String ownerOfTodo;

    // Constructors
    public TodoContainerDTO(String containerTitle, List<Todo> todos, String ownerOfTodo) {
        this.containerTitle = containerTitle;
        this.todos = todos;
        this.ownerOfTodo = ownerOfTodo;
    }
    
    

    public Long getContainerId() {
		return containerId;
	}



	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}



	// Getters and Setters
    public String getContainerTitle() {
        return containerTitle;
    }

    public String getOwnerOfTodo() {
		return ownerOfTodo;
	}

	public void setOwnerOfTodo(String ownerOfTodo) {
		this.ownerOfTodo = ownerOfTodo;
	}

	public void setContainerTitle(String containerTitle) {
        this.containerTitle = containerTitle;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }
}