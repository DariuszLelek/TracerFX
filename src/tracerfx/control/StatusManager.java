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
package tracerfx.control;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.joda.time.DateTime;
import tracerfx.task.TaskManager;

/**
 *
 * @author Dariusz Lelek
 */
public class StatusManager {
    
    private Label statusLabel;
    private DateTime lastUpdateTime;
    
    private final long STATUS_CLEAR_DELAY_MS = 6 * 1000;
    private final int threadPool = 1;
    private final String EMPTY_STATUS = "";
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(threadPool);
    
    public void setStatusLabel(Label statusLabel){
        this.statusLabel = statusLabel;       
        startClearingThread();
    }
    
    public synchronized void setStatus(String status){
        lastUpdateTime = new DateTime();
        getStatusLabel().setText(status);
    }
    
    public Label getStatusLabel(){
        if(statusLabel == null){
            throw new NullPointerException("Status label not set.");
        }
        return statusLabel;
    }
    
    public void shutDown(){
        scheduler.shutdown();
    }
    
    private boolean hasStatus(){
        return !statusLabel.getText().isEmpty();
    }

    private void startClearingThread() {
        TaskManager.getScheduledExecutor().scheduleAtFixedRate(getClearingRunnable(), 1, 1, TimeUnit.SECONDS);      
    }
    
    private Runnable getClearingRunnable() {
        return () -> {
            if (hasStatus() && isUpdateTime()) {
                runStatusUpdate();
            }
        };
    }
    
    private boolean isUpdateTime(){
        return (new DateTime()).getMillis() - lastUpdateTime.getMillis() >= STATUS_CLEAR_DELAY_MS;
    }

    private void runStatusUpdate() {
        Platform.runLater(() -> {setStatus(EMPTY_STATUS);});
    }
}
