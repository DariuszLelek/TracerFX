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
package tracerfx.tab.manager;

import tracerfx.control.StatusManager;

/**
 *
 * @author Dariusz Lelek
 */
public class ManagerFactory { 
    private static final FileTabManager fileTabManager;
    private static final ProjectTabManager projectTabManager;
    private static final StatusManager statusManager;
    

    static{
        fileTabManager = new FileTabManager();
        projectTabManager = new ProjectTabManager();
        statusManager = new StatusManager();
    }
    
    public static StatusManager getStatusManager(){
        return statusManager;
    }

    public static FileTabManager getFileTabManager() {
        return fileTabManager;
    }

    public static ProjectTabManager getProjectTabManager() {
        return projectTabManager;
    }
}
