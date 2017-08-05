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
import javafx.scene.control.Tab;
import tracerfx.tab.FileTab;
import tracerfx.tab.ProjectTab;

/**
 *
 * @author Dariusz Lelek
 */
public class FileTabManager extends Manager<FileTab>{
    private final String FXML = "tracerfx/fxml/FileTabFXML.fxml";
     
    public void addNewFileToProject(File file, ProjectTab projectTab){
        FileTab fileTab = new FileTab(file, getParent(FXML));
        projectTab.getFileTabPane().getTabs().add(fileTab.getTab());
        addItem(fileTab);
    }

    @Override
    protected FileTab getActiveItem() {
        final Tab selectedTab = ManagerFactory.getProjectTabManager().getActiveItem().getActiveFileTab();
        return getAllItems().stream().filter(x -> x.getTab().equals(selectedTab)).findFirst().orElse(null);  
    }

}
