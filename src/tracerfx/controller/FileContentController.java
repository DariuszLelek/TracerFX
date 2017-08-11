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
package tracerfx.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import tracerfx.control.FileContent.FileContent;
import tracerfx.control.FileContent.FileContentProperty;
import tracerfx.controller.ControllerFactory;
import tracerfx.menu.Option;
import tracerfx.menu.Property;

/**
 *
 * @author Dariusz Lelek
 */
public class FileContentController {
    private FileContentProperty lastFileContentProperty;
    private final List<FileContent> fileContentList = new ArrayList<>();
    private final IntegerProperty monitoredFilesIntProperty = new SimpleIntegerProperty(0);

    public FileContentController() {
        ControllerFactory.getTaskController().scheduleAtFixedRateSeconds(getUpdateRunnable(), Option.getInt(Property.THREAD_FILES_MOD_CHECK_DELAY_S));
    }

    public IntegerProperty getMonitoredFilesIntProperty() {
        return monitoredFilesIntProperty;
    }

    public FileContentProperty getFileContentProperty() {
        return lastFileContentProperty;
    }

    public FileContent getFileContent(final File file) {
        final FileContent fileContent = new FileContent(file);
        addFileContent(fileContent);
        this.lastFileContentProperty = fileContent.getFileContentProperty();
        return fileContent;
    }
    
    private synchronized void addFileContent(final FileContent fileContent){
        fileContentList.add(fileContent);
    }
         
    public synchronized void removeFileContent(final FileContent fileContent){
        if(fileContentList.contains(fileContent)){
            fileContentList.remove(fileContent);
        }
    }
    
    private void updateMonitoredFilesIntProperty() {
        monitoredFilesIntProperty.set(fileContentList.stream().filter(f -> f.isFollowTrail()).collect(Collectors.toList()).size());
    }

    private Runnable getUpdateRunnable() {
        return () -> {
            Platform.runLater(() -> {
                updateMonitoredFilesIntProperty();
                synchronized (fileContentList) {
                    fileContentList.stream().forEach(f -> {
                        if (f.isFollowTrail() && f.getLastModified() != f.getFile().lastModified()) {
                            f.setFileAsModified();
                            ControllerFactory.getFileTabController().markTabAsModified(f.getFile());
                            
                            if(!Option.getBoolean(Property.LOAD_ON_CONTENT_FOCUS)){
                                f.processFileModified();
                            }
                        }
                    });
                }
            }
            );
        };
    }
}
