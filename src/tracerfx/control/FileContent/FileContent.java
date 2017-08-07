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
import java.util.stream.IntStream;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
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
    private final ListProperty<Integer> lineNumbersProperty = new SimpleListProperty<>();
    private final ListProperty<Integer> searchResultsProperty = new SimpleListProperty<>();
    private final ListProperty<String> originalContentListProperty = new SimpleListProperty<>(originalContentObservable);
    private final SimpleStringProperty lastSearchProperty = new SimpleStringProperty("");
    private final DateTime addTime;
    
    private long lastModified;
    private boolean followTrail;
    
    public FileContent(){
        this.file = null;
        this.addTime = new DateTime();
    }

    public FileContent(File file) {
        this.file = file;
        this.addTime = new DateTime();
        
        readFileAndUpdate();
    }

    public synchronized boolean isFollowTrail() {
        return followTrail;
    }

    public synchronized void setFollowTrail(boolean followTrail) {
        this.followTrail = followTrail;
    }

    public File getFile() {
        return file;
    }

    public synchronized long getLastModified() {
        return lastModified;
    }
    
    private synchronized void setLastModified(long thislastModified) {
        this.lastModified = thislastModified;
    }
    
    public ListProperty<String> getOriginalContentListProperty() {
        return originalContentListProperty;
    }

    public ListProperty<String> getContentProperty() {
        return contentListProperty;
    }
    
    public SimpleStringProperty getLastSearchProperty(){
        return lastSearchProperty;
    }
    
    public ListProperty<Integer> getLineNumbersProperty() {
        return lineNumbersProperty;
    }
    
    public ListProperty<Integer> getSearchResultsProperty() {
        return searchResultsProperty;
    }
    
    public void setFilter(String filter){
        if(filterChanged(filter)){
            this.filter = filter;
            setLastSearch("");
            updateContent(filterOriginalContent(), contentObservable);
            updateLineNumbers();
        }
    }

    public void processSearch(String searchString, boolean exactMatch) {
        setLastSearch(searchString);
        searchResultsProperty.set(FXCollections.observableArrayList(getSearchResultIndexes(filterOriginalContent(), searchString, exactMatch)));
        
//        if(searchString.isEmpty()){
//            updateContent(filterOriginalContent(), contentObservable);
//        }else{
//            updateContent(getSearchResult(searchString, filterOriginalContent(), exactMatch), contentObservable);
//        }
    } 
    
    public synchronized void processFileModified(){
        readFileAndUpdate();
    }
    
    private void setLastSearch(String lastsearch){
        lastSearchProperty.set(lastsearch);
    }

    private List<Integer> getSearchResultIndexes(List<String> filteredContent, String searchString, boolean exactMatch) {
        return IntStream.range(0, filteredContent.size()).filter(
                x -> exactMatch ? filteredContent.get(x).contains(searchString) : StringUtils.containsIgnoreCase(filteredContent.get(x), searchString))
                .boxed().collect(Collectors.toList());
    }

    private void updateLineNumbers(){   
        lineNumbersProperty.set(FXCollections.observableArrayList(IntStream.range(1, contentListProperty.size() + 1).boxed()
                .collect(Collectors.toList())));
    }
    
    private boolean filterChanged(String newFilter){
        return !this.filter.equals(newFilter);
    }
    
    private List<String> getSearchResult(String searchString, List<String> content, boolean exactMatch){
        return content.stream()
                .filter(x -> exactMatch ? x.contains(searchString) : StringUtils.containsIgnoreCase(x, searchString))
                .collect(Collectors.toList());
    }
        
    private List<String> filterOriginalContent(){
        if(!filter.isEmpty()){
            return getSearchResult(filter, originalContent, true);
        }
        return originalContent;
    }

    private void readFileAndUpdate() {
        final List<String> newContent = FileUtility.getFileLines(file);
        setLastModified(file.lastModified());
        updateContents(newContent);
    }
    
    private void updateContents(List<String> newContent){
        originalContent.clear();
        originalContent.addAll(newContent);
        
        updateContent(filterOriginalContent(), contentObservable);
        updateContent(newContent, originalContentObservable);
        
        updateLineNumbers();
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
        return Objects.equals(this.addTime, other.addTime);
    }
}
