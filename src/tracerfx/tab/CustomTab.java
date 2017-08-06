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
package tracerfx.tab;

import javafx.scene.Node;
import javafx.scene.control.Tab;

/**
 *
 * @author Dariusz Lelek
 */
public abstract class CustomTab {
    protected String title;
    protected final Tab tab;
    
    protected final String modifiedStyle = "-fx-background-color: #b3f984;";

    public CustomTab() {
        tab = new Tab();
    }

    public CustomTab(String title, Node root) {
        this.title = title;
        
        tab = new Tab(title, root);
    }

    public String getTitle() {
        return title;
    }
    
    public Tab getTab() {
        return tab;
    }
}
