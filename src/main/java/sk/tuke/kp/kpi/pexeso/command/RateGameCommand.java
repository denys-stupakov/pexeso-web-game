package sk.tuke.kp.kpi.pexeso.command;

import sk.tuke.kp.kpi.pexeso.consoleui.ConsoleUI;
import sk.tuke.kp.kpi.pexeso.entity.Rating;
import sk.tuke.kp.kpi.pexeso.service.RatingService;

import java.util.Date;
import java.util.Scanner;

public class RateGameCommand implements Command {
    private RatingService ratingService;
    private Scanner scanner;
    private ConsoleUI consoleUI;

    public RateGameCommand(ConsoleUI consoleUI, RatingService ratingService, Scanner scanner) {
        this.ratingService = ratingService;
        this.scanner = scanner;
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        int rating;
        int step = 0;
        do {
            step++;
            if(step > 1) System.out.print("\033[31mYour rating (1-10) \033[0m$ ");
            else System.out.print("Your rating (1-10) $ ");
            while (!scanner.hasNextInt()) {
                System.out.print("\033[31mInvalid input. Enter a number between 1-10 \033[0m$ ");
                scanner.next();
            }
            rating = scanner.nextInt();
            scanner.nextLine();
        } while (rating < 1 || rating > 10);

        ratingService.setRating(new Rating(System.getProperty("user.name"), "pexeso",   rating, new Date()));
        consoleUI.menu();
    }
}