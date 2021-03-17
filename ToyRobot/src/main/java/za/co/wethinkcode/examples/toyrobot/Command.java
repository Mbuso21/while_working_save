package za.co.wethinkcode.examples.toyrobot;

import java.util.*;

public abstract class Command {

    private final String name;
    private String argument;
    public static List<String> commandHistory = new ArrayList<String>();
    public abstract boolean execute(Robot target);

    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }

    public Command(String name) {
        this.name = name.trim().toLowerCase();
    }

    public String getArgument() {
        return this.argument.trim();
    }
    public void setCommandHistory(String command) {
        if(!command.contains("replay") && !command.contains("help")) {
            commandHistory.add(command);
        }
    }

    public List getCommandHistory() {
        return commandHistory;
    }

    public List sortCommandHistoryIndexToLast(String fromHereReplay, List getCommandHistory) {
        int replayIndex = Integer.parseInt(fromHereReplay);
        int lastIndex = getCommandHistory.size();
        return getCommandHistory.subList(lastIndex-replayIndex,lastIndex);
    }

    public List sortCommandHistoryIndexToIndex(String fromHereToThere, List commandHistory){
        String[] argSplit = fromHereToThere.split("-");
        int firstNum = Integer.parseInt(argSplit[0]);
        int secondNum = Integer.parseInt(argSplit[1]);
        if(firstNum - secondNum < 1){return commandHistory;}
        else{ return commandHistory.subList(secondNum-2, firstNum-2);}
    }


    public List sortCommandInstruction(String[] argSplit, List commandHistory){

        if(getArgument().contains("reversed")) {

            Collections.reverse(commandHistory);
            if(argSplit.length == 2){
                return commandHistory;
            }else if(argSplit[2].matches("[0-9]")) {
                int toIndex = Integer.parseInt(argSplit[2]);
                return commandHistory.subList(0,toIndex);
            }

        }else if(argSplit.length == 2){

            if(argSplit[1].matches("[0-9]")){
                return sortCommandHistoryIndexToLast(argSplit[1], commandHistory);
            }else if(argSplit[1].split("-").length == 2) {
                return sortCommandHistoryIndexToIndex(argSplit[1], commandHistory);
            }
            else {
                return commandHistory;
            }
        }else {
            return commandHistory;
        }
        return commandHistory;
    }

    public String getName() {
        return name;
    }

    public static Command create(String instruction) {
        String[] args = instruction.toLowerCase().trim().split(" ");
        switch (args[0]){
            case "shutdown":
            case "off":
                return new ShutdownCommand();
            case "help":
                return new HelpCommand();
            case "forward":
                return new ForwardCommand(args[1]);
            case "back":
                return new BackCommand(args[1]);
            case "left":
                return new LeftCommand();
            case "right":
                return new RightCommand();
            case "sprint":
                return new SprintCommand(args[1]);
            case "replay":
                return new ReplayCommand(instruction);
            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }
}
