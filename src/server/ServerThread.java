package server;

import java.io.*;
import java.net.Socket;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ServerThread extends Thread {
    private Socket socket;
    private ServerUI serverUI;
    private int client_id;
    private int thread_id;

    public ServerThread(Socket socket, int cid, ServerUI serverUI) {
        this.socket = socket;
        this.serverUI = serverUI;
        this.client_id = cid;
        this.thread_id = cid;
    }

    @Override
    public void run() {
        try {
            serverUI.setStatusText("Client " + client_id + " Receiving Image...");
            System.out.println("Thread " + thread_id + " : Client " + client_id + " Receiving Image...");

            // Receive the image data from the client
            BufferedImage bufferedImage = receiveImageData();

            if (bufferedImage != null) {
                ImageIcon imageIcon = new ImageIcon(bufferedImage);
                serverUI.setImageIcon(imageIcon);

                String directory = "C:\\Users\\ASUS\\OneDrive\\Desktop\\server\\";
                File imageFile = new File(directory + "client_" + client_id + "_received_image.jpg");
                ImageIO.write(bufferedImage, "jpg", imageFile);

                serverUI.setStatusText("Image Received by Client " + client_id + " and Saved: " + imageFile.getAbsolutePath());
                System.out.println("Image Received by Client " + client_id + " and Saved: " + imageFile.getAbsolutePath());

                // Broadcast the received image data to all clients
                Server.broadcastImageData(bufferedImage);
            } else {
                serverUI.setStatusText("Error: Failed to read image from input stream");
                System.out.println("Error: Failed to read image from input stream");
            }

            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            serverUI.setStatusText("Error: " + ex.getMessage());
        }
    }

    public BufferedImage receiveImageData() throws IOException {
        InputStream inputStream = socket.getInputStream();
        System.out.println("thread reeive image");
        return ImageIO.read(inputStream);
        
    }
}
