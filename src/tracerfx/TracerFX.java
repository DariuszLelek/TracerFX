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

package tracerfx;

import javafx.scene.image.Image;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tracerfx.utilities.ScheduledExecutor;

/**
 *
 * @author Dariusz Lelek
 */
public class TracerFX extends Application {
    private final static String TITLE = "TracerFX";
    private final static String FXML = "tracerfx/fxml/TracerFXML.fxml";
    private final static String IMAGE = "tracerfx/image.png";
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(getScene());
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(300);
        primaryStage.getIcons().add(new Image(IMAGE));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            ScheduledExecutor.stopScheduledExecutor();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private Parent getParent() throws IOException{
        return FXMLLoader.load(getClass().getClassLoader().getResource(FXML));
    }
    
    private Scene getScene() throws IOException{
        return new Scene(getParent());
    } 
}
