package com.jaspher.rest.webservices.restfulwebservices.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaspher.rest.webservices.restfulwebservices.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTodo_Id(Integer todoId);
}
