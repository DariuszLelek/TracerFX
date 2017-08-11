/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracerfx.controller;

import tracerfx.controller.DescriptionController;
import tracerfx.controller.ControllerFactory;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import tracerfx.utilities.Strings;

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
        DescriptionController descriptionController = ControllerFactory.getDescriptionController();
        String lastSearchString = descriptionController.getSplitCharEscapeMap().entrySet().stream()
            .map(entry -> String.valueOf(entry.getKey())).collect(Collectors.joining(Strings.EMPTY.toString()));
        
        String expResult = descriptionController.getSplitCharEscapeMap().entrySet().stream()
            .map(entry -> entry.getValue()).collect(Collectors.joining(Strings.EMPTY.toString()));
        
        String result = descriptionController.getSplitString(lastSearchString);
        assertEquals(expResult, result);
    }
    
}
