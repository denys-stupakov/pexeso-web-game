package sk.tuke.kp.kpi.pexeso.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kp.kpi.pexeso.entity.Comment;
import sk.tuke.kp.kpi.pexeso.entity.Score;
import sk.tuke.kp.kpi.pexeso.service.CommentService;
import sk.tuke.kp.kpi.pexeso.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{game}/{player}")
    public List<Comment> getComments(@PathVariable String game, @PathVariable String player) {
        return commentService.getComments(player, game);
    }

    @PostMapping
    public void addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
    }
}