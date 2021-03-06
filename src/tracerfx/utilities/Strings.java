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
package tracerfx.utilities;

/**
 *
 * @author Dariusz Lelek
 */
public enum Strings {
    EMPTY(""),
    
    ABOUT_TITLE("About"),
    ABOUT_HEADER("TracerFX - Version 1.0"),
    ABOUT_CONTENT("JavaFX log tracer tool.\n\n created by: dariusz.lelek@gmail.com"),
    
    NEW_PROJECT_DIALOG_TITLE("Project Name"),  
    NEW_PROJECT_DIALOG_HEADER("Enter new project name"),
    NEW_PROJECT_DIALOG_PROJECT_NAME("New Project"),
    
    NEW_FILE_DIALOG_TITLE("Open new file"),
    
    STATUS_FILE_ADDED("File added to the project."),
    STATUS_PROJECT_ADDED("New project added."),
    STATUS_FAILED_ADD_PROJECT("Failed to add project."),
    STATUS_PROJECT_NAME_EXISTS("Project with given name already exists."),
    STATUS_FAILED_REMOVE_FILE("Failed to remove a file from the project."),
    STATUS_FAILED_REMOVE_PROJECT("Failed to remove a project."),
    STATUS_REMOVE_FILE("File removed from the project."),
    STATUS_REMOVE_PROJECT("Project removed."),
    STATUS_SEARCH_NO_FILE("No file to search."),
    STATUS_SEARCH_FOR("Search for: "),
    STATUS_SEARCH_RESTORE("No search string entered, restoring original view."),
    
    FX_BACK_COLOR("-fx-background-color: "),
    
    STATUS_FILTER_SET("Applying filter: "),
    STATUS_FILTER_EMPTY("Filter is empty.");
    
    private final String value;

    private Strings(String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }
}
