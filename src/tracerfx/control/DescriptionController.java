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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import tracerfx.tab.manager.ManagerFactory;

/**
 *
 * @author Dariusz Lelek
 */
public class DescriptionController {

    public static final ChangeListener CHANGE_LISTENER_TAB_SWITCH = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
        ManagerFactory.getFileTabManager().getTxtLineDescription().setText("");
    };

    public static final ChangeListener CHANGE_LISTENER_LINE_CHANGE = (ChangeListener) new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            final String text = newValue != null ? newValue.toString() : "";
            ManagerFactory.getFileTabManager().getTxtLineDescription().setText(text);
        }
    };
}