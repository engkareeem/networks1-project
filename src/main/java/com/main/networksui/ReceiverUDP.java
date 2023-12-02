package com.main.networksui;

import javafx.application.Platform;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public class ReceiverUDP {
    private static int listeningPort;
    private static DatagramSocket datagramSocket;

    private static Thread mainThread;

    static {
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init(){
        mainThread = new Thread(ReceiverUDP::receiveUDP); // Start the udp listener with 1234 as default port
        mainThread.start();
    }

    public static void updatePort(int port){
        if(port == listeningPort) return;
        listeningPort = port;
        try {
            mainThread.interrupt();
            datagramSocket.close();
            datagramSocket = new DatagramSocket(listeningPort);
            init();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }


    public static void receiveUDP() {
        DatagramPacket packet = null;
        byte[] receiveArray = new byte[65535];
        while (!Thread.interrupted()) {
            packet = new DatagramPacket(receiveArray, receiveArray.length);

            try {
                datagramSocket.receive(packet);
            } catch (IOException e) {
                continue;
//                throw new RuntimeException(e);
            }
            String msg = new String(packet.getData()).trim();
            if(msg.contains("CMD")){
                processCommand(msg);
                continue;
            }

            Platform.runLater(() -> {
                Functions.addMessage(msg, false);

            });
            receiveArray = new byte[65535];

        }
    }

    public static void processCommand(String cmd) {
        if(cmd.contains("delete@")){
            // TODO: delete a msg
            String id = cmd.split("@")[1];
        }else if(cmd.contains("deleteAll")){
            //TODO: delete all user message
        }
    }



}