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
import java.util.Objects;
import java.util.stream.Collector;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.joda.time.DateTime;
import tracerfx.util.FileUtility;

/**
 *
 * @author Dariusz Lelek
 */
public class FileContent {
    private final File file;
    private final List<String> originalContent = new ArrayList<>();
    private final ObservableList<String> originalContentObservable = FXCollections.observableArrayList(originalContent);;
    private final ListProperty<String> contentListProperty = new SimpleListProperty<>();
    private final DateTime addTime;
    private DateTime lastModified;
    
    public FileContent(){
        this.file = null;
        this.addTime = new DateTime();
    }

    public FileContent(File file) {
        this.file = file;
        this.addTime = new DateTime();
        
        readFileAndUpdate();
    }
    
    public File getFile() {
        return file;
    }

    public void setSearchResultContent(String searchString) {
        // add mapper
        // check if can refactor
        ObservableList<String> searchResultContent
                = originalContent.stream().filter(x -> x.contains(searchString)).collect(Collector.of(FXCollections::observableArrayList,
                        ObservableList::add, (l1, l2) -> {
                            l1.addAll(l2);
                            return l1;
                        }));
        contentListProperty.set(searchResultContent);
    }

    public ListProperty<String> getContentProperty() {
        return contentListProperty;
    }
    
    public void processFileModified(){
        readFileAndUpdate();
    }

    private void readFileAndUpdate() {
        originalContent.clear();
        originalContent.addAll(FileUtility.getFileLines(file));
        originalContentObservable.clear();
        originalContentObservable.setAll(originalContent);
        contentListProperty.set(originalContentObservable);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.file);
        hash = 67 * hash + Objects.hashCode(this.addTime);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileContent other = (FileContent) obj;
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        if (!Objects.equals(this.addTime, other.addTime)) {
            return false;
        }
        return true;
    }

    
    
    
    
}
