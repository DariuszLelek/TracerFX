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

import java.io.File;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Tab;
import javafx.scene.web.WebView;
import tracerfx.tab.FileTab;
import tracerfx.tab.ProjectTab;
import tracerfx.control.FileContent.FileContentController;
import tracerfx.control.FileContent.FileContentProperty;

/**
 *
 * @author Dariusz Lelek
 */
public class FileTabManager extends Manager<FileTab>{
    private final String FXML = "tracerfx/fxml/FileTabFXML.fxml";
    private WebView txtLineDescription;
    private final FileContentController fileManager = new FileContentController();
     
    public void addNewFileToProject(File file, ProjectTab projectTab){
        FileTab fileTab = new FileTab(fileManager.getFileContent(file), getParent(FXML), projectTab);
        
        projectTab.getFileTabPane().getTabs().add(fileTab.getTab());
        addItem(fileTab);
    }

    public WebView getTxtLineDescription() {
        return txtLineDescription;
    }
    
    public IntegerProperty getMonitoredFilesIntProperty() {
        return fileManager.getMonitoredFilesIntProperty();
    }
    
    public FileContentProperty getFileContentProperty(){
        return fileManager.getFileContentProperty();
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
        
        // In situation when project has no files - getActiveItem will return dummy if other project have files
        if (activeFileTab.isNotEmpty()) {
            ProjectTab activeProjectTab = ManagerFactory.getProjectTabManager().getActiveItem();
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
        final Tab selectedTab = ManagerFactory.getProjectTabManager().getActiveItem().getActiveFileTab();
        return getAllItems().stream().filter(x -> x.getTab().equals(selectedTab)).findFirst().orElse(FileTab.EMPTY);  
    }

}
