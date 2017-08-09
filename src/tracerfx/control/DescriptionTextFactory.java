/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracerfx.control;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Dariusz Lelek
 */
public class DescriptionTextFactory {
    private static final int FONT_SIZE = 14;
    private final static String FONT_NAME = "Consolas";
    
    private static final Font FONT_BOLD = Font.font(FONT_NAME, FontWeight.BOLD, FONT_SIZE);
    private static final Font FONT_REGULAR = Font.font(FONT_NAME, FONT_SIZE);
    private static final Color COLOR = Color.RED;

    public static Text getText(String text, boolean bold) {
        Text t = new Text(text);

        if (bold) {
            t.setFont(FONT_BOLD);
            t.setFill(COLOR);
        } else {
            t.setFont(FONT_REGULAR);
        }

        return t;
    }
}
