/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracerfx.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Dariusz Lelek
 */
public class FileUtilityTest {  
    private static File newFile;
    private static String invalidLine = Strings.EMPTY.toString();
    
    public FileUtilityTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        newFile = new File("testFile.txt");
        if(!newFile.exists()){
            newFile.createNewFile();
        }   
        
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, FileUtility.excludedASCII.length).forEach(i -> sb.append(String.valueOf(Character.toChars(0))));
        invalidLine = sb.toString();
    }
    
    @AfterClass
    public static void tearDownClass() {
        if(newFile.exists()){
            newFile.delete();
        }   
    }

    /**
     * Test of getFileLineNumber method, of class FileUtility.
     */
    @Test
    public void testGetFileLineNumber() {
        System.out.println("getFileLineNumber");
        int result = FileUtility.getFileLineNumber(null);
        assertEquals(-1, result);
        result = FileUtility.getFileLineNumber(newFile);
        assertEquals(0, result);
    }

    /**
     * Test of getFileLines method, of class FileUtility.
     */
    @Test
    public void testGetFileLines() {
        System.out.println("getFileLines");  
        List<String> result = FileUtility.getFileLines(null);
        assertNotNull(result);
        assertEquals(result.size(), 0);
        result = FileUtility.getFileLines(newFile);
        assertNotNull(result);
        assertEquals(result.size(), 0);
    }

    /**
     * Test of validLine method, of class FileUtility.
     */
    @Test
    public void testValidLine() {
        System.out.println("validLine");
        assertEquals(false, FileUtility.validLine(invalidLine));
    }

    /**
     * Test of containsExcludedASCII method, of class FileUtility.
     */
    @Test
    public void testContainsExcludedASCII() {
        System.out.println("containsExcludedASCII");
        assertEquals(true, FileUtility.containsExcludedASCII(invalidLine));
    }
    
}
