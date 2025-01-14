package com.jaspher.rest.webservices.restfulwebservices.todo.sharablelink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jaspher.rest.webservices.restfulwebservices.sharablelink.model.SharableLink;
import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy.TodoContainer;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

import java.util.Optional;

@Repository
public interface SharableLinkRepository extends CrudRepository<SharableLink, Integer> {
    Optional<SharableLink> findByToken(String token);
    void  deleteByContainer(TodoContainer container); //delete all which have this containerID


}