package server;

import java.awt.BorderLayout;
import java.io.IOException;

import java.net.*;
import javax.swing.*;



public class Server {
    private static final int PORT = 6000;
    private static int current_clients = 1;

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Image Receiver");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel imageLabel = new JLabel();
        frame.add(imageLabel, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("Waiting for Image...");
        frame.add(statusLabel, BorderLayout.SOUTH);

        frame.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(PORT);
        
        try {
			while (true) {
				// Listening for any client socket requests
				Socket client = serverSocket.accept();
				System.out.println("Client is waiting............\n\n");

				ServerThread ct = new ServerThread(client,current_clients, statusLabel, statusLabel);
				System.out.println("Client " +current_clients + " connected");
				ct.start();
				current_clients++;
			}
        }finally {
            serverSocket.close();
        }

    }
}
