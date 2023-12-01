package com.main.networksui;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
public class Controller implements Initializable {

    @FXML
    ScrollPane chatPane;
    @FXML
    TextField chatField,serverIPField,serverPortField;

    public static Stage currentStage;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /* Events */
        serverPortField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                serverPortField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        VBox vBox = new VBox();
        vBox.getStyleClass().add("chatVBox");
        vBox.setId("chatVBox");
        chatPane.setContent(vBox);

        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    Functions.addMessage("Hi hau??",true);
                });
            }
        }).start();
    }

    public void sendButtonClicked(ActionEvent event) {
        if(!chatField.getText().isEmpty() && !serverIPField.getText().isEmpty() && !serverPortField.getText().isEmpty()) {
            String message = chatField.getText();
            String ip = serverIPField.getText();
            int port = Integer.parseInt(serverPortField.getText());
            new Thread((() -> {
                try {
                    Functions.sendUDP(message, ip, port);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
    }


}
