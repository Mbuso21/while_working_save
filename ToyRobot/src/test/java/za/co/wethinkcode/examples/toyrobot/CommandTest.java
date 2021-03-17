package za.co.wethinkcode.examples.toyrobot;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    Command test;

    @Test
    void getHelpName() {
        Command test = new HelpCommand();
        assertEquals("help", test.getName());
    }
    @Test
    void getShutdownName() {
        Command test = new ShutdownCommand();
        assertEquals("off", test.getName());
    }
    @Test
    void getForwardName() {
        Command  test = new ForwardCommand("100");
        assertEquals("forward", test.getName());
        assertEquals("100", test.getArgument());
    }
    @Test
    void createCommand() {
        Command forward = Command.create("forward 10");
        assertEquals("forward", forward.getName());
        assertEquals("10", forward.getArgument());

        Command shutdown = Command.create("shutdown");
        assertEquals("off", shutdown.getName());

        Command help = Command.create("help");
        assertEquals("help", help.getName());
    }
    @Test
    void createInvalidCommand() {
        try {
            Command forward = Command.create("say hello");
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported command: say hello", e.getMessage());
        }
    }
    @Test
    void executeForward() {
        Robot robot = new Robot("CrashTestDummy");
        Command forward100 = Command.create("forward 10");
        assertTrue(forward100.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY() + 10);
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Moved forward by 10 steps.", robot.getStatus());
    }
    @Test
    void getBackName() {
        Command  test = new BackCommand("100");
        assertEquals("back", test.getName());
        assertEquals("100", test.getArgument());
    }
    @Test
    void executeBack() {
        Robot robot = new Robot("CrashTestDummy");
        Command back10 = Command.create("back 10");
        assertTrue(back10.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY() - 10);
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Moved back by 10 steps.", robot.getStatus());
    }
    @Test
    void executeHelp() {
        Robot robot = new Robot("CrashTestDummy");
        Command help = Command.create("help");
        assertTrue(help.execute(robot));
        assertEquals("I can understand these commands:\n" +
                "OFF  - Shut down robot\n" +
                "HELP - provide information about commands\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                "BACK - move backwards by a specified number of steps\n" +
                "LEFT - turn left 90 degrees\n" +
                "RIGHT - turn right 90 degrees\n" +
                "SPRINT - to sprint forward in step" +
                "REPLAY - to replay commands" +
                "REPLAY REVERSED - to replay commands in reverse", robot.getStatus());
    }
    @Test
    void executeShutDown() {
        Robot robot = new Robot("CrashTestDummy");
        Command shutDown = Command.create("shutdown");
        assertFalse(shutDown.execute(robot));
        assertEquals("Shutting down...", robot.getStatus());
    }
    @Test
    void getLeftCommand() {
        Command test = new LeftCommand();
        assertEquals("left", test.getName());
    }
    @Test
    void executeLeftCommand() {
        Robot robot = new Robot("CrashTestDummy");
        Command left = Command.create("left");
        assertTrue(left.execute(robot));
        assertEquals("Turned left.", robot.getStatus());
    }
    @Test
    void getRightCommand() {
        Command test = new RightCommand();
        assertEquals("right", test.getName());
    }
    @Test
    void executeRightCommand() {
        Robot robot = new Robot("CrashTestDummy");
        Command right = Command.create("right");
        assertTrue(right.execute(robot));
        assertEquals("Turned right.", robot.getStatus());
    }
    @Test
    void getSprintName() {
        Command  test = new SprintCommand("5");
        assertEquals("sprint", test.getName());
        assertEquals("5", test.getArgument());
    }
    @Test
    void keepCommandAndGetCommand(){
        Command test = new ReplayCommand();
        test.setCommandHistory("forward 10");
        test.setCommandHistory("forward 5");
        test.setCommandHistory("left");
        test.setCommandHistory("right");
        test.setCommandHistory("help");
        test.setCommandHistory("replay");
        test.setCommandHistory("replay 2");
        test.setCommandHistory("replay reversed");
        assertEquals("[forward 10, forward 5, left, right]",test.getCommandHistory().toString());
    }
    @Test
    void getReplayName() {
        Command test = new ReplayCommand();
        assertEquals("replay", test.getName());
    }
    @Test
    void getReplayWithArg(){
        Command test = new ReplayCommand("2");
        assertEquals("replay", test.getName());
        assertEquals("2", test.getArgument());
    }
    @Test
    void sortCommandHistory(){
        Command test = new ReplayCommand("2");
        test.setCommandHistory("forward 10");
        test.setCommandHistory("forward 5");
        test.setCommandHistory("left");
        test.setCommandHistory("right");
        List commandHistory = test.getCommandHistory();
        List newCommandHistory = test.sortCommandHistoryIndexToLast(test.getArgument(), commandHistory);
        assertEquals("[left, right]", newCommandHistory.toString());
    }
//    @Test
//    void testSortArgumentOneDigit() {
//        Command test = new ReplayCommand("2");
//        assertEquals("2",);
//    }
}