package za.co.wethinkcode.examples.toyrobot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    public void shouldKnowXandY() {
        Position p = new Position(10, 20);
        assertEquals(10, p.getX());
        assertEquals(20, p.getY());

    }
    @Test
    public void equality() {
        assertEquals(new Position(-44, 63), new Position(-44, 63));
        assertNotEquals(new Position(-44, 63), new Position(0, 63));
        assertNotEquals(new Position(-44, 63), new Position(-44, 0));
        assertNotEquals(new Position(-44, 63), new Position(0, 0));
    }
    @Test
    public void insideRectangularRegion() {
        Position topLeft = new Position(-20, 20);
        Position bottomRight = new Position(20,-20);
        assertTrue((new Position(10,10)).isIn(topLeft, bottomRight), "should be inside");
        assertFalse((new Position(10,30)).isIn(topLeft, bottomRight), "should be beyond top boundary");
        assertFalse((new Position(10,-30)).isIn(topLeft, bottomRight), "should be beyond bottom boundary");
        assertFalse((new Position(30,10)).isIn(topLeft, bottomRight), "should be beyond right boundary");
        assertFalse((new Position(-30,10)).isIn(topLeft, bottomRight), "should be beyond left boundary");
    }
}