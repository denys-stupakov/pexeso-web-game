package sk.tuke.kp.kpi.pexeso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kp.kpi.pexeso.entity.Rating;
import sk.tuke.kp.kpi.pexeso.entity.Score;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

@Service
@Component
public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String INSERT = "INSERT INTO rating (player, game, rating, ratedon) VALUES (?, ?, ?, ?) " +
            "ON CONFLICT (game, player) DO UPDATE SET rating = EXCLUDED.rating, ratedOn = EXCLUDED.ratedOn";
    public static final String SELECT_AVG = "SELECT ROUND(AVG(rating)) FROM rating WHERE game = ?";
    public static final String SELECT_PLAYER = "SELECT rating FROM rating WHERE player = ? AND game = ?";
    public static final String DELETE = "DELETE FROM rating";
    private static RatingServiceJDBC lastRatingServiceJDBC = null;

    public static synchronized RatingServiceJDBC getInstance()
    {
        if (lastRatingServiceJDBC == null)
            lastRatingServiceJDBC = new RatingServiceJDBC();

        return lastRatingServiceJDBC;
    }

    private RatingServiceJDBC() {};

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, rating.getPlayer());
            statement.setString(2, rating.getGame());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Problem inserting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_AVG)) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0; // Ak hra nemá hodnotenie, vráti 0
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem selecting average rating", e);
        }
    }

    @Override
    public int getRating(String player, String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER)) {
            statement.setString(1, player);
            statement.setString(2, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0; // Ak hráč nemá hodnotenie, vráti 0
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem selecting player rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting ratings", e);
        }
    }

    @RestController
    public static class ScoreServiceRestClient implements ScoreService {
        private final String url = "http://localhost:8080/api/score";

        @Autowired
        private RestTemplate restTemplate;
        //private RestTemplate restTemplate = new RestTemplate();

        @Override
        public void addScore(Score score) {
            restTemplate.postForEntity(url, score, Score.class);
        }

        @Override
        public List<Score> getTopScores(String gameName) {
            return Arrays.asList(restTemplate.getForEntity(url + "/" + gameName, Score[].class).getBody());
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException("Not supported via web service");
        }
    }
}