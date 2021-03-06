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
package tracerfx.controller;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import tracerfx.component.FileTab;
import tracerfx.component.ProjectTab;

/**
 *
 * @author Dariusz Lelek
 */
public class ProjectTabController extends TabController<ProjectTab>{
    private final String FXML = "tracerfx/fxml/ProjectTabFXML.fxml";
    private TabPane projectTabPane;


    public void setProjectTabPane(TabPane projectTabPane) {
        this.projectTabPane = projectTabPane;
    }
    
    public void addNewProject(String title) {
        final ProjectTab projectTab = new ProjectTab(title, getParent(FXML));
        projectTabPane.getTabs().add(projectTab.getTab());
        addItem(projectTab);
    }
    
    public boolean projectExists(String title){
        return getAllItems().stream().anyMatch(x -> title.equals(x.getTitle()));
    }
    
    public boolean tryToRemoveActiveProject(){
        ProjectTab activeProjectTab = getActiveItem();
        if(activeProjectTab.isNotEmpty()){
            projectTabPane.getTabs().remove(activeProjectTab.getTab());
            removeItem(activeProjectTab);
            ControllerFactory.getFileTabController().tryToRemoveFilesFromProject(activeProjectTab);
            return true;
        }
        return false;
    }

    public boolean hasAnyChildTabModified(ProjectTab projectTab) {
        return !getAllFileTabs(projectTab).stream().filter(x -> x.isModified()).collect(Collectors.toList()).isEmpty();
    }
    
    public List<FileTab> getAllFileTabs(ProjectTab projectTab){
        return ControllerFactory.getFileTabController().getAllItems()
                .stream().filter(x -> x.getProjectTab().equals(projectTab))
                .collect(Collectors.toList());
    }
    
    @Override
    public ProjectTab getActiveItem() {
        final Tab selectedTab = projectTabPane.getSelectionModel().getSelectedItem();
        return getAllItems().stream().filter(x -> x.getTab().equals(selectedTab)).findFirst().orElse(ProjectTab.EMPTY);   
    }
}
