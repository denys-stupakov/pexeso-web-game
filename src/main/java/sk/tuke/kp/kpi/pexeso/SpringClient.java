package sk.tuke.kp.kpi.pexeso;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kp.kpi.pexeso.consoleui.ConsoleUI;
import sk.tuke.kp.kpi.pexeso.core.Field;
import sk.tuke.kp.kpi.pexeso.core.LevelDifficulty;
import sk.tuke.kp.kpi.pexeso.service.*;

import java.util.Scanner;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI console) {
        return s -> console.menu();
    }

    @Bean
    public ConsoleUI console() {
        return new ConsoleUI(new Field(LevelDifficulty.EASY));
    }

    @Bean
    public ScoreService scoreService() { return new ScoreServiceRestClient();}

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }

    @Bean
    public CommentService commentService() { return new CommentServiceJPA(); }

    @Bean
    public RatingService ratingService() { return new RatingServiceJPA(); }
}