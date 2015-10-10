/**
 *
 */
package org.prowl.aprslib.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ihawkins
 *
 */
public class APRSPacketTest {

   private static final String                TEST_PACKET       = "IS0AML>APRS,TCPIP*,qAC,ASSISI-IT:;EL-IS0AML*111111z3917.93N200905.68E0connection to IW0UQF-L closed";
   private static final String                TEST_CALL_1       = "AB1CDE-6";
   private static final String                TEST_CALL_2       = "APRS";
   private static final String                TEST_CALL_3       = "G0XYZ";
   private static final String                DIGI_CALL_1       = "FG4HIJ";
   private static final String                DIGI_CALL_2_QAC   = "qAC";
   private static final String                DIGI_CALL_2_QAR   = "qAR";
   private static final String                DIGI_CALL_2_QAS   = "qAS";
   private static final String                DIGI_CALL_2_QAO   = "qAO";

   private static final String                DIGI_CALL_3_IGATE = "T2DIGI";
   private static final String                DIGI_SSID_1       = "2";
   private static final byte[]                AX25              = new byte[] { -126, -96, -92, -90, 64, 64, -32, -126, -124, 98, -122, -120, -118, 108, -116, -114, 104, -112, -110, -108, -28, -30, -126, -92, 64, 64, 64, 96, -88, 100, -120, -110, -114, -110, 97, 3, -16, 59, 80, 68, 49,
                                                                      66, 76, 85, 32, 32, 32, 42, 48, 48, 48, 48, 48, 48, 122, 53, 50, 48, 48, 46, 57, 57, 78, 47, 48, 48, 53, 49, 48, 46, 56, 48, 69, 45, 66, 97, 114, 116 };
   private static final ArrayList<Digipeater> DIGIPEATERS       = new ArrayList<>();
   private static InformationField            INFORMATIONFIELD;
   private static APRSPacket                  APRSPACKET;

