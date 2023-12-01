package com.main.networksui;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Controller implements Initializable {

    @FXML
    ScrollPane chatPane;
    @FXML
    TextField chatField;
    @FXML
    TextField remoteIPField,remotePortField;
    @FXML
    TextField localIPField,localPortField;
    @FXML
    TextField TCPServerIPField,TCPServerPortField;
    @FXML
    ComboBox<String> interfacesComboBox;
    @FXML
    TextArea statusArea;

    public static Stage currentStage;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* Events */
        TextField []numericFields = {remotePortField,TCPServerPortField};
        for(TextField tf:numericFields) {
            tf.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
        localPortField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                localPortField.setText(newValue.replaceAll("[^\\d]", ""));
            } else {
                ReceiverUDP.updatePort(Integer.parseInt(newValue));
            }
        });

        /*      ComboBox initialize   */

        for(String inter: Functions.getInterfaces()) {
            interfacesComboBox.getItems().add(inter);
        }



        VBox vBox = new VBox();
        vBox.getStyleClass().add("chatVBox");
        vBox.setId("chatVBox");
        chatPane.setContent(vBox);
        Functions.getInterfaces();
        new Thread(ReceiverUDP::receiveUDP).start(); // Start the udp listener with 1234 as default port
    }

    void changeStatus(String text) {
        statusArea.setText(text);
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

    public void interfacesComboBoxOnChanged() {
        String selected = interfacesComboBox.getSelectionModel().getSelectedItem();
        String ip = selected.split(": ")[1];
        localIPField.setText(ip);
    }


}
