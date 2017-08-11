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
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import tracerfx.control.FileContent.FileContentProperty;
import tracerfx.controller.StatusController;
import tracerfx.tab.FileTab;
import tracerfx.controller.ControllerFactory;
import tracerfx.menu.Option;
import tracerfx.menu.Property;
import tracerfx.utilities.Strings;

/**
 * FXML Controller class
 *
 * @author Dariusz Lelek
 */
public class FileTabFXMLController implements Initializable {

    private ObservableList<Integer> searchResultIndexList;
    
    private final StatusController statusManager = ControllerFactory.getStatusController();
    private int searchResultsIndex = 0;

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
    @FXML
    private ListView<Integer> numberListView;
    @FXML
    private ListView<String> contentListView;
    @FXML
    private Label lblLastSearchResultNum;
    @FXML
    private VBox vBoxNumbers;
    @FXML
    private Pane paneNumbersPlaceHolder;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareProperties();
        prepareListeners();
        bindScollWithLineNumbersList();
        prepareLineNumbersPlaceHolder();
    }

    private void prepareProperties() {
        final FileContentProperty fileContentProperty = ControllerFactory.getFileTabController().getFileContentProperty();
        
        bindListSizeToText(new SimpleListProperty(fileContentProperty.getContentObservableList()), lblLines);
        bindListSizeToText(new SimpleListProperty(fileContentProperty.getOriginalContentObservableList()), lblTotalLines);
        bindListSizeToText(new SimpleListProperty(fileContentProperty.getSearchLineNumbersObservableList()), lblLastSearchResultNum);

        contentListView.setItems(fileContentProperty.getContentObservableList());
        numberListView.setItems(fileContentProperty.getLineNumbersObservableList());
        
        searchResultIndexList = fileContentProperty.getSearchLineNumbersObservableList();
        
        txtFilter.disableProperty().bind(chckFilter.selectedProperty().not());
        lblLastSearch.textProperty().bind(fileContentProperty.getLastSearchProperty());
        
        numberListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        numberListView.setMouseTransparent(true);
        numberListView.setFocusTraversable(false);
    }

    private void bindListSizeToText(ListProperty listProperty, Label label) {
        SimpleIntegerProperty listSizeProperty = new SimpleIntegerProperty();
        listSizeProperty.bind(listProperty.sizeProperty());
        label.textProperty().bind(listSizeProperty.asString());
    }

    private void prepareListeners() {
        contentListView.getSelectionModel().selectedItemProperty().addListener(ControllerFactory.getDescriptionController().CHANGE_LISTENER_LINE_CHANGE);

        searchResultIndexList.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            contentListView.requestFocus();
            numberListView.getSelectionModel().selectIndices(-1, searchResultIndexList.stream().mapToInt(i -> i).toArray());
            updateSearchResultIndex(0);
            trySelectContentIndex();
        });
    }

    private void trySelectContentIndex() {
        if (!searchResultIndexList.isEmpty()) {
            focusIndex(searchResultIndexList.get(searchResultsIndex));
        }
    }

    private void focusIndex(int index) {
        contentListView.getSelectionModel().select(index);
        contentListView.scrollTo(index);
        numberListView.scrollTo(index);
    }

    private void updateSearchResultIndex(int searchResultIndex) {
        this.searchResultsIndex = searchResultIndex < 0 ? searchResultIndexList.size() - 1
                : (searchResultIndex < searchResultIndexList.size() ? searchResultIndex : 0);
    }

    private void bindScollWithLineNumbersList() {
        Platform.runLater(() -> {
            Node node1 = contentListView.lookup(".scroll-bar:vertical");
            if (node1 instanceof ScrollBar) {
                final ScrollBar bar1 = (ScrollBar) node1;
                Node node2 = numberListView.lookup(".scroll-bar:vertical");
                if (node2 instanceof ScrollBar) {
                    final ScrollBar bar2 = (ScrollBar) node2;
                    bar2.valueProperty().bindBidirectional(bar1.valueProperty());
                }
            }
        });
    }

    private void prepareLineNumbersPlaceHolder() {
        Platform.runLater(() -> {
            contentListView.lookupAll(".scroll-bar").stream().filter((node) -> (node instanceof ScrollBar)).map((node) -> (ScrollBar) node).filter((scroll) -> (scroll.getOrientation() == Orientation.HORIZONTAL)).map((scroll) -> {
                processPlaceHolderInsert(scroll.isVisible());
                return scroll;
            }).forEachOrdered((scroll) -> {
                scroll.visibleProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    processPlaceHolderInsert(newValue);
                });
            });
        });
    }

    private void processPlaceHolderInsert(boolean scrollVisible) {
        if (scrollVisible && !vBoxNumbers.getChildren().contains(paneNumbersPlaceHolder)) {
            vBoxNumbers.getChildren().add(paneNumbersPlaceHolder);
            Platform.runLater(vBoxNumbers::requestLayout);
        } else if (!scrollVisible && vBoxNumbers.getChildren().contains(paneNumbersPlaceHolder)) {
            vBoxNumbers.getChildren().remove(paneNumbersPlaceHolder);
            Platform.runLater(vBoxNumbers::requestLayout);
        }
    }

    private void processFilterChange(String filter) {
        if (!filter.isEmpty()) {
            statusManager.setStatus(Strings.STATUS_FILTER_SET.toString() + filter);
        } else {
            statusManager.setStatus(Strings.STATUS_FILTER_EMPTY.toString());
        }

        ControllerFactory.getFileTabController().getActiveItem().getFileContent().processFilterChange(filter);
    }

    @FXML
    private void onClick(MouseEvent event) {
        FileTab fileTab = ControllerFactory.getFileTabController().getActiveItem();
        fileTab.setTabModified(false);
        
        if(Option.getBoolean(Property.LOAD_ON_CONTENT_FOCUS)){
            fileTab.getFileContent().processFileModified();
        }
    }

    @FXML
    private void onKeyPressedContent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            contentListView.getSelectionModel().clearSelection();
            return;
        }

        if (event.isShiftDown() && event.getCode() == KeyCode.F3) {
            updateSearchResultIndex(searchResultsIndex - 1);
            trySelectContentIndex();
            return;
        }

        if (event.getCode().equals(KeyCode.F3)) {
            updateSearchResultIndex(searchResultsIndex + 1);
            trySelectContentIndex();
        }
    }

    @FXML
    private void onKeyPressedFilter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            processFilterChange(txtFilter.getText());
            contentListView.requestFocus();
            return;
        }

        if (event.getCode().equals(KeyCode.ESCAPE)) {
            contentListView.requestFocus();
        }
    }

    @FXML
    private void chckFilter(ActionEvent event) {
        if (!chckFilter.isSelected()) {
            txtFilter.setText(Strings.EMPTY.toString());
        }
        processFilterChange(txtFilter.getText());
    }

    @FXML
    private void chckFollowTrail(ActionEvent event) {
        ControllerFactory.getFileTabController().getActiveItem().setFollowTrail(chckFollowTrail.isSelected());
    }
}
