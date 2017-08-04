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
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tracerfx.tab.manager.FileTabManager;
import tracerfx.tab.manager.ManagerFactory;
import tracerfx.tab.manager.ProjectTabManager;

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
    @FXML
    private TabPane projectTabPane;
    
    private final ProjectTabManager projectTabManager = ManagerFactory.getProjectTabManager();
    private final FileTabManager fileTabManager = ManagerFactory.getFileTabManager();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareBindings();
        updateManagers();
    }    
    
    @FXML
    public void refineSearch(){
    
    }

    @FXML
    private void btnAddProject(ActionEvent event) {
       // if(!projectTabManager.projectExists("some title")){
            projectTabManager.addNewProject("some title");
      //  }
    }

    @FXML
    private void btnAddFile(ActionEvent event) {
        
        
        // just for test
        //ManagerFactory.getManager(ManagerFactory.TYPE.FILE).addItem(new Object());
    }

    @FXML
    private void btnSearch(ActionEvent event) {
    }

    @FXML
    private void chckTrailFollow(ActionEvent event) {
    }

    private void prepareBindings() {
        ReadOnlyBooleanProperty anyFileProperty = fileTabManager.getCollectionProperty().emptyProperty();
        ReadOnlyBooleanProperty anyProjectProperty = projectTabManager.getCollectionProperty().emptyProperty();
        
        // add file
        btnAddFile.disableProperty().bind(anyProjectProperty);

        // line description
        txtLineDescription.textProperty().bind(fileTabManager.getSelectedLineProperty());
        
        // file tab controls
        chckTrailFollow.disableProperty().bind(anyFileProperty);
        txtSearch.disableProperty().bind(anyFileProperty);
        btnSearch.disableProperty().bind(anyFileProperty);  
    }
    
    private void updateManagers(){
        projectTabManager.setProjectTabPane(projectTabPane);
    }
}
