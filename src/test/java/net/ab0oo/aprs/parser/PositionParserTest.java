package net.ab0oo.aprs.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PositionParserTest {

   @Test
   public void testParseNMEASentences() throws Exception {
      // NMEA test packets as seen on ARPS-IS
      String[] testCases = new String[] {
            "$GPGLL,4609.105,N,12258.824,W,122358,A*3A"
      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test: testCases) {
         Position position = PositionParser.parseNMEA(test.getBytes());
         assertNotNull("Position should not be null", position);
      }
   }

   /**
    * Check that a null sentence throws an exception.
    */
   @Test
   public void invalidNMEASentence() throws Exception {
      try {
         Position position = PositionParser.parseNMEA(null);
         fail("Invalid position did not throw exception");
      } catch(IllegalArgumentException e) {
         // Test successfull.
      }
   }

}
