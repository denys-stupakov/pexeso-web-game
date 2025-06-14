package sk.tuke.kp.kpi.pexeso.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.kp.kpi.pexeso.entity.Comment;
import org.springframework.stereotype.Service;
import sk.tuke.kp.kpi.pexeso.entity.Score;

import java.util.List;

@Transactional
@Service("CommentServiceJPA")
public class CommentServiceJPA implements CommentService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) throws ScoreException {
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String player, String game) throws ScoreException {
        return entityManager.createNamedQuery("Comment.getComments")
                .setParameter("game", game).setParameter("player", player).getResultList();
    }

    @Override
    public List<Comment> getAllComments(String game) throws ScoreException {
        return entityManager.createNamedQuery("Comment.getAllComments")
                .setParameter("game", game).getResultList();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Comment.resetComments").executeUpdate();
        // alebo:
        // entityManager.createNativeQuery("delete from score").executeUpdate();
    }
}
