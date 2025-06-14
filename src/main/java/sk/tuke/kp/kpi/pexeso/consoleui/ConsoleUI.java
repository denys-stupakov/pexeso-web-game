package sk.tuke.kp.kpi.pexeso.consoleui;

import sk.tuke.kp.kpi.pexeso.command.*;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.kp.kpi.pexeso.core.Field;
import sk.tuke.kp.kpi.pexeso.core.FieldState;
import sk.tuke.kp.kpi.pexeso.core.TileState;
import sk.tuke.kp.kpi.pexeso.entity.Comment;
import sk.tuke.kp.kpi.pexeso.entity.Rating;
import sk.tuke.kp.kpi.pexeso.entity.Score;
import sk.tuke.kp.kpi.pexeso.service.CommentService;
import sk.tuke.kp.kpi.pexeso.service.RatingService;
import sk.tuke.kp.kpi.pexeso.service.ScoreService;
import java.util.Map;
import java.util.HashMap;

import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;


public class ConsoleUI {
    private static Pattern INPUT_PATTERN = null;
    private final Field field;
    private Scanner scanner = new Scanner(System.in);

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    public ConsoleUI(Field field) {
        this.field = field;
        this.INPUT_PATTERN = Pattern.compile("([OP])([A-D])([0-5])");
    }

    private Map<Integer, Command> commands = new HashMap<>();

    private void initializeCommands() {
        commands.put(1, new PlayGameCommand(this));
        commands.put(2, new AddCommentCommand(this, commentService, scanner));
        commands.put(3, new ReadCommentsCommand(this, commentService, scanner));
        commands.put(4, new GetTopScoresCommand(this, scoreService));
        commands.put(5, new RateGameCommand(this, ratingService, scanner));
        commands.put(6, new ExitGameCommand());
    }

    public void menu() {
        initializeCommands();
        int step = 0;
        var prompt = "\033[0m\n---------------------------------------\n" +
                "\t\t Menu\n" +
                "- play again (1)\n" +
                "- comment (2)\n" +
                "- read comments (3)\n" +
                "- get top scores (4)\n" +
                "- rate game (5)\n" +
                "- exit game (6)\n\n";
        while (true) {
            step++;
            if(step < 2)
                System.out.print(prompt + " $ ");

            if (!scanner.hasNextInt()) {
                System.out.println(prompt);
                System.out.print(prompt + "\033[31m" + "Invalid input. Please enter a number (1-6). $");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            Command command = commands.get(choice);
            if (command != null) {
                command.execute();
            } else {
                System.out.print(prompt + "\033[31m" + "Invalid choice. Please enter a number between 1 and 6." + "\033[0m" + " $ ");
            }
        }
    }

    public void play() {
        scoreService.addScore(new Score(System.getProperty("user.name"), "pexeso", field.getScore(), new Date()));
        do {
            this.field.setFieldState(FieldState.SOLVED);
            System.out.println();
            printField();
            System.out.println();
            processInput();
            if (this.field.checkVictory()) this.field.setFieldState(FieldState.SOLVED);
        } while (field.getFieldState() == FieldState.PLAYING);

        if (this.field.getFieldState() == FieldState.SOLVED) {
            printField();
            System.out.println("You Won!");
            saveScore();
        }
    }

    private void printField() {
        printColumnHeader();
        for (int row = 0; row < this.field.getRowCount(); row++) {
            System.out.printf("%c| ", 'A' + row); // Print row index with a fixed width
            for (int column = 0; column < this.field.getColumnCount(); column++) {
                if (this.field.getTile(row, column).getState() == TileState.CLOSED) {
                    System.out.print("  _ ");
                } else {
                    String imagePath = this.field.getTile(row, column).getImagePath().replace(".png", "");
                    System.out.printf("%3s ", imagePath); // Ensures at least 3-character width
                }
            }
            System.out.println();
        }
    }

    private void printColumnHeader() {
        System.out.print("  ");
        for (int column = 1; column < this.field.getColumnCount() + 1; column++) {
            System.out.print("   " + column);
        }
        System.out.println();
        for (int column = 0; column < this.field.getColumnCount() * 4 + 2; column++) {
            System.out.print("_");
        }
        System.out.println();
    }

    private void processInput() {
        System.out.print("Enter command (X - exit, OA1 - open tile)" + " $ ");
        var line = scanner.nextLine().toUpperCase();
        if ("X".equals(line)) {
            System.exit(0);
        }

        var matcher = INPUT_PATTERN.matcher(line);
        if (matcher.matches()) {
            int row = matcher.group(2).charAt(0) - 'A';
            int column = Integer.parseInt(matcher.group(3)) - 1;
            if ("O".equals(matcher.group(1))) field.openTile(row, column);
        } else {
            System.out.println("\n\033[31m" + "Wrong input" + "\033[0m");
        }
    }

    private void saveScore() {
        scoreService.addScore(
                new Score(System.getProperty("user.name"), "pexeso", field.getRowCount() * field.getColumnCount() / 2 - field.getScore(), new Date()));
    }

}
