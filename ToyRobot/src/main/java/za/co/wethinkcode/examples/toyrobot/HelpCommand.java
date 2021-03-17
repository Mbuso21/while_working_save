package za.co.wethinkcode.examples.toyrobot;

public class HelpCommand extends Command{
    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("I can understand these commands:\n" +
                "OFF  - Shut down robot\n" +
                "HELP - provide information about commands\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                "BACK - move backwards by a specified number of steps\n" +
                "LEFT - turn left 90 degrees\n" +
                "RIGHT - turn right 90 degrees\n" +
                "SPRINT - to sprint forward in step\n" +
                "REPLAY - to replay commands\n" +
                "REPLAY REVERSED - to replay commands in reverse");
        return true;
    }
}
