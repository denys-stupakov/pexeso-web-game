package sk.tuke.kp.kpi.pexeso.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kp.kpi.pexeso.entity.Comment;
import sk.tuke.kp.kpi.pexeso.entity.Rating;
import sk.tuke.kp.kpi.pexeso.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/{game}/{player}")
    public int getRatings(@PathVariable String player, @PathVariable String game) {
        return ratingService.getRating(player, game);
    }

    @GetMapping("/{game}/AverageRating")
    public int getAverageRating(@PathVariable String game) {
        return ratingService.getAverageRating(game);
    }

    @PostMapping
    public void addRating(@RequestBody Rating rating) {
        ratingService.setRating(rating);
    }
}
