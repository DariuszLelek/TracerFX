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

import java.util.Collection;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Dariusz Lelek
 */
public class Manager<T>{
    protected final ObservableList<T> itemList; 
    protected final SimpleListProperty<T> itemListProperty; 
    protected T activeItem;

    public Manager() {
        itemList = FXCollections.observableArrayList();
        itemListProperty = new SimpleListProperty<>(itemList);
    } 

    public Collection<T> getAllItems() {
        return itemList;
    }

    public T getActiveItem() {
        return activeItem;
    }

    public void addItem(T item) {
        itemList.add(item);
    }

    public void removeItem(T item) {
        if(itemList.contains(item)){
            itemList.remove(item);
        }
    }

    public SimpleListProperty<T> getCollectionProperty() {
        return itemListProperty;
    }
}
