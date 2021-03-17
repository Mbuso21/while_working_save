package za.co.wethinkcode.examples.toyrobot;

import java.util.*;

enum Direction{
    UP, DOWN, LEFT, RIGHT
}

public class Robot {

    private final Position TOP_LEFT = new Position(-200,100);
    private final Position BOTTOM_RIGHT = new Position(100,-200);

    public static final Position CENTRE = new Position(0,0);

    private Position position;
    private Direction currentDirection;
    private String status;
    private String name;

    public Robot(String name) {
        this.name = name;
        this.status = "Ready";
        this.position = CENTRE;
        this.currentDirection = Direction.UP;
    }

    public String getStatus() {
        return this.status;
    }

    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection){

        if(this.currentDirection == Direction.UP) {

            this.currentDirection = currentDirection;
        }else if(this.currentDirection == Direction.LEFT){

            if(currentDirection == Direction.LEFT){
                this.currentDirection = Direction.DOWN;
            }else{
                this.currentDirection = Direction.UP;
            }
        }else if(this.currentDirection == Direction.DOWN){

            if(currentDirection == Direction.LEFT){
                this.currentDirection = Direction.RIGHT;
            } else{
                this.currentDirection = Direction.LEFT;
            }
        }else if(this.currentDirection == Direction.RIGHT){

            if(currentDirection == Direction.LEFT) {
                this.currentDirection = Direction.UP;
            }else{
                this.currentDirection = Direction.DOWN;
            }
        }
    }

    public boolean handleCommand(Command command) {
        return command.execute(this);
    }

    public boolean updatePosition(int nrSteps){

        int newX = this.position.getX();
        int newY = this.position.getY();

        if (Direction.UP.equals(this.currentDirection)) {
            newY = newY + nrSteps;
        }else if(Direction.DOWN.equals(this.currentDirection)){
            newY = newY - nrSteps;
        }else if(Direction.RIGHT.equals(this.currentDirection)){
            newX = newX + nrSteps;
        }else{
            newX = newX - nrSteps;
        }

        Position newPosition = new Position(newX, newY);
        if (newPosition.isIn(TOP_LEFT,BOTTOM_RIGHT)){
            this.position = newPosition;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + this.position.getX() + "," + this.position.getY() + "] "
                + this.name + "> " + this.status;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
}
