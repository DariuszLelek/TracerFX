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
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.joda.time.DateTime;
import tracerfx.util.FileUtility;

/**
 *
 * @author Dariusz Lelek
 */
public class FileContent {
    private String filter = "";
    
    private final File file;
    private final List<String> originalContent = new ArrayList<>();
    private final ObservableList<String> contentObservable = FXCollections.observableArrayList(originalContent);
    private final ObservableList<String> originalContentObservable = FXCollections.observableArrayList(originalContent);
    private final ListProperty<String> contentListProperty = new SimpleListProperty<>(contentObservable);
    private final ListProperty<String> originalContentListProperty = new SimpleListProperty<>(originalContentObservable);
    private final SimpleStringProperty lastSearchProperty = new SimpleStringProperty("");
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

    public ListProperty<String> getOriginalContentListProperty() {
        return originalContentListProperty;
    }

    public ListProperty<String> getContentProperty() {
        return contentListProperty;
    }
    
    public SimpleStringProperty getString(){
        return lastSearchProperty;
    }
    
    public void setFilter(String filter){
        if(filterChanged(filter)){
            this.filter = filter;
            updateContent(filterOriginalContent(), contentObservable);
        }
    }

    public void processSearch(String searchString) {
        setLastSearch(searchString);
        updateContent(getSearchResult(searchString, filterOriginalContent()), contentObservable);
    }
    
    public void processFileModified(){
        readFileAndUpdate();
    }
    
    private void setLastSearch(String lastsearch){
        lastSearchProperty.set(lastsearch);
    }
    
    private boolean filterChanged(String newFilter){
        return !this.filter.equals(newFilter);
    }
    
    private List<String> getSearchResult(String searchString, List<String> content){
        return content.stream()
                .filter(x -> x.contains(searchString)).collect(Collectors.toList());
    }
        
    private List<String> filterOriginalContent(){
        if(!filter.isEmpty()){
            return getSearchResult(filter, originalContent);
        }
        return originalContent;
    }

    private void readFileAndUpdate() {
        List<String> newContent = FileUtility.getFileLines(file);
        updateContents(newContent);
    }
    
    private void updateContents(List<String> newContent){
        originalContent.clear();
        originalContent.addAll(newContent);
        
        updateContent(filterOriginalContent(), contentObservable);
        updateContent(newContent, originalContentObservable);
    }
    
    private void updateContent(List<String> newContent, ObservableList observableList){
        observableList.clear();
        observableList.addAll(newContent);
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
