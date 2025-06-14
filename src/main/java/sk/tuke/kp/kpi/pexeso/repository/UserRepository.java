package sk.tuke.kp.kpi.pexeso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tuke.kp.kpi.pexeso.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
