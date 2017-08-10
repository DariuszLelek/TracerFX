/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracerfx.control;

import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Dariusz Lelek
 */
public class DescriptionControllerTest {
    /**
     * Test of getSplitString method, of class DescriptionController.
     */
    @Test
    public void testGetSplitString() {
        System.out.println("getSplitString");
        
        String lastSearchString = DescriptionController.SPLIT_CHAR_EXCAPE_MAP.entrySet().stream()
            .map(entry -> String.valueOf(entry.getKey())).collect(Collectors.joining(""));
        
        String expResult = DescriptionController.SPLIT_CHAR_EXCAPE_MAP.entrySet().stream()
            .map(entry -> entry.getValue()).collect(Collectors.joining(""));
        
        String result = DescriptionController.getSplitString(lastSearchString);
        assertEquals(expResult, result);
    }
    
}
