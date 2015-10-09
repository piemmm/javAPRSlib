/**
 *
 */
package org.prowl.aprslib.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ihawkins
 *
 */
public class APRSPacketTest {

   private static final String                TEST_CALL_1       = "AB1CDE-6";
   private static final String                TEST_CALL_2       = "APRS";
   private static final String                DIGI_CALL_1       = "FG4HIJ";
   private static final String                DIGI_CALL_2_Q     = "qAC";
   private static final String                DIGI_CALL_3_IGATE = "T2DIGI";
   private static final String                DIGI_SSID_1       = "2";
   private static final ArrayList<Digipeater> DIGIPEATERS       = new ArrayList<>();
   private static InformationField            INFORMATIONFIELD;
   private static APRSPacket                  APRSPACKET;

   @BeforeClass
   public static void setup() throws Exception {
      DIGIPEATERS.add(new Digipeater(DIGI_CALL_1 + "-" + DIGI_SSID_1));
      DIGIPEATERS.add(new Digipeater(DIGI_CALL_2_Q));
      DIGIPEATERS.add(new Digipeater(DIGI_CALL_3_IGATE));
      INFORMATIONFIELD = new ObjectPacket(";PD1BLU   *000000z5200.99N/00510.80E-Bart".getBytes());
      APRSPACKET = new APRSPacket(TEST_CALL_1, TEST_CALL_2, DIGIPEATERS, INFORMATIONFIELD);
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#APRSPacket(java.lang.String, java.lang.String, java.util.ArrayList, org.prowl.aprslib.parser.InformationField)}.
    */
   @Test
   public void testAPRSPacket() {
      assertEquals(DIGI_CALL_1, APRSPACKET.getDigipeaters().get(0).getCallsign());
      assertEquals(DIGI_SSID_1, APRSPACKET.getDigipeaters().get(0).getSsid());

   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getBaseCall(java.lang.String)}.
    */
   @Test
   public void testGetBaseCall() {
      assertEquals(DIGI_CALL_1, APRSPacket.getBaseCall(DIGI_CALL_1 + "-" + DIGI_SSID_1));
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getSsid(java.lang.String)}.
    */
   @Test
   public void testGetSsid() {
      assertEquals(DIGI_SSID_1, APRSPacket.getSsid(DIGI_CALL_1 + "-" + DIGI_SSID_1));
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getIgate()}.
    */
   @Test
   public void testGetIgate() {
      assertEquals(DIGI_CALL_3_IGATE, APRSPACKET.getIgate());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getSourceCall()}.
    */
   @Test
   public void testGetSourceCall() {
      assertEquals(TEST_CALL_1, APRSPACKET.getSourceCall());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getDestinationCall()}.
    */
   @Test
   public void testGetDestinationCall() {
      assertEquals(TEST_CALL_2, APRSPACKET.getDestinationCall());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getDigipeaters()}.
    */
   @Test
   public void testGetDigipeaters() {
      List<Digipeater> digipeaters = APRSPACKET.getDigipeaters();
      assertEquals(DIGIPEATERS.size(), digipeaters.size());
      assertEquals(DIGI_CALL_1, digipeaters.get(0).getCallsign());
      assertEquals(DIGI_CALL_2_Q, digipeaters.get(1).getCallsign());
      assertEquals(DIGI_CALL_3_IGATE, digipeaters.get(2).getCallsign());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#setDigipeaters(java.util.ArrayList)}.
    */
   @Test
   public void testSetDigipeaters() {
      APRSPacket p = new APRSPacket(TEST_CALL_1, TEST_CALL_2, DIGIPEATERS, INFORMATIONFIELD);

      p.setDigipeaters(new ArrayList());
      assertEquals(0, p.getDigipeaters().size());

      p.setDigipeaters(null);
      assertNull(p.getDigipeaters());
   }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getLastUsedDigi()}.
   // */
   // @Test
   // public void testGetLastUsedDigi() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getDigiString()}.
   // */
   // @Test
   // public void testGetDigiString() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getDti()}.
   // */
   // @Test
   // public void testGetDti() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getAprsInformation()}.
   // */
   // @Test
   // public void testGetAprsInformation() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#isAprs()}.
   // */
   // @Test
   // public void testIsAprs() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#hasFault()}.
   // */
   // @Test
   // public void testHasFault() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#setHasFault(boolean)}.
   // */
   // @Test
   // public void testSetHasFault() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getType()}.
   // */
   // @Test
   // public void testGetType() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#setType(org.prowl.aprslib.parser.APRSTypes)}.
   // */
   // @Test
   // public void testSetType() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getOriginalString()}.
   // */
   // @Test
   // public void testGetOriginalString() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#setOriginalString(java.lang.String)}.
   // */
   // @Test
   // public void testSetOriginalString() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#toString()}.
   // */
   // @Test
   // public void testToString() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link org.prowl.aprslib.parser.APRSPacket#toAX25Frame()}.
   // */
   // @Test
   // public void testToAX25Frame() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#Object()}.
   // */
   // @Test
   // public void testObject() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#getClass()}.
   // */
   // @Test
   // public void testGetClass() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#hashCode()}.
   // */
   // @Test
   // public void testHashCode() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
   // */
   // @Test
   // public void testEquals() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#clone()}.
   // */
   // @Test
   // public void testClone() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#toString()}.
   // */
   // @Test
   // public void testToString1() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#notify()}.
   // */
   // @Test
   // public void testNotify() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#notifyAll()}.
   // */
   // @Test
   // public void testNotifyAll() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#wait(long)}.
   // */
   // @Test
   // public void testWaitLong() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#wait(long, int)}.
   // */
   // @Test
   // public void testWaitLongInt() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#wait()}.
   // */
   // @Test
   // public void testWait() {
   // fail("Not yet implemented");
   // }
   //
   // /**
   // * Test method for {@link java.lang.Object#finalize()}.
   // */
   // @Test
   // public void testFinalize() {
   // fail("Not yet implemented");
   // }

}
