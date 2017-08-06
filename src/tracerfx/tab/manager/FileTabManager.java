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
import javafx.beans.property.ListProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import tracerfx.tab.FileTab;
import tracerfx.tab.ProjectTab;
import tracerfx.control.FileContent.FileContentManager;

/**
 *
 * @author Dariusz Lelek
 */
public class FileTabManager extends Manager<FileTab>{
    private final String FXML = "tracerfx/fxml/FileTabFXML.fxml";
    private TextArea txtLineDescription;
    private final FileContentManager fileManager = new FileContentManager();
   
     
    public void addNewFileToProject(File file, ProjectTab projectTab){
        FileTab fileTab = new FileTab(fileManager.getFileContent(file), getParent(FXML));
        
        projectTab.getFileTabPane().getTabs().add(fileTab.getTab());
        addItem(fileTab);
    }

    public TextArea getTxtLineDescription() {
        return txtLineDescription;
    }
    
    public ListProperty<String> getContentProperty(){
        return fileManager.getContentProperty();
    }
    
    public void search(String searchString){
        getActiveItem().getFileContent().processFileModified();//search(searchString);
        
        
    }
    
    public void tryToRemoveFilesFromProject(ProjectTab projectTab){
        removeItems(getAllItems().stream()
                .filter(x -> projectTab.getFileTabPane().getTabs().contains(x.getTab()))
                .collect(Collectors.toList()));        
    }
   
    public boolean tryToRemoveFileFromActiveProject() {
        FileTab activeFileTab = getActiveItem();
        
        // In situation when project has not files - getActiveItem will return dummy if other project have files
        if (activeFileTab.isNotDummy()) {
            ProjectTab activeProjectTab = ManagerFactory.getProjectTabManager().getActiveItem();
            activeProjectTab.getFileTabPane().getTabs().remove(activeFileTab.getTab());
            removeItem(activeFileTab);
            return true;
        }
        return false;
    }

    public void setTxtLineDescription(TextArea txtLineDescription) {
        this.txtLineDescription = txtLineDescription;
    }

    @Override
    public FileTab getActiveItem() {
        final Tab selectedTab = ManagerFactory.getProjectTabManager().getActiveItem().getActiveFileTab();
        return getAllItems().stream().filter(x -> x.getTab().equals(selectedTab)).findFirst().orElse(FileTab.DUMMY);  
    }

}
