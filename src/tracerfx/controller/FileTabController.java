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

import java.io.File;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Tab;
import javafx.scene.web.WebView;
import tracerfx.component.FileTab;
import tracerfx.component.ProjectTab;
import tracerfx.component.FileContentProperty;

/**
 *
 * @author Dariusz Lelek
 */
public class FileTabController extends TabController<FileTab>{ 
    private final String FXML = "tracerfx/fxml/FileTabFXML.fxml";
    
    private WebView txtLineDescription;
     
    public void addNewFileToProject(File file, ProjectTab projectTab){
        FileTab fileTab = new FileTab(ControllerFactory.getFileContentController().getFileContent(file), getParent(FXML), projectTab);
        
        projectTab.getFileTabPane().getTabs().add(fileTab.getTab());
        addItem(fileTab);
    }

    public WebView getTxtLineDescription() {
        return txtLineDescription;
    }
    
    public IntegerProperty getMonitoredFilesIntProperty() {
        return ControllerFactory.getFileContentController().getMonitoredFilesIntProperty();
    }
    
    public FileContentProperty getFileContentProperty(){
        return ControllerFactory.getFileContentController().getFileContentProperty();
    }
    
    public void processSearch(String searchString){
        getActiveItem().getFileContent().processSearch(searchString);
    }
    
    public void tryToRemoveFilesFromProject(ProjectTab projectTab){
        removeItems(getAllItems().stream()
                .filter(x -> projectTab.getFileTabPane().getTabs().contains(x.getTab()))
                .collect(Collectors.toList()));        
    }
   
    public boolean tryToRemoveFileFromActiveProject() {
        FileTab activeFileTab = getActiveItem();
        
        if (activeFileTab.isNotEmpty()) {
            ControllerFactory.getFileContentController().removeFileContent(activeFileTab.getFileContent());
            ProjectTab activeProjectTab = ControllerFactory.getProjectTabController().getActiveItem();
            activeProjectTab.getFileTabPane().getTabs().remove(activeFileTab.getTab());
            removeItem(activeFileTab);
            return true;
        }
        return false;
    }
    
    public void markTabAsModified(File file){
        getAllItems().stream().filter(x -> x.getFileContent().getFile().equals(file)).forEach(x -> {
            x.setTabModified(true);
        });
    }

    public void setTxtLineDescription(WebView txtLineDescription) {
        this.txtLineDescription = txtLineDescription;
    }

    @Override
    public FileTab getActiveItem() {
        final Tab selectedTab = ControllerFactory.getProjectTabController().getActiveItem().getActiveFileTab();
        return getAllItems().stream().filter(x -> x.getTab().equals(selectedTab)).findFirst().orElse(FileTab.EMPTY);  
    }

}
