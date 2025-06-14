package sk.tuke.kp.kpi.pexeso.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.kp.kpi.pexeso.entity.Rating;
import sk.tuke.kp.kpi.pexeso.entity.Score;

import java.util.List;

@Transactional
@Service("RatingServiceJPA")
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            Double avg = (Double) entityManager
                    .createNamedQuery("Rating.getAverageRating")
                    .setParameter("game", game)
                    .getSingleResult();
            return avg != null ? (int) Math.round(avg) : 0;
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public int getRating(String player, String game) {
        Rating rating = (Rating) entityManager
                .createNamedQuery("Rating.getRating")
                .setParameter("game", game)
                .setParameter("player", player)
                .getSingleResult();

        return rating.getRating();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Rating.resetRating").executeUpdate();
    }
}
