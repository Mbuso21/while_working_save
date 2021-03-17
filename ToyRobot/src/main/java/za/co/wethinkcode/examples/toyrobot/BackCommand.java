package za.co.wethinkcode.examples.toyrobot;

public class BackCommand extends Command{

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        if (target.updatePosition(-nrSteps)){
            target.setStatus("Moved back by "+nrSteps+" steps.");
        } else {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
        }
        return true;
    }

    public BackCommand(String argument){
        super("back", argument);
    }
}
