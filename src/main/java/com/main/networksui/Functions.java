package com.main.networksui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Enumeration;

public class Functions {

    public static void addMessage(String text, boolean sent) {

        Long timestamp = new Date().getTime();
        VBox chatVBox = (VBox) Controller.currentStage.getScene().lookup("#chatVBox");
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a"); // DD for date, E for day name
        Pane messagePane = new Pane();
        HBox messageHBox = new HBox();
        StackPane textStackPane = new StackPane();
        StackPane dateStackPane = new StackPane();
        Text messageText = new Text();
        Text messageDate = new Text();
        Button deleteMessageButton = new Button();

        String timestampID = timestamp.toString();
        messageHBox.setId(timestampID);


        deleteMessageButton.setOnAction(event -> {
            Functions.deleteMessage(messageHBox.getId());
        });

        deleteMessageButton.getStyleClass().add("deleteMessageButton");

        messageDate.setText(dateFormat.format(timestamp));
        messageDate.getStyleClass().add("messageDateText");

        dateStackPane.getStyleClass().add("messageDatePane");
        dateStackPane.getChildren().add(messageDate);

        messageText.setText(text);
        messageText.setWrappingWidth(sent? 185: 210);
        messageText.getStyleClass().add("messageContentText");

        textStackPane.getStyleClass().add("messageContentPane");
        messagePane.getStyleClass().add(sent ? "sentMessagePane":"receivedMessagePane");
        textStackPane.getChildren().add(messageText);

        messageHBox.getChildren().add(sent ? textStackPane:dateStackPane);
        if(sent) messageHBox.getChildren().add(deleteMessageButton);
        messageHBox.getChildren().add(sent ? dateStackPane:textStackPane);

        messagePane.getChildren().add(messageHBox);
        chatVBox.getChildren().add(messagePane);
    }
    public static void deleteMessage(String msgID) {
        String ip = ((TextField) Controller.currentStage.getScene().lookup("#remoteIPField")).getText();
        String port = ((TextField) Controller.currentStage.getScene().lookup("#remotePortField")).getText();

        sendUDP("CMD@delete@" + msgID, ip, Integer.parseInt(port));
        try {
            VBox vBox = (VBox) (Controller.currentStage.getScene().lookup("#chatVBox"));
            vBox.getChildren().remove(vBox.lookup("#" + msgID));
        } catch (Exception e) {
            System.out.println("Error occurred while deleting message pane");
        }
    }

    public static ArrayList<String> getInterfaces(){
        ArrayList<String> interfaces = new ArrayList<>();

        try {

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();


            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if(networkInterface.getInetAddresses().hasMoreElements()){
                    String interfaceAddress = networkInterface.getInetAddresses().nextElement().getHostAddress();
                    String name = networkInterface.getName().replaceAll("wlan\\d*", "Wi-Fi").replaceAll("lo\\d*", "LocalHost").replaceAll("eth\\d*", "Ethernet");
                    if(interfaceAddress.matches("\\d+.\\d+.\\d+.\\d+")){
                        interfaces.add(name + ": " + interfaceAddress);
                    }
                }

            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return interfaces;
    }

    public static void sendUDP(String message,String ip, int port) {

        try {
            DatagramSocket ds = new DatagramSocket();
            byte[] buf;
            buf = message.getBytes();
            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), port);
            ds.send(DpSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}