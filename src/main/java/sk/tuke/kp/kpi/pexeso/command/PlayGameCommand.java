package sk.tuke.kp.kpi.pexeso.command;

import sk.tuke.kp.kpi.pexeso.consoleui.ConsoleUI;

public class PlayGameCommand implements Command {
    private ConsoleUI consoleUI;

    public PlayGameCommand(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        consoleUI.play();
        consoleUI.menu();
    }
}