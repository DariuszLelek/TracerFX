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
    private final FileContentProperty fileContentProperty;
    
    private final ObservableList<String> originalContentObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> contentObservableList = FXCollections.observableArrayList();
    private final ObservableList<Integer> lineNumbersObservableList = FXCollections.observableArrayList();
    private ObservableList<Integer> searchLineNumbersObservableList = FXCollections.observableArrayList();
      
    private final SimpleStringProperty lastSearchProperty = new SimpleStringProperty("");
    private final DateTime addTime;
    
    private long lastModified;
    private boolean followTrail;
    
    public FileContent(){
        this.file = null;
        this.addTime = new DateTime();
        this.fileContentProperty = new FileContentProperty(this);
    }

    public FileContent(File file) {
        this.file = file;
        this.addTime = new DateTime();
        this.fileContentProperty = new FileContentProperty(this);
        
        processFileModified();
    }

    public void fileModified(){
        processFileModified();
    }

    public FileContentProperty getFileContentProperty() {
        return fileContentProperty;
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
    
    private synchronized void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public ObservableList<String> getOriginalContentObservableList() {
        return originalContentObservableList;
    }

    public ObservableList<String> getContentObservableList() {
        return contentObservableList;
    }

    public ObservableList<Integer> getLineNumbersObservableList() {
        return lineNumbersObservableList;
    }

    public ObservableList<Integer> getSearchLineNumbersObservableList() {
        return searchLineNumbersObservableList;
    }

    public SimpleStringProperty getLastSearchProperty(){
        return lastSearchProperty;
    }
    
    public void setFilter(String filter) {
        this.filter = filter;
    }
    
    public void processFilterChange(String newFilter) {
        setFilter(newFilter);       
        displayOriginalContent();
        processSearch("");
        clearAndAddToObservableList(getFilteredContentList(), contentObservableList);
        updateLineNumbers();
    }

    public void processSearch(String searchString) {
        setLastSearch(searchString);
        clearAndAddToObservableList(getSearchResultLineNumbers(searchString), searchLineNumbersObservableList);
    } 
    
    private List<Integer> getSearchResultLineNumbers(String searchString){
        return searchString.isEmpty() ? new ArrayList<>() : 
                IntStream.range(0, contentObservableList.size())
                        .filter(i -> contentObservableList.get(i).contains(searchString))
                        .boxed().collect(Collectors.toList());
    }
    
    private void processFileModified(){
        clearAndAddToObservableList(getFileContentList(), originalContentObservableList);
        displayOriginalContent();
        setLastModified(file.lastModified());
        updateLineNumbers();
    }
    
    private void displayOriginalContent(){
        clearAndAddToObservableList(originalContentObservableList, contentObservableList);
    }
    
    private void setLastSearch(String lastsearch){
        lastSearchProperty.set(lastsearch);
    }

    private void updateLineNumbers(){   
        clearAndAddToObservableList(IntStream.range(1, contentObservableList.size() + 1).boxed().collect(Collectors.toList()), lineNumbersObservableList);
    }
        
    private List<String> getFilteredContentList(){
        if(!filter.isEmpty()){
            return originalContentObservableList.stream().filter(x -> x.contains(filter)).collect(Collectors.toList());
        }
        return originalContentObservableList;
    }
    
    private List<String> getFileContentList(){
        return FileUtility.getFileLines(file);
    }
    
    private <T> void clearAndAddToObservableList(List<T> listToAdd, ObservableList<T> observableList){
        observableList.clear();
        observableList.addAll(listToAdd);
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
