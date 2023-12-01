package com.main.networksui;

import javafx.application.Platform;
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
import java.util.Date;
import java.util.Scanner;
import java.util.Enumeration;

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

    public static void getInterfaces(){
        try {

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.getName().startsWith("wlan")) {

                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();


                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();


                        System.out.println("Name: " + networkInterface.getName() + ", IP: " + inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void sendUDP(String message,String ip, int port) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        byte[] buf;
        buf = message.getBytes();
        DatagramPacket DpSend = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), port);
        ds.send(DpSend);
    }
}
