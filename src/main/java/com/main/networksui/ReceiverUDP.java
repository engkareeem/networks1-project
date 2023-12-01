package com.main.networksui;

import javafx.application.Platform;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiverUDP {
    private static int listeningPort = 1234;
    private static DatagramSocket datagramSocket;

    static {
        try {
            datagramSocket = new DatagramSocket(listeningPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updatePort(int port){
        listeningPort = port;
        try {
            datagramSocket = new DatagramSocket(listeningPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }


    public static void receiveUDP() {
        DatagramPacket packet = null;
        byte[] receiveArray = new byte[65535];
        while (true) {
            packet = new DatagramPacket(receiveArray, receiveArray.length);

            try {
                datagramSocket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String msg = new String(packet.getData()).trim();

            Platform.runLater(() -> {
                Functions.addMessage(msg, false);

            });
            receiveArray = new byte[65535];

        }
    }

}
