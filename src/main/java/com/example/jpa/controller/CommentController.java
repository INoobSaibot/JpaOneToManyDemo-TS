package com.example.jpa.controller;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.Comment;
import com.example.jpa.repository.CommentRepository;
import com.example.jpa.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts/{postId}/comments")
    public Page<Comment> getAllCommentsByPostId(@PathVariable (value = "postId") Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable);
    }

    @PostMapping("/posts/{postId}/comments")
    public Comment createComment(@PathVariable (value="postId") Long postId,
            @Valid @RequestBody Comment comment) {
        return postRepository.findById(postId).map(post-> {
            comment.setPost(post);
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    
}