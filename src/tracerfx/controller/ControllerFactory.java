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

/**
 *
 * @author Dariusz Lelek
 */
public class ControllerFactory { 
    private static final FileTabController FILE_TAB_CONTROLLER = new FileTabController();
    private static final ProjectTabController PROJECT_TAB_CONTROLLER = new ProjectTabController();
    private static final StatusController STATUS_CONTROLLER = new StatusController();
    private static final DescriptionController DESCRIPTION_CONTROLLER = new DescriptionController();
    private static final TaskController TASK_CONTROLLER = new TaskController();
    private static final FileContentController FILE_CONTENT_CONTROLLER = new FileContentController();
    
    public static StatusController getStatusController(){
        return STATUS_CONTROLLER;
    }

    public static FileTabController getFileTabController() {
        return FILE_TAB_CONTROLLER;
    }

    public static ProjectTabController getProjectTabController() {
        return PROJECT_TAB_CONTROLLER;
    }

    public static DescriptionController getDescriptionController() {
        return DESCRIPTION_CONTROLLER;
    }

    public static TaskController getTaskController() {
        return TASK_CONTROLLER;
    }

    public static FileContentController getFileContentController() {
        return FILE_CONTENT_CONTROLLER;
    }
}
