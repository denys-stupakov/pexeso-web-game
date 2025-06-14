package sk.tuke.kp.kpi.pexeso.service;

import sk.tuke.kp.kpi.pexeso.entity.Comment;

import java.sql.Timestamp;
import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;

    List<Comment> getComments(String player, String game) throws CommentException;

    void reset() throws CommentException;

    List<Comment> getAllComments(String pexeso);
}