package sk.tuke.kp.kpi.pexeso.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kp.kpi.pexeso.entity.Score;
import sk.tuke.kp.kpi.pexeso.service.*;
import java.util.Date;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.kp.kpi.pexeso")
@EnableJpaRepositories(basePackages = "sk.tuke.kp.kpi.pexeso.repository")
public class GameStudioServer {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreServiceS() { return new ScoreServiceJPA(); }

    @Bean
    public CommentService commentServiceS() { return new CommentServiceJPA(); }

    @Bean
    public RatingService ratingServiceS() { return new RatingServiceJPA(); }

    @Bean
    public UserService userService() { return new UserServiceJPA(); }
}