/*
 * Copyright 2017 Dariusz Lelek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tracerfx.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import tracerfx.tab.manager.ManagerFactory;

/**
 *
 * @author Dariusz Lelek
 */
public class DescriptionController {
    private static String lastDescription = "";
    private static String lastSearchString = "";
    
    final static Map<Character, String> SPLIT_CHAR_EXCAPE_MAP;
    
    static{
        SPLIT_CHAR_EXCAPE_MAP = new HashMap();
        SPLIT_CHAR_EXCAPE_MAP.put('-', "\\-");
        SPLIT_CHAR_EXCAPE_MAP.put('+', "\\+");
        SPLIT_CHAR_EXCAPE_MAP.put('/', "\\/");
        SPLIT_CHAR_EXCAPE_MAP.put('*', "\\*");
        SPLIT_CHAR_EXCAPE_MAP.put('^', "\\^");
        SPLIT_CHAR_EXCAPE_MAP.put('x', "\\x");
    }

    public static final ChangeListener CHANGE_LISTENER_TAB_SWITCH = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
        setDescription("");
    };

    public static final ChangeListener CHANGE_LISTENER_LINE_CHANGE = (ChangeListener) new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            final String text = newValue != null ? newValue.toString() : "";
            setDescription(text);
        }
    };
    
    public static void setDescription(String description) {
        if (!lastDescription.equals(description)) {
            lastDescription = description;
            String lastSearch = ManagerFactory.getFileTabManager().getActiveItem().getFileContent().getLastSearchProperty().get();
            if (!lastSearchString.equals(lastSearch)) {
                lastSearchString = lastSearch;
            }
            setDescription();
        }
    }
    
    private static void setDescription(){    
       ManagerFactory.getFileTabManager().getTxtLineDescription().getEngine()
               .loadContent(String.join(getSearchtringStyled(), getDescriptionParts()));
    }
    
    private static String getSearchtringStyled(){
        return "<b><font color=\"red\">" + lastSearchString + "</font></b>";
    }
    
    private static List<String> getDescriptionParts(){
        return lastSearchString.isEmpty() ? Arrays.asList(lastDescription) : Arrays.asList(lastDescription.split(getSplitString(lastSearchString)));
    }
    
    static String getSplitString(String lastSearchString){
        StringBuilder sb = new StringBuilder();
        
        for(char character : lastSearchString.toCharArray()){
            sb.append(SPLIT_CHAR_EXCAPE_MAP.containsKey(character) ? SPLIT_CHAR_EXCAPE_MAP.get(character) : character);
        }
        
        return sb.toString();
    }
}
