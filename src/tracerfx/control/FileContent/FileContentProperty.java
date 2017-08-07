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

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Dariusz Lelek
 */
public class FileContentProperty {

    private final FileContent fileContent;

    public FileContentProperty(FileContent fileContent) {
        this.fileContent = fileContent;
    }

    public ListProperty<String> getContentProperty() {
        return fileContent.getContentProperty();
    }

    public ListProperty<String> getOriginalContentListProperty() {
        return fileContent.getOriginalContentListProperty();
    }

    public SimpleStringProperty getLastSearchProperty() {
        return fileContent.getLastSearchProperty();
    }
    
    public ListProperty<Integer> getLineNumbersProperty() {
        return fileContent.getLineNumbersProperty();
    }
    
    public ListProperty<Integer> getSearchResultsProperty() {
        return fileContent.getSearchResultsProperty();
    }
}
