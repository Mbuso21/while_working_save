package za.co.wethinkcode.examples.toyrobot;

public class SprintCommand extends Command {

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        String output = "";
        for (int i = nrSteps; i > 1; i--) {
            if(target.updatePosition(i)) {
                target.setStatus("Moved forward by " + i + " steps.");
                System.out.println(target.toString());
            } else {
                target.setStatus("Sorry, I cannot go outside my safe zone.");
                System.out.println(target.toString());
            }
        }
        if(target.updatePosition(1)){
            target.setStatus("Moved forward by 1 steps.");
        }
        return true;
    }

    public SprintCommand(String argument){
        super("sprint", argument);
    }
}
