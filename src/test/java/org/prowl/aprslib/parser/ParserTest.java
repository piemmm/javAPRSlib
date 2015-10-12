package org.prowl.aprslib.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTest {

   private static final byte[] AX25 = new byte[] { -126, -96, -92, -90, 64, 64, -32, -126, -124, 98, -122, -120, -118, 108, -116, -114, 104, -112, -110, -108, -28, -30, -126, -92, 64, 64, 64, 96, -88, 100, -120, -110, -114, -110, 97, 3, -16, 59, 80, 68, 49,
         66, 76, 85, 32, 32, 32, 42, 48, 48, 48, 48, 48, 48, 122, 53, 50, 48, 48, 46, 57, 57, 78, 47, 48, 48, 53, 49, 48, 46, 56, 48, 69, 45, 66, 97, 114, 116 };

   @Test
   public void parseWeatherUltimeter2000() throws Exception {
      APRSPacket packet = Parser.parse("X9XX-4>APRS,X6XXX-1,qAR,X6XXX-1:!!0000002202B3000027860334--------003D052300000000");
      assertEquals("Parsed packet is not of type weather", APRSTypes.WX, packet.getType());
   }

   @Test
   public void parseMessage() throws Exception {
      APRSPacket packet = Parser.parse("ANSRVR>APWW10,KJ4ERJ-15,TCPIP*,qAS,KJ4ERJ-15::EA1QV    :Now Monitoring ISS");
      assertEquals("Parsed packet is not of type message", APRSTypes.MESSAGE, packet.getType());
   }

   @Test
   public void parseTelemetry() throws Exception {
      APRSPacket packet = Parser.parse("KG4OFO-9>APRX27,TCPIP*,qAC,T2CAWEST:T#504,0.0,0.0,0.0,0.0,0.0,00000000");
      assertEquals("Parsed packet is not of type telemetry", APRSTypes.TELEMETRY, packet.getType());
   }

   @Test
   public void testPParseAX25() throws Exception {
      APRSPacket packet = Parser.parseAX25(AX25);
      assertEquals("AB1CDE-6", packet.getSourceCall());
      assertEquals("APRS", packet.getDestinationCall());
      assertEquals("FG4HIJ-2*,qAR,T2DIGI", packet.getDigiString());
      assertEquals(59, packet.getDti());
   }

}
