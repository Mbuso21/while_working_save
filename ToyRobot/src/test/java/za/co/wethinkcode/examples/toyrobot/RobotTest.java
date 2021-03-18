package za.co.wethinkcode.examples.toyrobot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    private Position position;

    @Test
    void testSetStatus(){
        Robot robot = new Robot("CrashTestDummy");
        robot.setStatus("Not safe");
        assertEquals("Not safe", robot.getStatus());
    }

    @Test
    void initialPosition() {
        Robot robot = new Robot("CrashTestDummy");
        assertEquals(Robot.CENTRE, robot.getPosition());
        assertEquals(Direction.UP, robot.getCurrentDirection());
    }

    @Test
    void dump() {
        Robot robot = new Robot("CrashTestDummy");
        assertEquals("[0,0] CrashTestDummy> Ready", robot.toString());
    }

    @Test
    void shutdown() {
        Robot robot = new Robot("CrashTestDummy");
        Command command = new ShutdownCommand();
        assertFalse(robot.handleCommand(command));
    }

    @Test
    void forward() {
        Robot robot = new Robot("CrashTestDummy");
        ForwardCommand command = new ForwardCommand("10");
        assertTrue(robot.handleCommand(command));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY() + 10);
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Moved forward by 10 steps.", robot.getStatus());
    }

    @Test
    void forwardforward() {
        Robot robot = new Robot("CrashTestDummy");
        ForwardCommand command = new ForwardCommand("10");
        ForwardCommand command1 = new ForwardCommand("5");
        assertTrue(robot.handleCommand(command));
        assertTrue(robot.handleCommand(command1));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY() + 15);
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Moved forward by 5 steps.", robot.getStatus());
    }

    @Test
    void tooFarForward() {
        Robot robot = new Robot("CrashTestDummy");
        ForwardCommand command = new ForwardCommand("1000");
        assertTrue(robot.handleCommand(command));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY());
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Sorry, I cannot go outside my safe zone.", robot.getStatus());
    }

    @Test
    void help() {
        Robot robot = new Robot("CrashTestDummy");
        Command command = new HelpCommand();
        assertTrue(robot.handleCommand(command));
        assertEquals("I can understand these commands:\n" +
                "OFF  - Shut down robot\n" +
                "HELP - provide information about commands\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                "BACK - move backwards by a specified number of steps\n" +
                "LEFT - turn left 90 degrees\n" +
                "RIGHT - turn right 90 degrees\n" +
                "SPRINT - to sprint forward in step\n" +
                "REPLAY - to replay commands\n" +
                "REPLAY REVERSED - to replay commands in reverse", robot.getStatus());
    }

    @Test
    void changeDirectionLeft(){
        Robot robot = new Robot("CrashTestDummy");
        Command command = new LeftCommand();
        assertTrue(robot.handleCommand(command));
        assertEquals(Direction.LEFT, robot.getCurrentDirection());
    }

    private final PrintStream standardOut = System.out;
    private final InputStream standardIn = System.in;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        System.setIn(standardIn);
    }

    private void verifyOutput(String[] actualOutput, String[] expectedOutput) {
        for (int i = actualOutput.length - 1, j = expectedOutput.length - 1; j > 0; i--, j--) {
            assertEquals(expectedOutput[j], actualOutput[i]);
        }
    }

    @Test
    void testLeft() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nleft\nforward 10\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,0] HAL> Turned left.",
                "HAL> What must I do next?",
                "[-10,0] HAL> Moved forward by 10 steps.",
                "HAL> What must I do next?",
                "[-10,0] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void changeDirectionRight(){
        Robot robot = new Robot("CrashTestDummy");
        Command command = new RightCommand();
        assertTrue(robot.handleCommand(command));
        assertEquals(Direction.RIGHT, robot.getCurrentDirection());
    }

    @Test
    void testLeftForward10RightForward10() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nleft\nforward 10\nright\nforward 10\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,0] HAL> Turned left.",
                "HAL> What must I do next?",
                "[-10,0] HAL> Moved forward by 10 steps.",
                "HAL> What must I do next?",
                "[-10,0] HAL> Turned right.",
                "HAL> What must I do next?",
                "[-10,10] HAL> Moved forward by 10 steps.",
                "HAL> What must I do next?",
                "[-10,10] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testForwardPastLimit() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 210\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,0] HAL> Sorry, I cannot go outside my safe zone.",
                "HAL> What must I do next?",
                "[0,0] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testBackCorrect() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nback 50\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,-50] HAL> Moved back by 50 steps.",
                "HAL> What must I do next?",
                "[0,-50] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testBackPastLimit() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nback 210\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,0] HAL> Sorry, I cannot go outside my safe zone.",
                "HAL> What must I do next?",
                "[0,0] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testRightBack10LeftBack10() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nright\nback 10\nleft\nback 10\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,0] HAL> Turned right.",
                "HAL> What must I do next?",
                "[-10,0] HAL> Moved back by 10 steps.",
                "HAL> What must I do next?",
                "[-10,0] HAL> Turned left.",
                "HAL> What must I do next?",
                "[-10,-10] HAL> Moved back by 10 steps.",
                "HAL> What must I do next?",
                "[-10,-10] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testSprint() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nsprint 5\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,5] HAL> Moved forward by 5 steps.",
                "[0,9] HAL> Moved forward by 4 steps.",
                "[0,12] HAL> Moved forward by 3 steps.",
                "[0,14] HAL> Moved forward by 2 steps.",
                "[0,15] HAL> Moved forward by 1 steps.",
                "HAL> What must I do next?",
                "[0,15] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testReplay() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nreplay\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,10] HAL> Moved forward by 10 steps.",
                "HAL> What must I do next?",
                "[0,30] HAL> Moved forward by 20 steps.",
                "HAL> What must I do next?",
                "[0,40] HAL> Moved forward by 10 steps.",
                "[0,60] HAL> Moved forward by 20 steps.",
                "[0,60] HAL> replayed 2 commands.",
                "HAL> What must I do next?",
                "[0,60] HAL> Shutting down..."};

        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testReplayLast2() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nback 30\nreplay 2\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,10] HAL> Moved forward by 10 steps.",
                "HAL> What must I do next?",
                "[0,30] HAL> Moved forward by 20 steps.",
                "HAL> What must I do next?",
                "[0,0] HAL> Moved back by 30 steps.",
                "HAL> What must I do next?",
                "[0,20] HAL> Moved forward by 20 steps.",
                "[0,-10] HAL> Moved back by 30 steps.",
                "[0,-10] HAL> replayed 2 commands.",
                "HAL> What must I do next?",
                "[0,-10] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testReplayRangeString() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nback 30\nback 40\nreplay 4-2\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String actualOutput = outputStreamCaptor.toString().trim();
        String expectedOutput = "What do you want to name your robot?\n" +
                "Hello Kiddo!\n" +
                "Loaded EmptyMaze\n" +
                "[0,0] HAL> Ready\n" +
                "HAL> What must I do next?\n" +
                "[0,10] HAL> Moved forward by 10 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,30] HAL> Moved forward by 20 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,0] HAL> Moved back by 30 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,-40] HAL> Moved back by 40 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,-30] HAL> Moved forward by 10 steps.\n" +
                "[0,-10] HAL> Moved forward by 20 steps.\n" +
                "[0,-10] HAL> replayed 2 commands.\n" +
                "HAL> What must I do next?\n" +
                "[0,-10] HAL> Shutting down...";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testReplayRange() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nback 30\nback 40\nreplay 4-2\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,10] HAL> Moved forward by 10 steps.",
                "HAL> What must I do next?",
                "[0,30] HAL> Moved forward by 20 steps.",
                "HAL> What must I do next?",
                "[0,0] HAL> Moved back by 30 steps.",
                "HAL> What must I do next?",
                "[0,-40] HAL> Moved back by 40 steps.",
                "HAL> What must I do next?",
                "[0,-30] HAL> Moved forward by 10 steps.",
                "[0,-10] HAL> Moved forward by 20 steps.",
                "[0,-10] HAL> replayed 2 commands.",
                "HAL> What must I do next?",
                "[0,-10] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testReplayNoArgReversedFullString() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nreplay reversed\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String actualOutput = outputStreamCaptor.toString().trim();
        String expectedOutput = "What do you want to name your robot?\n" +
                "Hello Kiddo!\n" +
                "Loaded EmptyMaze\n" +
                "[0,0] HAL> Ready\n" +
                "HAL> What must I do next?\n" +
                "[0,10] HAL> Moved forward by 10 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,30] HAL> Moved forward by 20 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,50] HAL> Moved forward by 20 steps.\n" +
                "[0,60] HAL> Moved forward by 10 steps.\n" +
                "[0,60] HAL> replayed 2 commands.\n" +
                "HAL> What must I do next?\n" +
                "[0,60] HAL> Shutting down...";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testReplayNoArgReversed() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nreplay reversed\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,10] HAL> Moved forward by 10 steps.",
                "HAL> What must I do next?",
                "[0,30] HAL> Moved forward by 20 steps.",
                "HAL> What must I do next?",
                "[0,50] HAL> Moved forward by 20 steps.",
                "[0,60] HAL> Moved forward by 10 steps.",
                "[0,60] HAL> replayed 2 commands.",
                "HAL> What must I do next?",
                "[0,60] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testReplayLast2ReversedFullString() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nback 30\nreplay reversed 2\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String actualOutput = outputStreamCaptor.toString().trim();
        String expectedOutput =  "What do you want to name your robot?\n" +
                "Hello Kiddo!\n" +
                "Loaded EmptyMaze\n" +
                "[0,0] HAL> Ready\n" +
                "HAL> What must I do next?\n" +
                "[0,10] HAL> Moved forward by 10 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,30] HAL> Moved forward by 20 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,0] HAL> Moved back by 30 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,-30] HAL> Moved back by 30 steps.\n" +
                "[0,-10] HAL> Moved forward by 20 steps.\n" +
                "[0,-10] HAL> replayed 2 commands.\n" +
                "HAL> What must I do next?\n" +
                "[0,-10] HAL> Shutting down...";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testReplayLast2Reversed() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nback 30\nreplay reversed 2\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,10] HAL> Moved forward by 10 steps.",
                "HAL> What must I do next?",
                "[0,30] HAL> Moved forward by 20 steps.",
                "HAL> What must I do next?",
                "[0,0] HAL> Moved back by 30 steps.",
                "HAL> What must I do next?",
                "[0,-30] HAL> Moved back by 30 steps.",
                "[0,-10] HAL> Moved forward by 20 steps.",
                "[0,-10] HAL> replayed 2 commands.",
                "HAL> What must I do next?",
                "[0,-10] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }

    @Test
    void testReplayRangeReversedFullString() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nback 30\nback 40\nreplay reversed 4-2\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String actualOutput = outputStreamCaptor.toString().trim();
        String expectedOutput = "What do you want to name your robot?\n" +
                "Hello Kiddo!\n" +
                "Loaded EmptyMaze\n" +
                "[0,0] HAL> Ready\n" +
                "HAL> What must I do next?\n" +
                "[0,10] HAL> Moved forward by 10 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,30] HAL> Moved forward by 20 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,0] HAL> Moved back by 30 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,-40] HAL> Moved back by 40 steps.\n" +
                "HAL> What must I do next?\n" +
                "[0,-20] HAL> Moved forward by 20 steps.\n" +
                "[0,-10] HAL> Moved forward by 10 steps.\n" +
                "[0,-10] HAL> replayed 2 commands.\n" +
                "HAL> What must I do next?\n" +
                "[0,-10] HAL> Shutting down...";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testReplayRangeReversed() {
        InputStream mockedInput = new ByteArrayInputStream("HAL\nforward 10\nforward 20\nback 30\nback 40\nreplay reversed 4-2\noff\n".getBytes());
        System.setIn(mockedInput);
        Play.main(new String[]{"text", "EmptyMaze"});
        String[] actualOutput = outputStreamCaptor.toString().trim().split("\n");
        String[] expectedOutput = {"HAL> What must I do next?",
                "[0,10] HAL> Moved forward by 10 steps.",
                "HAL> What must I do next?",
                "[0,30] HAL> Moved forward by 20 steps.",
                "HAL> What must I do next?",
                "[0,0] HAL> Moved back by 30 steps.",
                "HAL> What must I do next?",
                "[0,-40] HAL> Moved back by 40 steps.",
                "HAL> What must I do next?",
                "[0,-20] HAL> Moved forward by 20 steps.",
                "[0,-10] HAL> Moved forward by 10 steps.",
                "[0,-10] HAL> replayed 2 commands.",
                "HAL> What must I do next?",
                "[0,-10] HAL> Shutting down..."};
        verifyOutput(actualOutput, expectedOutput);
    }
}