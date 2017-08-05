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
package tracerfx.tab;

import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 *
 * @author Dariusz Lelek
 */
public class ProjectTab extends CustomTab {
    public static final ProjectTab DEFAULT = new ProjectTab();
    private final TabPane fileTabPane;
 
    // dummy projectTab
    private ProjectTab(){
        fileTabPane = new TabPane();
    }
    
    public ProjectTab(String title, Parent root) {
        super(title, root);
        
        fileTabPane = findTabPaneForNode(root);
    }
    
    public boolean isValidTab(){
        return !title.isEmpty();
    }

    public TabPane getFileTabPane() {
        return fileTabPane;
    }
    
    public Tab getActiveFileTab(){
        return fileTabPane.getSelectionModel().getSelectedItem();
    }

    private TabPane findTabPaneForNode(Parent parent) {
        return (TabPane) parent.getChildrenUnmodifiable().stream().filter(n -> n instanceof TabPane).findFirst().orElse(new TabPane());
    }

}
