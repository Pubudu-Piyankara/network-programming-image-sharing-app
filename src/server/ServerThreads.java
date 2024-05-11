// File: ServerThread.java

package server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ServerThreads extends Thread {
    private Socket socket;
    private JLabel imageLabel;
    private JLabel statusLabel;

    public ServerThreads(Socket socket, JLabel imageLabel, JLabel statusLabel) {
        this.socket = socket;
        this.imageLabel = imageLabel;
        this.statusLabel = statusLabel;
    }

    @Override
    public void run() {
        try {
            statusLabel.setText("Receiving Image...");

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
                File imageFile = new File(directory + "received_image.jpg");
                ImageIO.write(bufferedImage, "jpg", imageFile);

                // Display the path of the saved image
                statusLabel.setText("Image Received and Saved: " + imageFile.getAbsolutePath());
            } else {
                statusLabel.setText("Error: Failed to read image from input stream");
            }

            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }
}
