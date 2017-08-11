/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracerfx.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import tracerfx.utilities.Strings;

/**
 *
 * @author Dariusz Lelek
 */
public class Option {
    private final static String FILE = "config.properties";
    
    private static Properties PROPERTIES = null;
    
    public static int getInt(Property property){
        String propertyValue = getString(property);
        return tryParseInt(propertyValue) ? Integer.parseInt(propertyValue) : Integer.MAX_VALUE;
    }
    
    public static Boolean getBoolean(Property property){
        String propertyValue = getString(property);
        return tryParseBoolean(propertyValue) ? Boolean.parseBoolean(propertyValue) : null;
    }
    
    public static String getString(Property property){
        return property != null ? get(property) : Strings.EMPTY.toString();
    }

    private static String get(Property property) {
        return getProperties().contains(property.getKey()) ? PROPERTIES.getProperty(property.getKey()) : getDefaultAfterSave(property);
    }
    
    private static String getDefaultAfterSave(Property property) {
        PROPERTIES.setProperty(property.getKey(), property.getDefaultValue());
        store();
        return property.getDefaultValue();
    }
    
    private static Properties getProperties(){
        return PROPERTIES != null ? PROPERTIES : load();
    }
    
    private static void store() {
        try {
            PROPERTIES.store(new FileOutputStream(getPropertyFile()), null);
        } catch (FileNotFoundException ex) {
            // TODO
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static boolean tryParseBoolean(String value) {
        return "true".equals(value) || "false".equals(value);
    }

    private static Properties load(){
        try {
            PROPERTIES = new Properties();
            PROPERTIES.load(new FileInputStream(getPropertyFile()));
        } catch (FileNotFoundException ex) {
            // TODO
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return PROPERTIES;
    }

    private static File getPropertyFile() throws IOException {
        File file = new File(FILE);

        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }
}
