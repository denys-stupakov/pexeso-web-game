package sk.tuke.kp.kpi.pexeso.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.kp.kpi.pexeso.entity.Score;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// Comment @Bean annotation on runner method in SpringClient when running the test, otherwise the UI will start
class ScoreServiceTestJPA {
    @Autowired
    @Qualifier("ScoreServiceJPA")
    private ScoreService scoreService;

    @Test
    void reset() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("mines").size());
    }

    @Test
    void addScore() {
        scoreService.reset();
        scoreService.addScore(new Score("jaro", "mines", 150, new Date()));
        var scores = scoreService.getTopScores("mines");
        assertEquals(1, scores.size());
        var score = scores.get(0);
        assertEquals("jaro", score.getPlayer());
        assertEquals("mines", score.getGame());
        assertEquals(150, score.getPoints());
    }

    @Test
    void getTopScores() {
        scoreService.reset();
        scoreService.addScore(new Score("jaro", "mines", 150, new Date()));
        scoreService.addScore(new Score("fero", "stones", 150, new Date()));
        scoreService.addScore(new Score("mara", "mines", 200, new Date()));
        scoreService.addScore(new Score("jano", "mines", 200, new Date()));
        scoreService.addScore(new Score("zuza", "mines", 50, new Date()));
        var scores = scoreService.getTopScores("mines");
        assertEquals(4, scores.size());
        var score = scores.get(0);
//        assertEquals("jano", score.getPlayer());
        assertEquals("mines", score.getGame());
        assertEquals(200, score.getPoints());

        score = scores.get(1);
//        assertEquals("jano", score.getPlayer());
        assertEquals("mines", score.getGame());
        assertEquals(200, score.getPoints());

        score = scores.get(2);
        assertEquals("jaro", score.getPlayer());
        assertEquals("mines", score.getGame());
        assertEquals(150, score.getPoints());

        score = scores.get(3);
        assertEquals("zuza", score.getPlayer());
        assertEquals("mines", score.getGame());
        assertEquals(50, score.getPoints());
    }
}
