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

import java.io.File;
import javafx.scene.Node;

/**
 *
 * @author Dariusz Lelek
 */
public class FileTab extends CustomTab{
    public final static FileTab DUMMY = new FileTab();
    
    private boolean followTrail = false;
    private final File file;
    private int totalFileLines;
    
    private FileTab(){
        file = null;
    }

    public FileTab(File file, Node content) {
        super(file.getName(), content);
        
        this.file = file;
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

    public int getTotalFileLines() {
        return totalFileLines;
    }
    
}
