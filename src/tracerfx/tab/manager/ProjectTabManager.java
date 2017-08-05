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
package tracerfx.tab.manager;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import tracerfx.fxml.FXMLResourceLoader;
import tracerfx.tab.ProjectTab;

/**
 *
 * @author Dariusz Lelek
 */
public class ProjectTabManager extends Manager<ProjectTab>{
    private final String FXML = "tracerfx/fxml/ProjectTabFXML.fxml";
    private TabPane projectTabPane;


    public void setProjectTabPane(TabPane projectTabPane) {
        this.projectTabPane = projectTabPane;
    }
    
    public void addNewProject(String title){
        final ProjectTab projectTab = new ProjectTab(title, FXMLResourceLoader.getResource(getClass().getClassLoader(), FXML));
        projectTabPane.getTabs().add(projectTab.getTab());
        addItem(projectTab);
    }
    
    public boolean projectExists(String title){
        return getAllItems().stream().anyMatch(x -> title.equals(x.getTitle()));
    }
    
    public void removeProject(final Node root){
        final ProjectTab projectTabToRemove = getAllItems().stream()
                .filter(x -> x.getRoot().equals(root))
                .findFirst().orElse(ProjectTab.DEFAULT);

        projectTabPane.getTabs().remove(projectTabToRemove.getTab());
        removeItem(projectTabToRemove);
    }

    @Override
    public ProjectTab getActiveItem() {
        final Tab selectedTab = projectTabPane.getSelectionModel().getSelectedItem();
        return getAllItems().stream().filter(x -> x.getTab().equals(selectedTab)).findFirst().orElse(ProjectTab.DEFAULT);
    }
}
