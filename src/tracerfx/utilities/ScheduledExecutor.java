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

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Dariusz Lelek
 */
public class ScheduledExecutor {

    private final static int SCHEDULER_THREAD_POOL = 1;
    private final static Collection<ScheduledExecutorService> RUNNING_EXECUTORS = new ArrayList<>();

    private static void addExecutorService(ScheduledExecutorService executor) {
        if (!RUNNING_EXECUTORS.contains(executor)) {
            RUNNING_EXECUTORS.add(executor);
        }
    }

    private static ScheduledExecutorService getScheduledExecutorService() {
        return Executors.newScheduledThreadPool(SCHEDULER_THREAD_POOL);
    }

    public static void stopScheduledExecutor() {
        RUNNING_EXECUTORS.stream().forEach(x -> x.shutdownNow());
    }

    public static void scheduleAtFixedRateMilliSeconds(final Runnable runnable, final long milliSecons) {
        scheduleAtFixedRateSeconds(runnable, milliSecons / 1000);
    }

    public static void scheduleAtFixedRateSeconds(final Runnable runnable, final long secons) {
        ScheduledExecutorService executorService = getScheduledExecutorService();
        addExecutorService(executorService);
        executorService.scheduleAtFixedRate(runnable, secons, secons, TimeUnit.SECONDS);
    }
}
