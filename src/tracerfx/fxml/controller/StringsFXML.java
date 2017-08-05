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
package tracerfx.fxml.controller;

/**
 *
 * @author Dariusz Lelek
 */
public enum StringsFXML {
    NEW_PROJECT_DIALOG_TITLE("Project Name"),  
    NEW_PROJECT_DIALOG_HEADER("Enter new project name"),
    
    NEW_FILE_DIALOG_TITLE("Open new file"),
    
    STATUS_PROJECT_NAME_EMPTY("No project name entered."),
    STATUS_PROJECT_NAME_EXISTS("Project with given name already exists.");

    private final String value;

    private StringsFXML(String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }
}