   @BeforeClass
   public static void setup() throws Exception {
      DIGIPEATERS.add(new Digipeater(DIGI_CALL_1 + "-" + DIGI_SSID_1 + "*"));
      DIGIPEATERS.add(new Digipeater(DIGI_CALL_2_QAR));
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
      assertEquals(TEST_CALL_3, APRSPacket.getBaseCall(TEST_CALL_3));
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getSsid(java.lang.String)}.
    */
   @Test
   public void testGetSsid() {
      assertEquals(DIGI_SSID_1, APRSPacket.getSsid(DIGI_CALL_1 + "-" + DIGI_SSID_1));
      assertEquals("0", APRSPacket.getSsid(TEST_CALL_3));
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getIgate()}.
    */
   @Test
   public void testGetIgate() {
      List<Digipeater> digis = new ArrayList<>();

      Digipeater qcode = new Digipeater(DIGI_CALL_2_QAR);
      digis.add(new Digipeater(DIGI_CALL_1 + "-" + DIGI_SSID_1 + "*"));
      digis.add(qcode);
      digis.add(new Digipeater(DIGI_CALL_3_IGATE));
      APRSPacket p = new APRSPacket(TEST_CALL_1, TEST_CALL_2, digis, INFORMATIONFIELD);
      assertEquals(DIGI_CALL_3_IGATE, p.getIgate());

      qcode.setCallsign(DIGI_CALL_2_QAS);
      assertEquals(DIGI_CALL_3_IGATE, p.getIgate());

      qcode.setCallsign(DIGI_CALL_2_QAC);
      assertEquals(DIGI_CALL_3_IGATE, p.getIgate());

      qcode.setCallsign(DIGI_CALL_2_QAO);
      assertEquals(DIGI_CALL_3_IGATE, p.getIgate());

      qcode.setCallsign("");
      assertEquals("", p.getIgate());

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
      assertEquals(DIGI_CALL_2_QAR, digipeaters.get(1).getCallsign());
      assertEquals(DIGI_CALL_3_IGATE, digipeaters.get(2).getCallsign());

      APRSPacket p = new APRSPacket(TEST_CALL_1, TEST_CALL_2, null, INFORMATIONFIELD);
      assertEquals("TCPIP", p.getDigipeaters().get(0).getCallsign());
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

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getLastUsedDigi()}.
    */
   @Test
   public void testGetLastUsedDigi() {
      assertEquals(DIGI_CALL_1, APRSPACKET.getLastUsedDigi());

      APRSPacket p = new APRSPacket(TEST_CALL_1, TEST_CALL_2, new ArrayList<Digipeater>(), INFORMATIONFIELD);
      assertNull(p.getLastUsedDigi());

   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getDigiString()}.
    */
   @Test
   public void testGetDigiString() {
      assertEquals(DIGI_CALL_1 + "-" + DIGI_SSID_1 + "*," + DIGI_CALL_2_QAR + "," + DIGI_CALL_3_IGATE, APRSPACKET.getDigiString());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getDti()}.
    */
   @Test
   public void testGetDti() {
      assertEquals(';', APRSPACKET.getDti());

      APRSPacket p = new APRSPacket(TEST_CALL_1, TEST_CALL_2, DIGIPEATERS, null);
      assertEquals(' ', p.getDti());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getAprsInformation()}.
    */
   @Test
   public void testGetAprsInformation() {
      assertEquals(INFORMATIONFIELD, APRSPACKET.getAprsInformation());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#isAprs()}.
    */
   @Test
   public void testIsAprs() {
      assertTrue(APRSPACKET.isAprs());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#hasFault()}.
    */
   @Test
   public void testHasFault() {
      assertFalse(APRSPACKET.hasFault());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#setHasFault(boolean)}.
    */
   @Test
   public void testSetHasFault() {
      APRSPacket p = new APRSPacket(TEST_CALL_1, TEST_CALL_2, DIGIPEATERS, INFORMATIONFIELD);
      assertFalse(p.hasFault());
      p.setHasFault(true);
      assertTrue(p.hasFault());
      p.setHasFault(false);
      assertFalse(p.hasFault());

   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getType()}.
    */
   @Test
   public void testGetType() throws Exception {
      assertNull(APRSPACKET.getType());
      APRSPacket p = Parser.parseBody(TEST_CALL_1, TEST_CALL_2, DIGIPEATERS, new String(INFORMATIONFIELD.rawBytes));
      assertEquals(APRSTypes.T_OBJECT, p.getType());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#setType(org.prowl.aprslib.parser.APRSTypes)}.
    */
   @Test
   public void testSetType() {
      APRSPacket p = new APRSPacket(TEST_CALL_1, TEST_CALL_2, DIGIPEATERS, INFORMATIONFIELD);
      assertNull(p.getType());
      p.setType(APRSTypes.T_KILL);
      assertEquals(APRSTypes.T_KILL, p.getType());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#getOriginalString()}.
    */
   @Test
   public void testGetOriginalString() throws Exception {
      APRSPacket p = Parser.parse(TEST_PACKET);
      assertEquals(TEST_PACKET, p.getOriginalString());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#setOriginalString(java.lang.String)}.
    */
   @Test
   public void testSetOriginalString() {
      APRSPacket p = new APRSPacket(TEST_CALL_1, TEST_CALL_2, DIGIPEATERS, INFORMATIONFIELD);
      assertNull(p.getOriginalString());
      p.setOriginalString(TEST_PACKET);
      assertEquals(TEST_PACKET, p.getOriginalString());
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#toString()}.
    */
   @Test
   public void testToString() {
      assertTrue("Contains IGATE", APRSPACKET.toString().contains(DIGI_CALL_3_IGATE));
      assertTrue("Contains DIGI Call 1", APRSPACKET.toString().contains(DIGI_CALL_1));
      assertTrue("Contains '>'", APRSPACKET.toString().contains(">"));
   }

   /**
    * Test method for {@link org.prowl.aprslib.parser.APRSPacket#toAX25Frame()}.
    */
   @Test
   public void testToAX25Frame() {
      // for (byte b : APRSPACKET.toAX25Frame()) {
      // System.out.print("," + b);
      // }
      // System.out.println("");
      assertArrayEquals(AX25, APRSPACKET.toAX25Frame());
   }

}
