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
package tracerfx.fxml.controller;

import tracerfx.control.DescriptionController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import tracerfx.control.FileContent.FileContentProperty;
import tracerfx.control.StatusManager;
import tracerfx.tab.manager.ManagerFactory;
import tracerfx.util.StringsFXML;

/**
 * FXML Controller class
 *
 * @author Dariusz Lelek
 */
public class FileTabFXMLController implements Initializable {

    @FXML
    private Label lblLines;
    @FXML
    private Label lblTotalLines;
    @FXML
    private CheckBox chckFollowTrail;
    @FXML
    private ListView<String> listView;
    @FXML
    private CheckBox chckFilter;
    @FXML
    private TextField txtFilter;
    @FXML
    private Label lblLastSearch;

    private final StatusManager statusManager = ManagerFactory.getStatusManager();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareListeners();
        prepareProperties();
    }

    @FXML
    private void chckFilter(ActionEvent event) {
        if(!chckFilter.isSelected()){
            txtFilter.setText("");
        }
        applyFilter(txtFilter.getText());
    }


    @FXML
    private void chckFollowTrail(ActionEvent event) {
        ManagerFactory.getFileTabManager().getActiveItem().setFollowTrail(chckFollowTrail.isSelected());
    }

    @FXML
    private void checkModified(MouseEvent event) {
        ManagerFactory.getFileTabManager().getActiveItem().processModified(false);
    }
    
    private void prepareProperties() {
        final FileContentProperty fcp = ManagerFactory.getFileTabManager().getFileContentProperty();

        bindSizeProperty(fcp.getContentProperty(), lblLines);
        bindSizeProperty(fcp.getOriginalContentListProperty(), lblTotalLines);

        txtFilter.disableProperty().bind(chckFilter.selectedProperty().not());
        listView.setItems(fcp.getContentProperty());
        lblLastSearch.textProperty().bind(fcp.getLastSearchProperty());
        lblLastSearch.textProperty().bind(fcp.getLastSearchProperty());
    }
    
    private void bindSizeProperty(ListProperty listProperty, Label label){
        SimpleIntegerProperty listSizeProperty = new SimpleIntegerProperty();
        listSizeProperty.bind(listProperty.sizeProperty());
        label.textProperty().bind(listSizeProperty.asString());
    }
    
    private void prepareListeners(){
        listView.getSelectionModel().selectedItemProperty().addListener(DescriptionController.CHANGE_LISTENER_LINE_CHANGE);

        listView.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.T)) {
                listView.scrollTo(0);
            }
            
            if (ke.getCode().equals(KeyCode.E)) {
                final int size = listView.getItems().size();
                if (size > 0) {
                    listView.scrollTo(size - 1);
                }
            }
            
            if (ke.getCode().equals(KeyCode.ESCAPE))
            {
                listView.getSelectionModel().clearSelection();
            }
        });
        
        txtFilter.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                applyFilter(txtFilter.getText());
                listView.requestFocus();
            }
        });
    }

    private void applyFilter(String filter) {
        if (!filter.isEmpty()) {
            statusManager.setStatus(StringsFXML.STATUS_FILTER_SET.toString() + filter);
        } else {
            statusManager.setStatus(StringsFXML.STATUS_FILTER_EMPTY.toString());
        }
        
        ManagerFactory.getFileTabManager().getActiveItem().getFileContent().setFilter(filter);
    }
}
