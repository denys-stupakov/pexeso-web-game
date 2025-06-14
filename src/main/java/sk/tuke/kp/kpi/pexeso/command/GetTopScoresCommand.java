package sk.tuke.kp.kpi.pexeso.command;

import sk.tuke.kp.kpi.pexeso.consoleui.ConsoleUI;
import sk.tuke.kp.kpi.pexeso.entity.Score;
import sk.tuke.kp.kpi.pexeso.service.ScoreService;

public class GetTopScoresCommand implements Command {
    private ScoreService scoreService;
    private ConsoleUI consoleUI;

    public GetTopScoresCommand(ConsoleUI consoleUI, ScoreService scoreService) {
        this.scoreService = scoreService;
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        var scores = scoreService.getTopScores("pexeso");

        if(scores.isEmpty()) {
            System.out.println("\n\033[31mNo scores found\033[0m");
        }

        System.out.println();

        for (Score score : scores) {
            System.out.println("\033[1;34m" + score.getPlayer() + " \033[0mwith" +
                    " \033[1;33m" + score.getPoints() +
                    "\033[0m" + " points " + "in" +
                    " \033[1;32m" + score.getGame() + " \033[0mon " +
                    "\033[1;35m" + score.getPlayedOn());
        }

        consoleUI.menu();
    }
}
