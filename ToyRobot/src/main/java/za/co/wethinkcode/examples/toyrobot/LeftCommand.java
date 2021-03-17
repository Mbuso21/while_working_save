package za.co.wethinkcode.examples.toyrobot;

public class LeftCommand extends Command {

    public LeftCommand() {
        super("left");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("Turned left.");
        target.setCurrentDirection(Direction.LEFT);
        return true;
    }
}
