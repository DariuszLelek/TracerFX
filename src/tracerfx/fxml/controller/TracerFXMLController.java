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
import tracerfx.control.StatusController;
import tracerfx.util.StringsFXML;
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
    @FXML
    private Button btnRemoveProject;
    @FXML
    private Button btnRemoveFile;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareManagers();
        prepareBindings();
        
        statusController = new StatusController(txtStatus);
        
        projectTabPane.getSelectionModel().selectedItemProperty().addListener(DescriptionController.CHANGE_LISTENER_TAB_SWITCH);
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
                statusController.setStatus(StringsFXML.STATUS_PROJECT_ADDED.toString());
            } else {
                statusController.setStatus(StringsFXML.STATUS_PROJECT_NAME_EXISTS.toString());
            }
        } else {
            statusController.setStatus(StringsFXML.STATUS_FAILED_ADD_PROJECT.toString());
        }
    }
    
    @FXML
    private void btnAddFile(ActionEvent event) {
        ProjectTab activeProjectTab = projectTabManager.getActiveItem();
        tryAddFile(activeProjectTab);

    }
    
    private void tryAddFile(ProjectTab activeProjectTab) {
        if (activeProjectTab.isNotDummy()) {
            File file = getFileChooserDialog().showOpenDialog(root.getScene().getWindow());
            fileTabManager.addNewFileToProject(file, activeProjectTab);
            statusController.setStatus(StringsFXML.STATUS_FILE_ADDED.toString());
        }
    }

    @FXML
    private void btnSearch(ActionEvent event) {
        //TODO add validation
        
        ManagerFactory.getFileTabManager().getTxtLineDescription().setText("");
    }

    private void chckTrailFollow(ActionEvent event) {
        fileTabManager.getActiveItem().setFollowTrail(chckTrailFollow.isSelected());
    }

    private void prepareBindings() {
        final ReadOnlyBooleanProperty anyFileProperty = fileTabManager.getCollectionProperty().emptyProperty();
        final ReadOnlyBooleanProperty anyProjectProperty = projectTabManager.getCollectionProperty().emptyProperty();
        
        // file
        btnAddFile.disableProperty().bind(anyProjectProperty);
        btnRemoveFile.disableProperty().bind(anyFileProperty);
        
        // project
        btnRemoveProject.disableProperty().bind(anyProjectProperty);

        // line description
        //txtLineDescription.textProperty().bind(fileTabManager.getSelectedLineProperty());
        
        // file tab controls
        //chckTrailFollow.disableProperty().bind(anyFileProperty);
        //chckTrailFollow.selectedProperty().bind(followTrailProperty);
        txtSearch.disableProperty().bind(anyFileProperty);
        btnSearch.disableProperty().bind(anyFileProperty);  
    }
    
    private void prepareManagers(){
        projectTabManager.setProjectTabPane(projectTabPane);
        fileTabManager.setTxtLineDescription(txtLineDescription);
    }
    
    private TextInputDialog getProjectNameDialog(){
        final TextInputDialog textInputDialog = new TextInputDialog(StringsFXML.NEW_PROJECT_DIALOG_PROJECT_NAME.toString());
        textInputDialog.setTitle(StringsFXML.NEW_PROJECT_DIALOG_TITLE.toString());
        textInputDialog.setHeaderText(StringsFXML.NEW_PROJECT_DIALOG_HEADER.toString());
        return textInputDialog;
    }
    
    private FileChooser getFileChooserDialog(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(StringsFXML.NEW_FILE_DIALOG_TITLE.toString());
        return fileChooser;
    }

    @FXML
    private void removeProject(ActionEvent event) {
        if (projectTabManager.tryToRemoveActiveProject()) {
            statusController.setStatus(StringsFXML.STATUS_REMOVE_PROJECT.toString());
        } else {
            statusController.setStatus(StringsFXML.STATUS_FAILED_REMOVE_PROJECT.toString());
        }
    }

    @FXML
    private void removeFile(ActionEvent event) {
        if(fileTabManager.tryToRemoveFileFromActiveProject()){
            statusController.setStatus(StringsFXML.STATUS_REMOVE_FILE.toString());
        }else{
            statusController.setStatus(StringsFXML.STATUS_FAILED_REMOVE_FILE.toString());
        }
    }
}
