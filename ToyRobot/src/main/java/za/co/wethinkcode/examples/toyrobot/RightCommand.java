package za.co.wethinkcode.examples.toyrobot;

public class RightCommand extends Command{

    public RightCommand() {
        super("right");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("Turned right.");
        target.setCurrentDirection(Direction.RIGHT);
        return true;
    }
}
