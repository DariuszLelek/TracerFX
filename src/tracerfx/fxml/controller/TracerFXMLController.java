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

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import tracerfx.tab.ProjectTab;
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
    @FXML
    private Label txtStatus;
    
    private final ProjectTabManager projectTabManager = ManagerFactory.getProjectTabManager();
    private final FileTabManager fileTabManager = ManagerFactory.getFileTabManager();    
    
    private StatusController statusController;
    @FXML
    private AnchorPane root;


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
        Optional<String> projectTitle = getProjectNameDialog().showAndWait();
        String title = projectTitle.isPresent() ? projectTitle.get() : "";
        tryAddProject(title);
    }

    private void tryAddProject(String title) {
        if (!title.isEmpty()) {
            if (!projectTabManager.projectExists(title)) {
                projectTabManager.addNewProject(title);
            } else {
                getStatusController().setStatus(StringsFXML.STATUS_PROJECT_NAME_EXISTS.toString());
            }
        } else {
            getStatusController().setStatus(StringsFXML.STATUS_PROJECT_NAME_EMPTY.toString());
        }
    }
    
    @FXML
    private void btnAddFile(ActionEvent event) {
        ProjectTab activeProjectTab = projectTabManager.getActiveItem();
        
        if(activeProjectTab.isValidTab()){
            File file = getFileChooserDialog().showOpenDialog(root.getScene().getWindow());
            fileTabManager.addNewFileToProject(file, activeProjectTab);
        }
    }

    @FXML
    private void btnSearch(ActionEvent event) {
    }

    @FXML
    private void chckTrailFollow(ActionEvent event) {
    }

    private void prepareBindings() {
        final ReadOnlyBooleanProperty anyFileProperty = fileTabManager.getCollectionProperty().emptyProperty();
        final ReadOnlyBooleanProperty anyProjectProperty = projectTabManager.getCollectionProperty().emptyProperty();
        
        // add file
        btnAddFile.disableProperty().bind(anyProjectProperty);

        // line description
        //txtLineDescription.textProperty().bind(fileTabManager.getSelectedLineProperty());
        
        // file tab controls
        chckTrailFollow.disableProperty().bind(anyFileProperty);
        txtSearch.disableProperty().bind(anyFileProperty);
        btnSearch.disableProperty().bind(anyFileProperty);  
    }
    
    private void updateManagers(){
        projectTabManager.setProjectTabPane(projectTabPane);
    }
    
    private TextInputDialog getProjectNameDialog(){
        final TextInputDialog textInputDialog = new TextInputDialog("enter");
        textInputDialog.setTitle(StringsFXML.NEW_PROJECT_DIALOG_TITLE.toString());
        textInputDialog.setHeaderText(StringsFXML.NEW_PROJECT_DIALOG_HEADER.toString());
        return textInputDialog;
    }
    
    private FileChooser getFileChooserDialog(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(StringsFXML.NEW_FILE_DIALOG_TITLE.toString());
        return fileChooser;
    }
    
    private StatusController getStatusController(){
        if(statusController == null){
            statusController = new StatusController(txtStatus);
        }
        return statusController;
    }
}
