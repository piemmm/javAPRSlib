package org.prowl.aprslib.position;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PositionParserTest {

   /**
    * Test for NMEA sentences with valid positions irrespective of accuracy
    * 
    * @throws Exception
    */
   @Test
   public void testParseNMEASentences() throws Exception {
      // NMEA test packets as seen on ARPS-IS
      String[] testCases = new String[] {
            "$GPGLL,4609.105,N,12258.824,W,122358,A*3A",
            "$GPGGA,000045.996,0340.0000,N,02345.0000,E,0,00,50.0,0.0,M,,,,0000*3C", // Invalid fix - no of sats also 0
            "$GPGGA,201114.000,4455.5520,N,09323.0659,W,2,09,0.9,286.3,M,-31.7,M,3.8,0000*4B", // Valid DGPS type fix
            "$GPGGA,201120.000,4531.7420,N,12225.7631,W,2,10,0.9,60.5,M,-19.2,M,3.8,0000*73" // Valid DGPS again
      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test : testCases) {
         Position position = PositionParser.parseNMEA(test.getBytes());
         assertNotNull("Position should not be null", position);
      }
   }

   /**
    * Test an NMEA sentence without a GPS lock, but has positioning data.
    *
    * This should throw an exception as the data has a status of '0' (no lock) and has zero lon/lat
    */
   @Test
   public void testInaccurateNMEASentence() throws Exception {
      // NMEA test packets as seen on ARPS-IS
      String[] testCases = new String[] {
            "$GPGGA,000045.996,0000.0000,N,00000.0000,E,0,00,50.0,0.0,M,,,,0000*3C" // Invalid fix - no of sats also 0
      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test : testCases) {
         try {
            Position position = PositionParser.parseNMEA(test.getBytes());
            fail("Invalid GPS data was converted to a position object for NMEA data");
         } catch (UnparsablePositionException e) {
            // Test success
         }
      }
   }

   @Test
   public void testParseUncompressedPacket() throws Exception {
      // NMEA test packets as seen on ARPS-IS
      String[] testCases = new String[] {
            ";146.80XY*111111z4359.00N/12049.76WrT173 R60 linked to Drake pk and Grizzly",
            ";DK0RE   *072012z5148.25N/00811.53EKN47 am Silo",
            ";*111111z4338.31N/12319.64W R50m DRAIN",
            // "!4553.97NPZ01556.88E#/W3, APRS 9A2CD Conjo Zagreb 1", // winaprs 'PZ' - needs investigation
            // "!3237.30N/11525.44@\"#W# Mexicali, Baja * XE2DAK-2 / UHF", investigate this too
            // "/08623.19WR203/051/caver mobile",
            ";W9MKS   *111111z4111.40N/08858.87WE SRRC Mtg 7 PM 1M (2M if Hol) Leonore, IL w9mks.org",
            // "!3302.51N/9655.48W_",
            // ";444.900+*111111z2857.60N/9952.40WrT141",
            // "=5322.97N/000�3.42W-73 via ISS. Op John {UISS54}",
            // ";147.210VT*111111Z4403.55N/07215.46Wr147.210MHz T100 +060 R50m KB1FDA",
            // ";444.375NC*224620h3618.62N/07830.06Wrhttp://www.carolina440.net 444.375 Mhz PL 100 Hz",
            // ";W9MKS *111111z4111.40N/08858.87WE SRRC Mtg 7 PM 1M (2M if Hol) Leonore, IL w9mks.org"

      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test : testCases) {
         Position position = PositionParser.parseUncompressed(test.getBytes());
         assertNotNull("Position should not be null", position);
      }
   }

   @Test
   public void testParseInvalidUncompressedPacket() throws Exception {
      // NMEA test packets as seen on ARPS-IS
      String[] testCases = new String[] {
            "!0000.000/00000.000>000/000",
            "/235949h0000.000/00000.000<000/000/A=000000/TinyTrak3",
            "!0000.000/00000.000v000/000/TT3",
            "!0000.000/00000.000>000/000",
            "/08623.19WR203/051/caver mobile"
      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test : testCases) {
         try {
            Position position = PositionParser.parseUncompressed(test.getBytes());
            fail("Invalid GPS data was converted to a position object for uncompressed data: " + test);
         } catch (UnparsablePositionException e) {
            // test pass
         }
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
      } catch (IllegalArgumentException e) {
         // Test successfull.
      }
   }

}
