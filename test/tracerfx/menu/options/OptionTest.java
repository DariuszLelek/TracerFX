/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracerfx.menu.options;

import tracerfx.menu.Property;
import tracerfx.menu.Option;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import tracerfx.utilities.Strings;

/**
 *
 * @author Dariusz Lelek
 */
public class OptionTest {
    
    public OptionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getInt method, of class Option.
     */
    @Test
    public void testGetInt() {
        System.out.println("getInt");
        assertEquals(Integer.MAX_VALUE ,Option.getInt(null));
        assertNotEquals(Integer.MAX_VALUE, Option.getInt(Property.THREAD_FILES_MOD_CHECK_DELAY_MS));
        
    }

    /**
     * Test of getBoolean method, of class Option.
     */
    @Test
    public void testGetBoolean() {
        System.out.println("getBoolean");
        assertEquals(null ,Option.getBoolean(null));
        assertNotEquals(null, Option.getBoolean(Property.LOAD_ON_CONTENT_FOCUS));
    }

    /**
     * Test of getString method, of class Option.
     */
    @Test
    public void testGetString() {
        System.out.println("getString");
        assertEquals(Strings.EMPTY.toString(), Option.getString(null));
        assertNotEquals(Strings.EMPTY.toString(), Option.getString(Property.LOAD_ON_CONTENT_FOCUS));
    }
    
}
