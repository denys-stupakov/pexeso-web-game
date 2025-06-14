package sk.tuke.kp.kpi.pexeso.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.kp.kpi.pexeso.entity.User;

@Service
@Transactional
public class UserServiceJPA implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public String getPassword(String username) {
        try {
            String query = "SELECT u.password FROM User u WHERE u.login = :username";
            return (String) entityManager.createQuery(query)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int checkLogin(String username) {
        try {
            String query = "SELECT u.login FROM User u WHERE u.login = :username";
            entityManager.createQuery(query)
                    .setParameter("username", username)
                    .getSingleResult();
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void reset() {
        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }
}