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
package tracerfx.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author Dariusz Lelek
 */
public class TaskManager {
    private final static int SCHEDULER_THREAD_POOL = 1;
    private final static Collection<ScheduledExecutorService> RUNNING_EXECUTORS = new ArrayList<>();
    
    public static ScheduledExecutorService getScheduledExecutor(){
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(SCHEDULER_THREAD_POOL);
        RUNNING_EXECUTORS.add(executor);
        return executor;
    }
    
    public static void stopTaskManager(){
        RUNNING_EXECUTORS.stream().forEach(x -> x.shutdownNow());
    }
}
