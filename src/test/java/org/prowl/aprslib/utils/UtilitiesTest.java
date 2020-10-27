/**
 *
 */
package org.prowl.aprslib.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Permission;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ihawkins
 *
 */
public class UtilitiesTest {

   private static PrintStream sysOut = System.out;

   @Before
   public void setup() {
      System.setSecurityManager(new NoExitSecurityManager());
   }

   @After
   public void after() {
      System.setSecurityManager(null);
   }

   @Test
   public void testMain() {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      System.setOut(new PrintStream(out));
      try {
         Utilities.main(new String[0]);
         fail("Did not exit");
      } catch (ExitException e) {
         assertTrue(out.toString().contains("Usage:"));
      }

      out.reset();
      try {
         Utilities.main(new String[] { "F0OOO" });
         fail("Did not exit");
      } catch (ExitException e) {
         assertTrue(out.toString().contains("Hash: 13725"));
      }
      System.setOut(sysOut);
   }

   /**
    * Test method for
    * {@link org.prowl.aprslib.utils.Utilities#doHash(java.lang.String)}.
    */
   @Test
   public void testDoHash() {
      assertEquals(29652, Utilities.doHash("ABC1DEF"));
      assertEquals(13968, Utilities.doHash("G0ABC"));
      assertEquals(18597, Utilities.doHash("KA3BCD"));
      assertEquals(14228, Utilities.doHash("F4ABC"));
      assertEquals(14228, Utilities.doHash("f4abc"));
      assertEquals(14228, Utilities.doHash("F4ABC-1"));
      assertEquals(14228, Utilities.doHash("F4ABC-0"));
      assertEquals(14228, Utilities.doHash("F4ABC-10"));

   }

   /**
    * Test method for {@link org.prowl.aprslib.utils.Utilities#ktsToMph(int)}.
    */
   @Test
   public void testKtsToMph() {
      assertEquals(46, Utilities.ktsToMph(40));
   }

   /**
    * Test method for {@link org.prowl.aprslib.utils.Utilities#kntsToKmh(int)}.
    */
   @Test
   public void testKntsToKmh() {
      assertEquals(74, Utilities.kntsToKmh(40));
   }

   /**
    * Test method for
    * {@link org.prowl.aprslib.utils.Utilities#metersToMiles(double)}.
    */
   @Test
   public void testMetersToMiles() {
      assertEquals(2.4854, Utilities.metersToMiles(4000), 0.0001d);
   }

   /**
    * Test method for
    * {@link org.prowl.aprslib.utils.Utilities#metersToKilometers(double)}.
    */
   @Test
   public void testMetersToKilometers() {
      assertEquals(5, Utilities.metersToKilometers(5000), 0.00001d);

   }

   @Test
   public void testDegressToCardinal() {
      assertEquals("N", Utilities.degressToCardinal(11.24));
      assertEquals("NNE", Utilities.degressToCardinal(11.25));
      assertEquals("NE", Utilities.degressToCardinal(55));
      assertEquals("ENE", Utilities.degressToCardinal(75));

      assertEquals("E", Utilities.degressToCardinal(79));
      assertEquals("ESE", Utilities.degressToCardinal(102));
      assertEquals("SE", Utilities.degressToCardinal(124));
      assertEquals("SSE", Utilities.degressToCardinal(160));

      assertEquals("S", Utilities.degressToCardinal(175));
      assertEquals("SSW", Utilities.degressToCardinal(192));
      assertEquals("SW", Utilities.degressToCardinal(214));
      assertEquals("WSW", Utilities.degressToCardinal(237));

      assertEquals("W", Utilities.degressToCardinal(259));
      assertEquals("WNW", Utilities.degressToCardinal(282));
      assertEquals("NW", Utilities.degressToCardinal(305));
      assertEquals("NNW", Utilities.degressToCardinal(327));

      assertEquals("N", Utilities.degressToCardinal(350));
   }

   protected static class ExitException extends SecurityException {
      public final int status;

      public ExitException(int status) {
         super();
         this.status = status;
      }
   }

   private static class NoExitSecurityManager extends SecurityManager {
      @Override
      public void checkPermission(Permission perm) {
      }

      @Override
      public void checkPermission(Permission perm, Object context) {
      }

      @Override
      public void checkExit(int status) {
         super.checkExit(status);
         throw new ExitException(status);
      }
   }

}
