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

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.joda.time.DateTime;
import tracerfx.task.ScheduledExecutor;
import tracerfx.util.Strings;

/**
 *
 * @author Dariusz Lelek
 */
public class StatusManager {
    
    private Label statusLabel;
    private DateTime lastUpdateTime;
    
    private final long STATUS_CLEAR_DELAY_MS = 6 * 1000;
    private final String EMPTY_STATUS = Strings.EMPTY.toString();
    
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
    
    private boolean hasStatus(){
        return !statusLabel.getText().isEmpty();
    }

    private void startClearingThread() {
        ScheduledExecutor.scheduleAtFixedRateMilliSeconds(getClearingRunnable(), STATUS_CLEAR_DELAY_MS);
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
