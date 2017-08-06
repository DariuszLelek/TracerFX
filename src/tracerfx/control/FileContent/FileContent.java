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
    private final List<String> originalContent;
    private final ObservableList<String> originalContentObservable;
    private final ListProperty<String> contentProperty = new SimpleListProperty<>();
    private final DateTime time;
    
    public FileContent(){
        this.file = null;
        this.time = new DateTime();
        originalContent = new ArrayList<>();
        originalContentObservable = FXCollections.observableArrayList(originalContent);
    }

    public FileContent(File file) {
        this.file = file;
        this.time = new DateTime();
        
        originalContent = FileUtility.getFileLines(file);
        originalContentObservable = FXCollections.observableArrayList(originalContent);
        contentProperty.set(originalContentObservable);
    }

    public void updateContent(){
        originalContent.clear();
        originalContent.addAll(FileUtility.getFileLines(file));
        originalContentObservable.clear();
        originalContentObservable.setAll(originalContent);
        setOriginalContent();
    }
    
    public File getFile() {
        return file;
    }

    public void setOriginalContent(){
        setContentProperty(originalContentObservable);
    }

    public void setSearchContent(String searchContent) {
        // check if can refactor
        setContentProperty(
                originalContent.stream().filter(x -> x.contains(searchContent)).collect(Collector.of(FXCollections::observableArrayList,
                        ObservableList::add, (l1, l2) -> {
                            l1.addAll(l2);
                            return l1;
                        })));
    }
    
    private void setContentProperty(ObservableList<String> content){
        contentProperty.set(content);
    }

    public ListProperty<String> getContentProperty() {
        return contentProperty;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.file);
        hash = 67 * hash + Objects.hashCode(this.time);
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
        if (!Objects.equals(this.time, other.time)) {
            return false;
        }
        return true;
    }

    
    
    
    
}
