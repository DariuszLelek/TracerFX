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
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
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
    private CheckBox chckFilter;
    @FXML
    private TextField txtFilter;
    @FXML
    private Label lblLastSearch;

    private final StatusManager statusManager = ManagerFactory.getStatusManager();
    @FXML
    private ListView<Integer> numberListView;
    @FXML
    private ListView<String> contentListView;
    
    private final ListProperty<Integer> searchResults = new SimpleListProperty<>();
    private int searchResultsIndex = 0;
    private boolean scrollBound = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareProperties();
        prepareListeners();
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

    private void prepareProperties() {
        final FileContentProperty fcp = ManagerFactory.getFileTabManager().getFileContentProperty();

        bindSizeProperty(fcp.getContentProperty(), lblLines);
        bindSizeProperty(fcp.getOriginalContentListProperty(), lblTotalLines);

        txtFilter.disableProperty().bind(chckFilter.selectedProperty().not());
        contentListView.setItems(fcp.getContentProperty());
        numberListView.setItems(fcp.getLineNumbersProperty());
        lblLastSearch.textProperty().bind(fcp.getLastSearchProperty());
        lblLastSearch.textProperty().bind(fcp.getLastSearchProperty());
        
        numberListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        searchResults.set(fcp.getSearchResultsProperty());
        
        numberListView.setMouseTransparent( true );
        numberListView.setFocusTraversable( false );
    }
    
    private void bindSizeProperty(ListProperty listProperty, Label label){
        SimpleIntegerProperty listSizeProperty = new SimpleIntegerProperty();
        listSizeProperty.bind(listProperty.sizeProperty());
        label.textProperty().bind(listSizeProperty.asString());
    }
    
    private void prepareListeners(){
        contentListView.getSelectionModel().selectedItemProperty().addListener(DescriptionController.CHANGE_LISTENER_LINE_CHANGE);

        contentListView.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ESCAPE))
            {
                contentListView.getSelectionModel().clearSelection();
                return;
            }
            
            if (ke.isShiftDown()&& ke.getCode() == KeyCode.F3)
            {
                trySelectContent(searchResultsIndex - 1);
                return;
            }
            
            if (ke.getCode().equals(KeyCode.F3))
            {
                trySelectContent(searchResultsIndex + 1);
            }
        });
        
        txtFilter.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                applyFilter(txtFilter.getText());
                contentListView.requestFocus();
            }
        });

        searchResults.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            contentListView.requestFocus();
            contentListView.getSelectionModel().clearSelection();
            numberListView.getSelectionModel().clearSelection();

            searchResults.stream().forEach(x -> numberListView.getSelectionModel().select(x.intValue()));
            
            trySelectContent(0);
        });

        contentListView.setOnScroll((event) -> {
            bindScollWithOtherList();
        });
    };
    
    private void trySelectContent(int searchResultIndex) {
        updateSearchResultIndex(searchResultIndex);
        
        if (!searchResults.isEmpty()) {
            contentListView.getSelectionModel().select(searchResults.get(this.searchResultsIndex));
            contentListView.getFocusModel().focus(searchResults.get(this.searchResultsIndex));
            // problematic for double scroll
            // contentListView.scrollTo(searchResults.get(this.searchResultsIndex));
        }
    }
    
    private void updateSearchResultIndex(int searchResultIndex){
        this.searchResultsIndex = searchResultIndex < 0 ? searchResults.size() - 1 : 
                (searchResultIndex < searchResults.size() ? searchResultIndex : 0);  
    }

    private void bindScollWithOtherList() {
        if (!scrollBound) {
            Node n1 = contentListView.lookup(".scroll-bar");
            if (n1 instanceof ScrollBar) {
                final ScrollBar bar1 = (ScrollBar) n1;
                Node n2 = numberListView.lookup(".scroll-bar");
                if (n2 instanceof ScrollBar) {
                    final ScrollBar bar2 = (ScrollBar) n2;
                    bar2.valueProperty().bindBidirectional(bar1.valueProperty());
                }
            }
            scrollBound = true;
        }
    }
    
    private void applyFilter(String filter) {
        if (!filter.isEmpty()) {
            statusManager.setStatus(StringsFXML.STATUS_FILTER_SET.toString() + filter);
        } else {
            statusManager.setStatus(StringsFXML.STATUS_FILTER_EMPTY.toString());
        }
        
        ManagerFactory.getFileTabManager().getActiveItem().getFileContent().setFilter(filter);
    }

    private void onClick(MouseEvent event) {
        ManagerFactory.getFileTabManager().getActiveItem().processModified(false);
    }

    @FXML
    private void checkItemClick(MouseEvent event) {
    }
}
