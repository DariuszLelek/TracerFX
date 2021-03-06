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
package tracerfx.controller.fxml;

import tracerfx.controller.StatusController;
import tracerfx.utilities.Strings;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import tracerfx.component.ProjectTab;
import tracerfx.controller.FileTabController;
import tracerfx.controller.ControllerFactory;
import tracerfx.controller.ProjectTabController;

/**
 * FXML Controller class
 *
 * @author Dariusz Lelek
 */
public class TracerFXMLController implements Initializable {
    private final ProjectTabController projectTabController = ControllerFactory.getProjectTabController();
    private final FileTabController fileTabControler = ControllerFactory.getFileTabController();
    private final StatusController statusController = ControllerFactory.getStatusController();

    @FXML
    private TextField txtSearch;
    @FXML
    private WebView txtLineDescription;
    @FXML
    private Button btnAddFile;
    @FXML
    private Button btnSearch;
    @FXML
    private TabPane projectTabPane;
    @FXML
    private Label txtStatus;
    @FXML
    private AnchorPane root;
    @FXML
    private Button btnRemoveProject;
    @FXML
    private Button btnRemoveFile;
    @FXML
    private Label lblFileMonitor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareControllers();
        prepareBindings();
        prepareListeners();
    }

    @FXML
    private void btnAddProject(ActionEvent event) {
        Optional<String> projectTitle = getProjectNameDialog().showAndWait();
        String title = projectTitle.isPresent() ? projectTitle.get() : Strings.EMPTY.toString();
        tryAddProject(title);
    }

    @FXML
    private void btnAddFile(ActionEvent event) {
        ProjectTab activeProjectTab = projectTabController.getActiveItem();
        tryAddFile(activeProjectTab);
    }

    @FXML
    private void btnSearch(ActionEvent event) {
        trySearch();
    }

    @FXML
    private void removeProject(ActionEvent event) {
        if (projectTabController.tryToRemoveActiveProject()) {
            statusController.setStatus(Strings.STATUS_REMOVE_PROJECT.toString());
        } else {
            statusController.setStatus(Strings.STATUS_FAILED_REMOVE_PROJECT.toString());
        }
    }

    @FXML
    private void removeFile(ActionEvent event) {
        if (fileTabControler.tryToRemoveFileFromActiveProject()) {
            statusController.setStatus(Strings.STATUS_REMOVE_FILE.toString());
        } else {
            statusController.setStatus(Strings.STATUS_FAILED_REMOVE_FILE.toString());
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        ControllerFactory.getTaskController().stopTaskController();
        Platform.exit();
    }

    private void trySearch() {
        final String searchString = txtSearch.getText();
        txtSearch.setText(Strings.EMPTY.toString());
        
        if (fileTabControler.getActiveItem().isNotEmpty()) {
            fileTabControler.processSearch(searchString);

            statusController.setStatus(searchString.isEmpty()
                    ? Strings.STATUS_SEARCH_RESTORE.toString()
                    : Strings.STATUS_SEARCH_FOR.toString() + searchString);
        } else {
            statusController.setStatus(Strings.STATUS_SEARCH_NO_FILE.toString());
        }
    }

    private void prepareBindings() {
        final ReadOnlyBooleanProperty anyFileProperty = fileTabControler.getCollectionProperty().emptyProperty();
        final ReadOnlyBooleanProperty anyProjectProperty = projectTabController.getCollectionProperty().emptyProperty();

        btnAddFile.disableProperty().bind(anyProjectProperty);
        btnRemoveFile.disableProperty().bind(anyFileProperty);
        btnRemoveProject.disableProperty().bind(anyProjectProperty);
        txtSearch.disableProperty().bind(anyFileProperty);
        btnSearch.disableProperty().bind(anyFileProperty);

        lblFileMonitor.textProperty().bind(fileTabControler.getMonitoredFilesIntProperty().asString());
    }

    private void prepareListeners() {
        projectTabPane.getSelectionModel().selectedItemProperty().addListener(ControllerFactory.getDescriptionController().CHANGE_LISTENER_TAB_SWITCH);
    }

    private void prepareControllers() {
        projectTabController.setProjectTabPane(projectTabPane);
        fileTabControler.setTxtLineDescription(txtLineDescription);
        statusController.setStatusLabel(txtStatus);
    }

    private TextInputDialog getProjectNameDialog() {
        final TextInputDialog textInputDialog = new TextInputDialog(Strings.NEW_PROJECT_DIALOG_PROJECT_NAME.toString());
        textInputDialog.setTitle(Strings.NEW_PROJECT_DIALOG_TITLE.toString());
        textInputDialog.setHeaderText(Strings.NEW_PROJECT_DIALOG_HEADER.toString());
        return textInputDialog;
    }

    private FileChooser getFileChooserDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Strings.NEW_FILE_DIALOG_TITLE.toString());
        return fileChooser;
    }

    private void tryAddProject(String title) {
        if (!title.isEmpty()) {
            if (!projectTabController.projectExists(title)) {
                projectTabController.addNewProject(title);
                statusController.setStatus(Strings.STATUS_PROJECT_ADDED.toString());
            } else {
                statusController.setStatus(Strings.STATUS_PROJECT_NAME_EXISTS.toString());
            }
        } else {
            statusController.setStatus(Strings.STATUS_FAILED_ADD_PROJECT.toString());
        }
    }

    private void tryAddFile(ProjectTab activeProjectTab) {
        if (activeProjectTab.isNotEmpty()) {
            File file = getFileChooserDialog().showOpenDialog(root.getScene().getWindow());
            if (file != null) {
                fileTabControler.addNewFileToProject(file, activeProjectTab);
                statusController.setStatus(Strings.STATUS_FILE_ADDED.toString());
            }
        }
    }

    @FXML
    private void about(ActionEvent event) {
        Alert aboutAlert = new Alert(AlertType.INFORMATION);
        aboutAlert.setTitle(Strings.ABOUT_TITLE.toString());
        aboutAlert.setHeaderText(Strings.ABOUT_HEADER.toString());
        aboutAlert.setContentText(Strings.ABOUT_CONTENT.toString());
        aboutAlert.show();
    }

    @FXML
    private void onKeyPressedTxtSearch(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            trySearch();
        }
    }

    @FXML
    private void onKeyPressedRoot(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.F) {
            txtSearch.requestFocus();
        }
    }
}
