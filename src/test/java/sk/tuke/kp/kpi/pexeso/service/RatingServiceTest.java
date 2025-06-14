package sk.tuke.kp.kpi.pexeso.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.kp.kpi.pexeso.entity.Rating;

import java.util.Date;

import static java.lang.Math.*;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.average;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RatingServiceTestJPA {
    @Autowired
    @Qualifier("RatingServiceJPA")
    private RatingService ratingService;

    @Test
    void setRating() {
    }

    @Test
    void getAverageRating() {
        ratingService.reset();
        ratingService.setRating(new Rating("1n", "pexeso", 25, new Date()));
        ratingService.setRating(new Rating("2n", "pexeso", 7, new Date()));
        ratingService.setRating(new Rating("3n", "pexeso", 13, new Date()));
        int rt = ratingService.getAverageRating("pexeso");
        assertEquals((25+7+13)/3, rt);
    }

    @Test
    void getRating() {
        ratingService.reset();
        ratingService.setRating(new Rating("denys", "pexeso", 15, new Date()));
        int rt = ratingService.getRating("denys", "pexeso");
        assertEquals(15, rt);
    }

    @Test
    void reset() {
        ratingService.reset();
        ratingService.setRating(new Rating("denys", "pexeso", 5, new Date()));
        int rt = ratingService.getRating("denys", "pexeso");
        assertEquals(5, rt);
    }
}