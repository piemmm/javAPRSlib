package org.prowl.aprslib.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



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
            "$GPGGA,201120.000,4531.7420,N,12225.7631,W,2,10,0.9,60.5,M,-19.2,M,3.8,0000*73", // Valid DGPS again
            "$GPGGA,194733,3601.0626,N,09547.8473,W,,,,,M,,M,,*75" // GPS data sparse - no lock or sat data provided but still usable


      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test : testCases) {
         Position position = PositionParser.parseNMEA(test.getBytes());
         assertNotEquals(0, position.getLatitude());
         assertNotEquals(0, position.getLongitude());
         assertNotNull( position,"Position should not be null");
      }
   }


   // (piemmm) This test is erroneous - it's not the parsers job to refuse to parse packets with quality set to 0
   // - that's the wrong place to do it. Instead, you store the quality in the position object and let the
    // caller decide what to do with it. Otherwise why did you bother with the hasFault flags?
//   /**
//    * Test an NMEA sentence without a GPS lock, but has positioning data.
//    *
//    * This should throw an exception as the data has a status of '0' (no lock) and has zero lon/lat
//    */
//   @Test
//   public void testInaccurateNMEASentence() throws Exception {
//      // NMEA test packets as seen on ARPS-IS
//      String[] testCases = new String[] {
//            "$GPGGA,000045.996,0000.0000,N,00000.0000,E,0,00,50.0,0.0,M,,,,0000*3C", // Invalid fix - no of sats also 0
//      };
//
//      // Check that our list of NMEA test cases can be parsed ok
//      for (String test : testCases) {
//         try {
//            PositionParser.parseNMEA(test.getBytes());
//            fail("Invalid GPS data was converted to a position object for NMEA data");
//         } catch (UnparsablePositionException e) {
//            // Test success
//         }
//      }
//   }

   @Test
   public void testParseUncompressedPacket() throws Exception {
      // Uncompressed test packets as seen on ARPS-IS
      String[] testCases = new String[] {
            // ";G0VEA-B *082251z5037.41N/-0323.84ErDVRPTR V2 Exmouth",
            // "/111111z6029.79N/0067.45E[inReachExplorer 12:49:00", // Short LON position. Not one off corruption LA3QMA
            ";145.687.5_111111z4633.53N/00023.19Er",
            ";SOMERSET CO RACES UHF SYSTEM*111111z4013.93N/07906.15Wr444.475-R 443.150-R PLs 123HZ",
            ";Windmill *111111z5121.33N\\900108.22EL Built in 1789, Grade 1 Listed - Open Sunday's Easter to Sept.", // alernate symbols
            ";LZ0DAC B *081940z4228.42ND02540.01EaRNG0050 440 Voice 438.45000MHz -7.6000MHz",
            ";146.80XY*111111z4359.00N/12049.76WrT173 R60 linked to Drake pk and Grizzly",
            ";DK0RE   *072012z5148.25N/00811.53EKN47 am Silo",
            ";*111111z4338.31N/12319.64W R50m DRAIN",
            ";W9MKS   *111111z4111.40N/08858.87WE SRRC Mtg 7 PM 1M (2M if Hol) Leonore, IL w9mks.org",
            ";444.375NC*224620h3618.62N/07830.06Wrhttp://www.carolina440.net 444.375 Mhz PL 100 Hz",
            ";W9MKS *111111z4111.40N/08858.87WE SRRC Mtg 7 PM 1M (2M if Hol) Leonore, IL w9mks.org",


// are these corrupt?
//        "@2105z91.23/.P00 Randy in Moraine APRSIS2 WIL#ek��@��@281053z3941.23N/08412.92W-PHG2030 Randy in Moraine APRSIS32 WINLINK",
//       "!25595600.00SD54572500.00W&/A=000000440 MMDVM Voice 434.56000MHz +0.0000MHz, LU1ALG_Pi-Star_ND",
//      "!441216.80ND12332.40E&RNG0001/A=000010 440 Voice 435.95000MHz +0.0000MHz",
//      "=39.881N/32.68266E#73s de TA2BXX iGate and Digi",
//       "!12118.00ND03124.00E&/A=000000440 MMDVM Voice 438.80000MHz +0.0000MHz, BH4CKK_Pi-Star"

      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test : testCases) {
         Position position = PositionParser.parseUncompressed(test.getBytes());
         assertNotEquals(0, position.getLatitude());
         assertNotEquals(0, position.getLongitude());
         assertNotNull(position,"Position should not be null");
      }
   }

   @Test
   public void testParseUncompressedPacketWithAmbiguity() throws Exception {
      String[] testCases = new String[] {
            ";145.687.5_111111z4633.53N/00023.1 Er",
            ";145.687.5_111111z4633.53N/00023.  Er",
            ";145.687.5_111111z4633.53N/0002 .  Er",
            ";145.687.5_111111z4633.53N/000  .  Er",
            ";145.687.5_111111z4633.0 N/00023.1 Er",
            ";145.687.5_111111z4633.  N/00023.  Er",
            ";145.687.5_111111z463 .  N/0002 .  Er",
            ";145.687.5_111111z46  .  N/000  .  Er",
            // "!330 . N/965 . W_", // too short, but recoverable?
      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test : testCases) {
         Position position = PositionParser.parseUncompressed(test.getBytes());
         assertNotEquals(0, position.getLatitude());
         assertNotEquals(0, position.getLongitude());
         assertNotNull( position, "Position should not be null");
      }
   }

   @Test
   public void testCompressedPacket() throws Exception {
     // String test = "'vVtl \u001cK\\]G0JMS  SONNING COMMON=";
     // Position position = PositionParser.parseCompressed(test.getBytes(),0);
//
      String testb = "MN7UMK-10>APDW17:;G0TAI    *111111z/40iVN<gs-  !Ian, Great Linford, IO92PB";
      APRSPacket packet = Parser.parse(testb);

      PositionField field = (PositionField) packet.getAprsInformation().getAprsData(APRSTypes.T_POSITION);

      System.out.println(field);
    assertTrue(field.getPosition().getLatitude() != 0);

    //   String test = "`vJdl \u001c#/`\"4r}MB6IMK  Gateway   433.6625 Fusion + WiresX  C4FM  DN Mode._$";
    //  Position position = PositionParser.parseCompressed(test.getBytes(),0);

   }

   @Test
   public void testMICe() throws Exception {
      String testb = "MN7UMK-10>URPSVU:`vJdl \u001c#/`\"5?}MB6IMK  Gateway   433.6625 Fusion + WiresX  C4FM  DN Mode._$\n";
      APRSPacket packet = Parser.parse(testb);

      PositionField field = (PositionField) packet.getAprsInformation().getAprsData(APRSTypes.T_POSITION);

      System.out.println(field);
      assertTrue(field.getPosition().getLatitude() != 0);
   }

   @Test
   public void testMICe2() throws Exception {
      String testb = "G0TAI-9>URPTQZ:`vI\u001dl \u001cj/]\"4f}=\n";
      APRSPacket packet = Parser.parse(testb);

      PositionField field = (PositionField) packet.getAprsInformation().getAprsData(APRSTypes.T_POSITION);

      System.out.println(field);
      assertTrue(field.getPosition().getLatitude() != 0);
   }

   @Test
   public void testParseUncompressedCorruptRecoverablePacket() throws Exception {
      // Uncompressed test packets as seen on ARPS-IS
      String[] testCases = new String[] {
            "!4126.95N/09629.10W", // missing symbol.
            // "!4553.97NPZ01556.88E#/W3, APRS 9A2CD Conjo Zagreb 1", // winaprs 'PZ' - needs investigation
            // "!3237.30N/11525.44@\"#W# Mexicali, Baja * XE2DAK-2 / UHF", // investigate this too - longitude sign is @ for some reason
            // "!3302.51N/9655.48W_", // too short. (not enough decimals 9655.48W is really 09655.48W)
            // ";444.900+*111111z2857.60N/9952.40WrT141", // Another short longitude
            ";147.210VT*111111Z4403.55N/07215.46Wr147.210MHz T100 +060 R50m KB1FDA", // Time id 'Z' instead of 'z'

      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test : testCases) {
         Position position = PositionParser.parseUncompressed(test.getBytes());
         assertNotEquals(0, position.getLatitude());
         assertNotEquals(0, position.getLongitude());
         assertNotNull(position,"Position should not be null");
      }
   }

   @Test
   public void testParseInvalidUncompressedPacket() throws Exception {
      // NMEA test packets as seen on ARPS-IS
      String[] testCases = new String[] {
            "!4401.01N/08480.01W_ CLARE COUNTY WX", // Bad minute data
            "!0000.000/00000.000>000/000",
            "/235949h0000.000/00000.000<000/000/A=000000/TinyTrak3",
            "!0000.000/00000.000v000/000/TT3",
            "!0000.000/00000.000>000/000",
            "=5322.97N/000�3.42W-73 via ISS. Op John {UISS54}", // invalid fix (unicode replacement character used)
            "/08623.19WR203/051/caver mobile",

      };

      // Check that our list of NMEA test cases can be parsed ok
      for (String test : testCases) {
         try {
            PositionParser.parseUncompressed(test.getBytes());
            fail("Invalid GPS data was converted to a position object for uncompressed data: " + test);
         } catch (UnparsablePositionException e) {
            // test pass
         }
      }
   }

   @Test
   public void testParseUncompressedExtension() throws Exception {
      assertNull(PositionParser.parseUncompressedExtension("@101008z5924.70N/01629.20E".getBytes(), 8)); // No extension present

      // CourseAndSpeed
      CourseAndSpeedExtension e1 = (CourseAndSpeedExtension) PositionParser.parseUncompressedExtension("=6027.31N/02612.17E$306/034/A=000123 TB4JGH0 F7".getBytes(), 1);
      assertEquals(306, e1.getCourse());
      assertEquals(34, e1.getSpeed());
      e1 = (CourseAndSpeedExtension) PositionParser.parseUncompressedExtension("=3553.03N/11959.61E$342/000/A=000033 青岛BG4PEF欢迎您438500-5".getBytes(), 1);
      assertEquals(342, e1.getCourse());
      assertEquals(0, e1.getSpeed());

      // PHG
      PHGExtension e2 = (PHGExtension) PositionParser.parseUncompressedExtension("!5029.19N/00916.55E#PHG4800 APRS Fill-in-Digi Herchenhain 733m NN".getBytes(), 1);
      assertEquals(0, e2.getDirectivity());
      assertEquals(0, e2.getGain());
      assertEquals(2560, e2.getHeight());
      assertEquals(16, e2.getPower());

      e2 = (PHGExtension) PositionParser.parseUncompressedExtension("=4628.00N/00047.32WIPHG20302/AGWPE 2004-1108 /UI-V 2.03 {UIV32}".getBytes(), 1);
      assertEquals(0, e2.getDirectivity());
      assertEquals(3, e2.getGain());
      assertEquals(10, e2.getHeight());
      assertEquals(4, e2.getPower());
   }

   @Test
   public void parseMICe() throws Exception {
      Position p1 = PositionParser.parseMICe("`0(=m>%P/\"4N}  144.64MHz 05.53V".getBytes(), "RRUYV9");
      assertEquals(-1, p1.getAltitude());
      assertEquals(22.99483, p1.getLatitude(), 0.0000001);
      assertEquals(120.2055, p1.getLongitude(), 0.0000001);
      assertEquals(PositionTest.Ambiguity.NONE, p1.getPositionAmbiguity());

      p1 = PositionParser.parseMICe("`&.@oU<>/]\"6J}=".getBytes(), "U9UY18");
      assertEquals(-1, p1.getAltitude());
      assertEquals(59.98633, p1.getLatitude(), 0.0000001);
      assertEquals(10.3060, p1.getLongitude(), 0.0000001);
      assertEquals(PositionTest.Ambiguity.NONE, p1.getPositionAmbiguity());

      p1 = PositionParser.parseMICe("`0<Kl#>/\"4a}144.64Mhz-1W 3.97V 33.7C TF".getBytes(), "RTPTV7");
      assertEquals(-1, p1.getAltitude());
      assertEquals(24.07783, p1.getLatitude(), 0.0000001);
      assertEquals(120.54117, p1.getLongitude(), 0.0000001);
      assertEquals(PositionTest.Ambiguity.NONE, p1.getPositionAmbiguity());
   }

   /**
    * Check that a null sentence throws an exception.
    */
   @Test
   public void invalidNMEASentence() throws Exception {
      try {
         PositionParser.parseNMEA(null);
         fail("Invalid position did not throw exception");
      } catch (IllegalArgumentException e) {
         // Test successfull.
      }
   }

}
