package org.prowl.aprslib.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CallsignTest2 {

   private static final byte[] AX25          = new byte[] { -126, -96, -92, -90, 64, 64, -32, -126, -124, 98, -122, -120, -118, 108, -116, -114, 104, -112, -110, -108, -28, -30, -126, -92, 64, 64, 64, 96, -88, 100, -120, -110, -114, -110, 97, 3, -16, 59, 80, 68, 49,
                                                   66, 76, 85, 32, 32, 32, 42, 48, 48, 48, 48, 48, 48, 122, 53, 50, 48, 48, 46, 57, 57, 78, 47, 48, 48, 53, 49, 48, 46, 56, 48, 69, 45, 66, 97, 114, 116 };

   private static final byte[] AX25_CALL_2   = new byte[] { -116, 102, -126, -124, -122, 64, 116 };

   private static final String CALL_1        = "AB1CDE";
   private static final String CALL_2        = "F3ABC-10";
   private static final String CALL_2_SSID_1 = "10";
   private static final String CALL_2_SSID_2 = "8";

   @Test
   public void testCallsignString() {
      Callsign c = new Callsign(CALL_1);
      assertEquals(CALL_1, c.getCallsign());

      c = new Callsign(CALL_2);
      assertEquals(CALL_2_SSID_1, c.getSsid());
   }

   @Test
   public void testCallsignByteArrayInt() {
      Callsign c = new Callsign(AX25, 0);
      assertEquals("APRS", c.getCallsign());
      c = new Callsign(AX25, 7);
      assertEquals(CALL_1, c.getCallsign());
   }

   @Test
   public void testSetCallsign() {
      Callsign c = new Callsign(CALL_1);
      assertEquals(CALL_1, c.getCallsign());

      c.setCallsign(CALL_2);
      assertEquals(CALL_2, c.getCallsign());

   }

   @Test
   public void testSetSsid() {
      Callsign c = new Callsign(CALL_2);
      assertEquals(CALL_2_SSID_1, c.getSsid());

      c.setSsid(CALL_2_SSID_2);
      assertEquals(CALL_2_SSID_2, c.getSsid());
   }

   @Test
   public void testToAX25() {
      Callsign c = new Callsign(CALL_2);
      assertArrayEquals(AX25_CALL_2, c.toAX25());
   }

}
