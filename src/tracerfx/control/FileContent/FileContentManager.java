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
package tracerfx.control.FileContent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;

/**
 *
 * @author Dariusz Lelek
 */
public class FileContentManager {
    private FileContent newFileContent;
    private final List<FileContent> filesContent = new ArrayList<>();
    
    public ListProperty<String> getContentProperty(){
        return newFileContent.getContentProperty();
    }

    public FileContent getFileContent(final File activeFile){
        FileContent fileContent = new FileContent(activeFile);
        newFileContent = fileContent;
        filesContent.add(fileContent);
        return fileContent;
    }
}
