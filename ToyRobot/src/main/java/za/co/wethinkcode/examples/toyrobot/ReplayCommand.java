package za.co.wethinkcode.examples.toyrobot;

import java.util.*;

public class ReplayCommand extends Command{


    public boolean execute(Robot target) {
        Command command;
        String[] argSplit = getArgument().toLowerCase().trim().split(" ");
        List commandHistoryHere = sortCommandInstruction(argSplit, commandHistory);

        for(int i = 0; i < commandHistoryHere.size();i++){
            command = create(commandHistoryHere.get(i).toString());
            target.handleCommand(command);
            System.out.println(target);
        }
        target.setStatus("replayed "+ commandHistoryHere.size() +" commands.");
        return true;
    }

    public ReplayCommand() {
        super("replay");
    }

    public ReplayCommand(String argument) {
        super("replay", argument);
    }

    public boolean isReversed() {
        return true;
    }
}
