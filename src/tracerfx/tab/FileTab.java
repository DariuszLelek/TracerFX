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
import javafx.scene.control.ListView;
import tracerfx.control.FileContent.FileContent;

/**
 *
 * @author Dariusz Lelek
 */
public class FileTab extends CustomTab{
    public final static FileTab DUMMY = new FileTab();
    
    private boolean followTrail = false;
    private final FileContent fileContent;
    private ListView listView;
    
    private FileTab(){
        fileContent = new FileContent();
    }

    public FileTab(FileContent fileContent, Node content) {
        super(fileContent.getFile().getName(), content);
        
        this.fileContent = fileContent;
    }

    public FileContent getFileContent() {
        return fileContent;
    }
    
    public boolean isNotDummy(){
        return !this.equals(DUMMY);
    }

    public boolean isFollowTrail() {
        return followTrail;
    }

    public void setFollowTrail(boolean followTrail) {
        this.followTrail = followTrail;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
