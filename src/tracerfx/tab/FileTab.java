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

import javafx.scene.Node;
import tracerfx.control.FileContent.FileContent;
import tracerfx.util.Strings;

/**
 *
 * @author Dariusz Lelek
 */
public class FileTab extends CustomTab{
    public final static FileTab EMPTY = new FileTab();
    
    private final FileContent fileContent;
    private final ProjectTab projectTab;
    private boolean modified = false;
    
    private FileTab(){
        fileContent = new FileContent();
        projectTab = ProjectTab.EMPTY;
    }

    public FileTab(FileContent fileContent, Node content, ProjectTab projectTab) {
        super(fileContent.getFile().getName(), content);
        
        this.projectTab = projectTab;
        this.fileContent = fileContent;
    }

    public FileContent getFileContent() {
        return fileContent;
    }
    
    @Override
    public boolean isNotEmpty(){
        return !this.equals(EMPTY);
    }

    public ProjectTab getProjectTab() {
        return projectTab;
    }

    public boolean isFollowTrail() {
        return fileContent.isFollowTrail();
    }

    public void setFollowTrail(boolean followTrail) {
        this.fileContent.setFollowTrail(followTrail);
    }

    public synchronized boolean isModified() {
        return modified;
    }
    
    @Override
    protected void processModified(){
        tab.setStyle(this.modified ? modifiedStyle : Strings.EMPTY.toString());
    }
    
    public synchronized void setTabModified(boolean modified) {
        this.modified = modified;
        
        this.processModified();
        projectTab.processModified();
    }
}
