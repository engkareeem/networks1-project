package com.main.networksui;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    ScrollPane chatPane;
    @FXML
    TextField chatField,remoteIPField,remotePortField;

    public static Stage currentStage;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* Events */
        remotePortField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                remotePortField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        VBox vBox = new VBox();
        vBox.getStyleClass().add("chatVBox");
        vBox.setId("chatVBox");
        chatPane.setContent(vBox);
        Functions.getInterfaces();
        new Thread(ReceiverUDP::receiveUDP).start(); // Start the udp listener with 1234 as default port
    }

    public void sendButtonClicked() {
        if(!chatField.getText().isEmpty() && !remoteIPField.getText().isEmpty() && !remotePortField.getText().isEmpty()) {
            String message = chatField.getText();
            String ip = remoteIPField.getText();
            int port = Integer.parseInt(remotePortField.getText());
            new Thread(() -> {
                try {
                    Functions.sendUDP(message, ip, port);
                    chatField.clear();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }


}
