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
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import tracerfx.tab.manager.ManagerFactory;

/**
 * FXML Controller class
 *
 * @author Dariusz Lelek
 */
public class FileTabFXMLController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label lblLines;
    @FXML
    private Label lblTotalLines;
    @FXML
    private CheckBox chckFollowTrail;
    @FXML
    private ListView<String> listView;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Collection collection = ManagerFactory.getFileTabManager().getActiveFileContent();
        String linesNum = collection.size() + "";
        
        lblTotalLines.setText(linesNum);
        lblLines.setText(linesNum);
        
        listView.getItems().addAll(collection);
        listView.getSelectionModel().selectedItemProperty().addListener(DescriptionController.CHANGE_LISTENER_LINE_CHANGE);
    }    

    @FXML
    private void followTrail(ActionEvent event) {
        ManagerFactory.getFileTabManager().getActiveItem().setFollowTrail(chckFollowTrail.isSelected());
    }
    
}
