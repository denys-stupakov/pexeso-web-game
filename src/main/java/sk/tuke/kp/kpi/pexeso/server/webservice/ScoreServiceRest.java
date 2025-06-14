package sk.tuke.kp.kpi.pexeso.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kp.kpi.pexeso.entity.Score;
import sk.tuke.kp.kpi.pexeso.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {
    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) {
        return scoreService.getTopScores(game);
    }

    @PostMapping
    public void addRating(@RequestBody Score score) {
        scoreService.addScore(score);
    }
}