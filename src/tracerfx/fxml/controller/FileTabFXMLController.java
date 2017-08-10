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
import tracerfx.control.DescriptionController;
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

    private final ListProperty<Integer> searchResultIndexList = new SimpleListProperty<>();
    private final StatusManager statusManager = ManagerFactory.getStatusManager();
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
        final FileContentProperty fileContentProperty = ManagerFactory.getFileTabManager().getFileContentProperty();
        
        bindListSizeToText(new SimpleListProperty(fileContentProperty.getContentObservableList()), lblLines);
        bindListSizeToText(new SimpleListProperty(fileContentProperty.getOriginalContentObservableList()), lblTotalLines);
        bindListSizeToText(new SimpleListProperty(searchResultIndexList), lblLastSearchResultNum);

        contentListView.setItems(fileContentProperty.getContentObservableList());
        numberListView.setItems(fileContentProperty.getLineNumbersObservableList());
        searchResultIndexList.set(fileContentProperty.getSearchLineNumbersObservableList());
        
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
        contentListView.getSelectionModel().selectedItemProperty().addListener(DescriptionController.CHANGE_LISTENER_LINE_CHANGE);

        searchResultIndexList.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            contentListView.requestFocus();
            contentListView.getSelectionModel().clearSelection();
            numberListView.getSelectionModel().clearSelection();

            searchResultIndexList.stream().forEach(x -> numberListView.getSelectionModel().select(x.intValue()));

            trySelectContentIndex(0);
        });

    }

    private void trySelectContentIndex(int searchResultIndex) {
        updateSearchResultIndex(searchResultIndex);

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
            for (Node node : contentListView.lookupAll(".scroll-bar")) {
                if (node instanceof ScrollBar) {
                    ScrollBar scroll = (ScrollBar) node;
                    if (scroll.getOrientation() == Orientation.HORIZONTAL) {
                        processPlaceHolderInsert(scroll.isVisible());
                        scroll.visibleProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                            processPlaceHolderInsert(newValue);
                        });
                    }
                }
            }
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
            statusManager.setStatus(StringsFXML.STATUS_FILTER_SET.toString() + filter);
        } else {
            statusManager.setStatus(StringsFXML.STATUS_FILTER_EMPTY.toString());
        }

        ManagerFactory.getFileTabManager().getActiveItem().getFileContent().processFilterChange(filter);
    }

    @FXML
    private void onClick(MouseEvent event) {
        ManagerFactory.getFileTabManager().getActiveItem().processModified(false);
    }

    @FXML
    private void onKeyPressedContent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            contentListView.getSelectionModel().clearSelection();
            return;
        }

        if (event.isShiftDown() && event.getCode() == KeyCode.F3) {
            trySelectContentIndex(searchResultsIndex - 1);
            return;
        }

        if (event.getCode().equals(KeyCode.F3)) {
            trySelectContentIndex(searchResultsIndex + 1);
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
            txtFilter.setText("");
        }
        processFilterChange(txtFilter.getText());
    }

    @FXML
    private void chckFollowTrail(ActionEvent event) {
        ManagerFactory.getFileTabManager().getActiveItem().setFollowTrail(chckFollowTrail.isSelected());
    }
}
