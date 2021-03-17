package za.co.wethinkcode.examples.toyrobot;

public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("off");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("Shutting down...");
        commandHistory.clear();
        return false;
    }
}
