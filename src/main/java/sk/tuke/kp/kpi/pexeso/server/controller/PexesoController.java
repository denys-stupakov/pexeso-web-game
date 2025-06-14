package sk.tuke.kp.kpi.pexeso.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kp.kpi.pexeso.core.*;
import sk.tuke.kp.kpi.pexeso.entity.Comment;
import sk.tuke.kp.kpi.pexeso.entity.Rating;
import sk.tuke.kp.kpi.pexeso.entity.Score;
import sk.tuke.kp.kpi.pexeso.service.CommentService;
import sk.tuke.kp.kpi.pexeso.service.RatingService;
import sk.tuke.kp.kpi.pexeso.service.ScoreService;

import java.util.Date;

@Controller
@RequestMapping("/pexeso")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PexesoController {
    private Field field = new Field(LevelDifficulty.MEDIUM);

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserController userController;

    private int reveal = 2;
    private boolean scoreAlreadySaved = false;

    @GetMapping("/new")
    public String newGame(Model model) {
        field = new Field(LevelDifficulty.MEDIUM);
        reveal = 2;
        scoreAlreadySaved = false;
        prepareModel(model);
        return "pexeso";
    }

    @GetMapping
    public String handleClick(@RequestParam int row,
                              @RequestParam int column,
                              Model model,
                              HttpServletRequest request) {

        field.openTile(row, column);

        if (field.checkVictory() && userController.isLogged() && !scoreAlreadySaved) {
            scoreService.addScore(new Score(
                    userController.getLoggedUser().getLogin(),
                    "pexeso",
                    field.getCountPairedTiles() - field.getScore(),
                    new Date()
            ));
            scoreAlreadySaved = true;
        }

        prepareModel(model);

        if ("true".equals(request.getHeader("HX-Request"))) {
            return "fragments/fragments :: field";
        }

        return "pexeso";
    }

    @GetMapping("/reveal")
    public String revealAll(Model model) {
        if (reveal > 0) {
            field.revealAllTemporarily();
            reveal--;
        }
        prepareModel(model);
        return "fragments/fragments :: field";
    }

    @GetMapping("/hide")
    public String hideRevealed(Model model) {
        field.hideUnpairedTiles();
        prepareModel(model);
        return "fragments/fragments :: field";
    }

    private void prepareModel(Model model) {
        model.addAttribute("field", field);
        model.addAttribute("victory", field.getFieldState() == FieldState.SOLVED);
        model.addAttribute("lives", field.getLives());
        model.addAttribute("gameOver", field.isGameOver());
        model.addAttribute("reveal", reveal);

        if (userController.isLogged()) {
            model.addAttribute("user", userController.getLoggedUser());
        }
    }

    @GetMapping("/scores")
    public String showScores(Model model) {
        model.addAttribute("scores", scoreService.getTopScores("pexeso"));
        return "scores";
    }

    @GetMapping("/comments")
    public String Comments(Model model) {
        model.addAttribute("comments", commentService.getAllComments("pexeso"));
        return "comments";
    }

    @PostMapping("/comments/post")
    public String postComment(@RequestParam("text") String commentText, Model model) {
        if (userController.isLogged()) {
            commentService.addComment(new Comment(
                    userController.getLoggedUser().getLogin(),
                    "pexeso",
                    commentText,
                    new Date()
            ));
        }
        model.addAttribute("comments", commentService.getAllComments("pexeso"));
        return "comments :: commentListFragment";
    }

    @GetMapping("/ratings")
    public String showRatings(Model model) {
        model.addAttribute("ratings", ratingService.getAverageRating("pexeso"));
        return "ratings";
    }

    @PostMapping("/ratings/post")
    public String postRating(@RequestParam("value") int value, Model model) {
        if (userController.isLogged()) {
            ratingService.setRating(new Rating(
                    userController.getLoggedUser().getLogin(),
                    "pexeso",
                    value,
                    new Date()
            ));
        }
        model.addAttribute("ratings", ratingService.getAverageRating("pexeso"));
        return "ratings :: ratingFragment"; // Only the fragment
    }
}
