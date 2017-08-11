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
import javafx.scene.layout.Pane;
import tracerfx.menu.options.Option;
import tracerfx.menu.options.Property;
import tracerfx.tab.controller.ControllerFactory;
import tracerfx.util.Strings;

/**
 *
 * @author Dariusz Lelek
 */
public class ProjectTab extends CustomTab {
    public static final ProjectTab EMPTY = new ProjectTab(Strings.EMPTY.toString(), new Pane());
    private final TabPane fileTabPane;
    
    public ProjectTab(String title, Parent root) {
        super(title, root);
        fileTabPane = findTabPaneForNode(root);
    }
    
    @Override
    public boolean isNotEmpty(){
        return !this.equals(EMPTY);
    }

    public TabPane getFileTabPane() {
        return fileTabPane;
    }
    
    public Tab getActiveFileTab(){
        return fileTabPane.getSelectionModel().getSelectedItem();
    }
    
    @Override
    protected void processModified(){
        this.tab.setStyle(ControllerFactory.getProjectTabController().hasAnyChildTabModified(this) ?
                Strings.FX_BACK_COLOR.toString() + Option.getString(Property.MODIFIED_TAB_COLOR) : Strings.EMPTY.toString());
    }

    private TabPane findTabPaneForNode(Parent parent) {
        return (TabPane) parent.getChildrenUnmodifiable().stream().filter(n -> n instanceof TabPane).findFirst().orElse(new TabPane());
    }

}
