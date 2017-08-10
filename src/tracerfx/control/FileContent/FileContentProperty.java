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

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Dariusz Lelek
 */
public class FileContentProperty {

    private final FileContent fileContent;

    public FileContentProperty(FileContent fileContent) {
        this.fileContent = fileContent;
    }

    public SimpleStringProperty getLastSearchProperty() {
        return fileContent.getLastSearchProperty();
    }

    public ObservableList<String> getOriginalContentObservableList() {
        return fileContent.getOriginalContentObservableList();
    }
    
    public ObservableList<String> getContentObservableList() {
        return fileContent.getContentObservableList();
    }
 
    public ObservableList<Integer> getLineNumbersObservableList() {
        return fileContent.getLineNumbersObservableList();
    }

    public ObservableList<Integer> getSearchLineNumbersObservableList() {
        return fileContent.getSearchLineNumbersObservableList();
    }
}
