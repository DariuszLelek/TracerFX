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

import java.util.ArrayList;
import tracerfx.utilities.FileLoadTask;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import tracerfx.component.FileContent;

/**
 *
 * @author Dariusz Lelek
 */
public class TaskController{
    private final Collection<ScheduledExecutorService> RUNNING_EXECUTORS = new ArrayList<>();
    private final Collection<FileLoadTask> RUNNING_TASKS = new ArrayList<>();

    public TaskController() {
    }    

    private void addExecutorService(ScheduledExecutorService executor) {
        RUNNING_EXECUTORS.add(executor);
    }

    private void addFileLoadTask(FileLoadTask task) {
        RUNNING_TASKS.add(task);
    }

    public void stopTaskController() {
        RUNNING_EXECUTORS.stream().forEach(x -> x.shutdownNow());
        RUNNING_TASKS.stream().forEach(x -> x.stopTask());
    }

    public void scheduleAtFixedRateMilliSeconds(final Runnable runnable, final long milliSecons) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        addExecutorService(executorService);
        executorService.scheduleAtFixedRate(runnable, milliSecons, milliSecons, TimeUnit.MILLISECONDS);
    }
    
    public void runFileLoadTask(FileContent fileContent){      
        FileLoadTask fileLoadTask = new FileLoadTask(fileContent);
        addFileLoadTask(fileLoadTask);
        fileLoadTask.start();
    }
}
