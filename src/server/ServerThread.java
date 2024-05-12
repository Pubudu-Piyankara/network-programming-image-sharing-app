// File: ServerThread.java

package server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ServerThread extends Thread {
    private Socket socket;
    private JLabel imageLabel;
    private JLabel statusLabel;
    private int client_id;
    private int thread_id;

    public ServerThread(Socket socket,int cid, JLabel imgLabel, JLabel statLabel) {
        this.socket = socket;
        this.imageLabel = imgLabel;
        this.statusLabel = statLabel;
        this.client_id = cid;
        this.thread_id = cid;
    }

    @Override
    public void run() {
        try {
            statusLabel.setText("client " + client_id + " Receiving Image...");
            System.out.println("Thread "+ thread_id+" : client " + client_id + " Receiving Image...");

            InputStream inputStream = socket.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            // Read the image bytes from the input stream
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            buffer.flush();
            bufferedInputStream.close();

            // Convert the received bytes to a BufferedImage
            byte[] imageData = buffer.toByteArray();
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
            buffer.close();

            // Display the received image on the server UI
            if (bufferedImage != null) {
                imageLabel.setIcon(new ImageIcon(bufferedImage));

                // Save the received image to a specified directory
                String directory = "C:\\Users\\ASUS\\OneDrive\\Desktop\\server\\";
                File imageFile = new File(directory +"client_" + client_id + "_received_image.jpg");
                ImageIO.write(bufferedImage, "jpg", imageFile);

                // Display the path of the saved image
                statusLabel.setText("Image Received by "+"client " + client_id +" and Saved: " + imageFile.getAbsolutePath());
                System.out.println("Image Received by "+"client " + client_id +" and Saved: " + imageFile.getAbsolutePath());
            } else {
                statusLabel.setText("Error: Failed to read image from input stream");
                System.out.println("Error: Failed to read image from input stream");
            }

            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }
}
