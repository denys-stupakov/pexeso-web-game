package sk.tuke.kp.kpi.pexeso.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sk.tuke.kp.kpi.pexeso.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceJDBC implements ScoreService {
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    private static final String CREATE = "drop table score; CREATE TABLE score (player VARCHAR(64) NOT NULL, game VARCHAR(64) NOT NULL, points INTEGER NOT NULL, playedOn TIMESTAMP NOT NULL)";
    public static final String SELECT = "SELECT player, game, points, playedOn FROM score WHERE game = ? ORDER BY points DESC LIMIT 10";
    public static final String DELETE = "DELETE FROM score";
    public static final String INSERT = "INSERT INTO score (player, game, points, playedOn) VALUES (?, ?, ?, ?)";

    private static ScoreServiceJDBC lastScoreServiceJDBC = null;

    public static synchronized ScoreServiceJDBC getInstance()
    {
        if (lastScoreServiceJDBC == null)
            lastScoreServiceJDBC = new ScoreServiceJDBC();

        return lastScoreServiceJDBC;
    }

    public ScoreServiceJDBC() {};
    
    @Override
    public void addScore(Score score) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, score.getPlayer());
            statement.setString(2, score.getGame());
            statement.setInt(3, score.getPoints());
            statement.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("Problem inserting score", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Score> scores = new ArrayList<>();
                while (rs.next()) {
                    scores.add(new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return scores;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting score", e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting score", e);
        }
    }
}