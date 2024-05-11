package server;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Server {
    private static final int PORT = 6000;

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
                Socket socket = serverSocket.accept();
                statusLabel.setText("Receiving Image...");

                InputStream inputStream = socket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                
                BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
                bufferedInputStream.close();

                if (bufferedImage != null) {
                    // Display the received image
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
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            statusLabel.setText("Error: " + ex.getMessage());
        } finally {
            serverSocket.close();
        }

    }
}
