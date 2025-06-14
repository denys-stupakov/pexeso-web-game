package sk.tuke.kp.kpi.pexeso.command;

public class ExitGameCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Exiting game...");
        System.exit(0);
    }
}