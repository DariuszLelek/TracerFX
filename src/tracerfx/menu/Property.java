/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracerfx.menu;

/**
 *
 * @author Dariusz Lelek
 */
public enum Property {
    LOAD_ON_CONTENT_FOCUS("load_on_content_focus", "false"),
    THREAD_FILES_MOD_CHECK_DELAY_S("thread_file_check_delay", "5"),
    MODIFIED_TAB_COLOR("modified_tab_color", "#b3f984");
    
    private final String key, defaultValue;

    private Property(String key, String value) {
        this.key = key;
        this.defaultValue = value;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
    
    
}
