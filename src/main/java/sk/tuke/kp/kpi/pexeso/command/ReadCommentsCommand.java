package sk.tuke.kp.kpi.pexeso.command;

import sk.tuke.kp.kpi.pexeso.consoleui.ConsoleUI;
import sk.tuke.kp.kpi.pexeso.entity.Comment;
import sk.tuke.kp.kpi.pexeso.service.CommentService;

import java.util.List;
import java.util.Scanner;

public class ReadCommentsCommand implements Command {
    private CommentService commentService;
    private Scanner scanner;
    private ConsoleUI consoleUI;

    public ReadCommentsCommand(ConsoleUI consoleUI, CommentService commentService, Scanner scanner) {
        this.commentService = commentService;
        this.scanner = scanner;
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        System.out.print("From player $ ");
        String player = scanner.nextLine().strip();

        var comments = commentService.getComments(player, "pexeso");
        if (comments.isEmpty()) {
            System.out.println("\n\033[31mNo comments found");
        }
        for (Comment comment : comments) {
            System.out.println("\033[1;33m" + comment.getComment() +
                    " \033[0mby \033[1;34m" + comment.getPlayer() + " \033[0min" +
                    " \033[1;32m" + comment.getGame() + " \033[0mon " +
                    "\033[1;35m" + comment.getCommentedOn());
        }

        consoleUI.menu();
    }
}