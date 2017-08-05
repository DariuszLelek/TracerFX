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
package tracerfx.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dariusz Lelek
 */
public class FileManager {
    private File activeFile;
    private final Map<File, Collection<String>> filesContent = new HashMap<>();

    public Collection<String> getActiveFileContent(){
        return filesContent.containsKey(activeFile) ? filesContent.get(activeFile) : new ArrayList<>();
    }

    public void setActiveFile(final File activeFile) {
        insertFile(activeFile);
        this.activeFile = activeFile;
    }
    
    private void insertFile(final File file){
        if(!filesContent.containsKey(file)){
            filesContent.put(file, FileUtility.getFileLines(file));
        }
    }
    
    
    
}
