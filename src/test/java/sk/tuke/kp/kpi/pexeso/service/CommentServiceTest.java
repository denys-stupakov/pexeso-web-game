package sk.tuke.kp.kpi.pexeso.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.kp.kpi.pexeso.entity.Comment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    @Qualifier("CommentServiceJPA")
    private CommentService commentService;

    @Test
    void addComment() {
        commentService.reset();
        commentService.addComment(new Comment("denys", "mines", "aces", new Date()));
        List<Comment> comments = commentService.getComments("denys", "mines");
        assertEquals(1, comments.size());
        assertEquals("aces", comments.get(0).getComment());
    }

    @Test
    void getComments() {
        commentService.reset();
        for(int i = 0; i < 5; i++) commentService.addComment(new Comment("denys", "mines", "get multiple comments test" + i, new Date()));
        List<Comment> comments = commentService.getComments("denys", "mines");
        assertEquals(5, comments.size());
    }

    @Test
    void reset() {
        commentService.addComment(new Comment("denys", "mines", "reset test", new Date()));
        commentService.reset();
        List<Comment> comments = commentService.getComments("denys", "mines");
        assertEquals(0, comments.size());
    }
}