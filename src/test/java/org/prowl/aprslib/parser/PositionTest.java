/**
 *
 */
package org.prowl.aprslib.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author ihawkins
 */
public class PositionTest {

    private static final double LAT_1 = 28.55217;
    private static final double LON_1 = 121.4780;
    private static final double LAT_2 = 18.55217;
    private static final double LON_2 = 101.4780;
    private static final Position POSITION = new Position(LAT_1, LON_1);

    /**
     * Test method for {@link org.prowl.aprslib.position.Position#Position()}.
     */
    @Test
    public void testPosition() {
        assertEquals(LAT_1, POSITION.getLatitude(), 0.000001);
        assertEquals(LON_1, POSITION.getLongitude(), 0.000001);
    }

    /**
     * Test method for {@link org.prowl.aprslib.position.Position#Position(double, double, org.prowl.aprslib.position.Ambiguity, char, char)}.
     */
    @Test
    public void testPositionDoubleDoubleAmbiguityCharChar() {
        Position p = new Position(LAT_1, LON_1, Ambiguity.NONE, '/', '>');
        assertEquals(LAT_1, p.getLatitude(), 0.000001);
        assertEquals(LON_1, p.getLongitude(), 0.000001);
        assertEquals(Ambiguity.NONE, p.getPositionAmbiguity());
        assertEquals('/', p.getSymbolTable());
        assertEquals('>', p.getSymbolCode());
    }

    /**
     * Test method for {@link org.prowl.aprslib.position.Position#setLatitude(double)}.
     */
    @Test
    public void testSetLatitude() {
        Position p = new Position(0, 0);
        assertEquals(0, p.getLatitude(), 0.00001);
        p.setLatitude(1.23456);
        assertEquals(1.23456, p.getLatitude(), 0.00001);
    }

    /**
     * Test method for {@link org.prowl.aprslib.position.Position#setLongitude(double)}.
     */
    @Test
    public void testSetLongitude() {
        Position p = new Position(0, 0);
        assertEquals(0, p.getLongitude(), 0.00001);
        p.setLongitude(1.23456);
        assertEquals(1.23456, p.getLongitude(), 0.00001);
    }

    /**
     * Test method for {@link org.prowl.aprslib.position.Position#setAltitude(int)}.
     */
    @Test
    public void testSetAltitude() {
        Position p = new Position(0, 0);
        assertEquals(-1, p.getAltitude());
        p.setAltitude(1000);
        assertEquals(1000, p.getAltitude());
    }

    /**
     * Test method for {@link org.prowl.aprslib.position.Position#setPositionAmbiguity(org.prowl.aprslib.position.Ambiguity)}.
     */
    @Test
    public void testSetPositionAmbiguity() {
        Position p = new Position(0, 0);
        assertEquals(Ambiguity.NONE, p.getPositionAmbiguity());
        p.setPositionAmbiguity(Ambiguity.NEAREST_10_MINUTES);
        assertEquals(Ambiguity.NEAREST_10_MINUTES, p.getPositionAmbiguity());

    }


    /**
     * Test method for {@link org.prowl.aprslib.position.Position#setSymbolTable(char)}.
     */
    @Test
    public void testSetSymbolTable() {
        Position p = new Position(0, 0);
        assertEquals(92, p.getSymbolTable());
        p.setSymbolTable('*');
        assertEquals('*', p.getSymbolTable());
    }

    /**
     * Test method for {@link org.prowl.aprslib.position.Position#setSymbolCode(char)}.
     */
    @Test
    public void testSetSymbolCode() {
        Position p = new Position(0, 0);
        assertEquals(46, p.getSymbolCode());
        p.setSymbolCode('*');
        assertEquals('*', p.getSymbolCode());
    }


    /**
     * Test method for {@link org.prowl.aprslib.position.Position#toDecimalString()}.
     */
    @Test
    public void testToDecimalString() {
        assertEquals("28.55217, 121.47800", POSITION.toDecimalString());
    }


    /**
     * Test method for {@link org.prowl.aprslib.position.Position#distFrom(double, double, double, double)}.
     */
    @Test
    public void testDistFrom() {
        assertEquals(1439.790893, Position.distFrom(LAT_1, LON_1, LAT_2, LON_2), 0.000001);
    }

    /**
     * Test method for {@link org.prowl.aprslib.position.Position#distance(org.prowl.aprslib.position.Position)}.
     */
    @Test
    public void testDistance() {
        assertEquals(1439.790893, POSITION.distance(new Position(LAT_2, LON_2)), 0.000001);
    }

    public class Ambiguity {
        public static final int NONE = 0;
        public static final int NEAREST_10_MINUTES = 10;
    }

}
