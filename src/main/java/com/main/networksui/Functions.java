package com.main.networksui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Functions {

    public static void addMessage(String text, boolean sent) {

        VBox chatVBox = (VBox) Controller.currentStage.getScene().lookup("#chatVBox");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Pane messagePane = new Pane();
        HBox messageHBox = new HBox();
        StackPane textStackPane = new StackPane();
        StackPane dateStackPane = new StackPane();
        Text messageText = new Text();
        Text messageDate = new Text();

        messageDate.setText(dateFormat.format(new Date().getTime()));
        messageDate.getStyleClass().add("messageDateText");

        dateStackPane.getStyleClass().add("messageDatePane");
        dateStackPane.getChildren().add(messageDate);

        messageText.setText(text);
        messageText.setWrappingWidth(210);
        messageText.getStyleClass().add("messageContentText");

        textStackPane.getStyleClass().add("messageContentPane");
        messagePane.getStyleClass().add(sent ? "sentMessagePane":"receivedMessagePane");
        textStackPane.getChildren().add(messageText);

        messageHBox.getChildren().add(sent ? dateStackPane:textStackPane);
        messageHBox.getChildren().add(sent ? textStackPane:dateStackPane);

        messagePane.getChildren().add(messageHBox);
        chatVBox.getChildren().add(messagePane);
    }

}
