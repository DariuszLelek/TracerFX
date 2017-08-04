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

/**
 *
 * @author Dariusz Lelek
 */
public class ManagerFactory {
    public static enum TYPE{
        FILE, PROJECT;
    }
    
    private static final FileTabManager fileTabManager;
    private static final ProjectTabManager projectTabManager;

    static{
        fileTabManager = new FileTabManager();
        projectTabManager = new ProjectTabManager();
    }

    public static Manager getManager(TYPE type){
        switch(type){
            case FILE:
                return fileTabManager;
            case PROJECT:
                return projectTabManager;
            default:
                return null;
        }
    }

    public static FileTabManager getFileTabManager() {
        return fileTabManager;
    }

    public static ProjectTabManager getProjectTabManager() {
        return projectTabManager;
    }
}
