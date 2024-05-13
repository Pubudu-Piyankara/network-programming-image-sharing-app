package server;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Server {
    private static final int PORT = 6000;
    private static int current_clients = 1;
    private static ServerUI serverUI;
    private static List<ServerThread> connectedClients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        serverUI = new ServerUI();
        serverUI.display();

        ServerSocket serverSocket = new ServerSocket(PORT);
        try {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client is waiting............\n\n");

                ServerThread serverThread = new ServerThread(client, current_clients, serverUI);

                // Add the new client thread to the list of connected clients
                connectedClients.add(serverThread);

                System.out.println("Client " + current_clients + " connected");
                serverThread.start();
                current_clients++;
            }
        } finally {
            serverSocket.close();
        }
    }

    public static void broadcastImageData(BufferedImage imageData) throws IOException {
    	
        for (ServerThread clientThread : connectedClients) {
            try {
            	BufferedImage serverThread = clientThread.receiveImageData();
                System.out.print( "server sending...");
      
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception
            }
        }
        
    }


}
