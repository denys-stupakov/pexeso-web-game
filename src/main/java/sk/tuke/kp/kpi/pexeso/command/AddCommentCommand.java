package sk.tuke.kp.kpi.pexeso.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.tuke.kp.kpi.pexeso.consoleui.ConsoleUI;
import sk.tuke.kp.kpi.pexeso.entity.Comment;
import sk.tuke.kp.kpi.pexeso.service.CommentService;

import java.util.Date;
import java.util.Scanner;

public class AddCommentCommand implements Command {
    private CommentService commentService;
    private Scanner scanner;
    private ConsoleUI consoleUI;

    public AddCommentCommand(ConsoleUI consoleUI, CommentService commentService, Scanner scanner) {
        this.commentService = commentService;
        this.scanner = scanner;
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        System.out.print("Add a comment: ");
        String comment = scanner.nextLine();
        commentService.addComment(new Comment(System.getProperty("user.name"), "pexeso", comment, new Date()));

        consoleUI.menu();
    }
}