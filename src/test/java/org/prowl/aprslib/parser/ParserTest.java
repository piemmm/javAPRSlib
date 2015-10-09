package org.prowl.aprslib.parser;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

   @Test
   public void parseWeatherUltimeter2000() throws Exception {
      APRSPacket packet = Parser.parse("X9XX-4>APRS,X6XXX-1,qAR,X6XXX-1:!!0000002202B3000027860334--------003D052300000000");
      Assert.assertEquals("Parsed packet is not of type weather", packet.getType(), APRSTypes.T_WX);
   }

}
