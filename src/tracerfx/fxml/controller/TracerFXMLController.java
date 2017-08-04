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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tracerfx.tab.manager.ManagerFactory;
import tracerfx.tab.FileTab;
import tracerfx.tab.ProjectTab;

/**
 * FXML Controller class
 *
 * @author Dariusz Lelek
 */
public class TracerFXMLController implements Initializable {

    @FXML
    private TextField txtSearch;
    @FXML
    private TextArea txtLineDescription;
    @FXML
    private Button btnAddFile;
    @FXML
    private CheckBox chckTrailFollow;
    @FXML
    private Button btnSearch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareBindings();

    }    
    
    @FXML
    public void refineSearch(){
    
    }

    @FXML
    private void btnAddProject(ActionEvent event) {
        // just for test
        ManagerFactory.getManager(ManagerFactory.TYPE.PROJECT).addItem(new ProjectTab());
    }

    @FXML
    private void btnAddFile(ActionEvent event) {
        // just for test
        ManagerFactory.getManager(ManagerFactory.TYPE.FILE).addItem(new FileTab());
    }

    @FXML
    private void btnSearch(ActionEvent event) {
    }

    @FXML
    private void chckTrailFollow(ActionEvent event) {
    }

    private void prepareBindings() {
        ReadOnlyBooleanProperty anyFileProperty = ManagerFactory.getFileTabManager().getCollectionProperty().emptyProperty();
        ReadOnlyBooleanProperty anyProjectProperty = ManagerFactory.getProjectTabManager().getCollectionProperty().emptyProperty();
        
        // add file
        btnAddFile.disableProperty().bind(anyProjectProperty);

        // line description
        txtLineDescription.textProperty().bind(ManagerFactory.getFileTabManager().getSelectedLineProperty());
        
        // file tab controls
        chckTrailFollow.disableProperty().bind(anyFileProperty);
        txtSearch.disableProperty().bind(anyFileProperty);
        btnSearch.disableProperty().bind(anyFileProperty);  
    }
}
