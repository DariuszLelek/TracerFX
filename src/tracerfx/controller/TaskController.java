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

import tracerfx.utilities.FileLoadTask;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import tracerfx.control.FileContent.FileContent;

/**
 *
 * @author Dariusz Lelek
 */
public class TaskController{

    private final int SCHEDULER_THREAD_POOL = 1;
    private final Collection<ScheduledExecutorService> RUNNING_EXECUTORS = new ArrayList<>();
    private final Map<Integer, Boolean> RUNNING_THREADS = new HashMap<>();
    private static Integer threadNum = 0;

    public TaskController() {
    }    

    private void addExecutorService(ScheduledExecutorService executor) {
        if (!RUNNING_EXECUTORS.contains(executor)) {
            RUNNING_EXECUTORS.add(executor);
        }
    }

    private ScheduledExecutorService getScheduledExecutorService() {
        return Executors.newScheduledThreadPool(SCHEDULER_THREAD_POOL);
    }

    public void stopScheduledExecutor() {
        RUNNING_EXECUTORS.stream().forEach(x -> x.shutdownNow());
    }

    public void scheduleAtFixedRateMilliSeconds(final Runnable runnable, final long milliSecons) {
        scheduleAtFixedRateSeconds(runnable, milliSecons / 1000);
    }

    public void scheduleAtFixedRateSeconds(final Runnable runnable, final long secons) {
        ScheduledExecutorService executorService = getScheduledExecutorService();
        addExecutorService(executorService);
        executorService.scheduleAtFixedRate(runnable, secons, secons, TimeUnit.SECONDS);
    }
    
    public void runFileUpdate(FileContent fileContent){      
        RUNNING_THREADS.put(threadNum, Boolean.TRUE);
        FileLoadTask fld = new FileLoadTask(fileContent, threadNum ++);
        
        fld.start();
    }
    
    public boolean threadRunning(Integer threadNumber) {
        return RUNNING_THREADS.containsKey(threadNumber) ? RUNNING_THREADS.get(threadNumber) : false;
    }
}
