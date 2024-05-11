package client;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Client {
    public static void main(String[] args)throws IOException {
        JFrame frame = new JFrame("Image Uploader");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(10, 10, 150, 150);
        panel.add(imageLabel);

        JButton chooseButton = new JButton("Choose Image");
        chooseButton.setBounds(200, 30, 150, 30);
        panel.add(chooseButton);

        JButton sendButton = new JButton("Send Image");
        sendButton.setBounds(200, 80, 150, 30);
        panel.add(sendButton);

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    imageLabel.setIcon(imageIcon);
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket socket = new Socket("localhost", 6000);
                    OutputStream outputStream = socket.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

                    // Get the image icon from the image label
                    ImageIcon imageIcon = (ImageIcon) imageLabel.getIcon();
                    if (imageIcon != null) {
                        // Get the image from the image icon
                        // Convert the image to bytes
                        // Write the bytes to the output stream
                        Image image = imageIcon.getImage();
                        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics = bufferedImage.createGraphics();
                        graphics.drawImage(image, 0, 0, null);
                        graphics.dispose();

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                        byteArrayOutputStream.close();

                        bufferedOutputStream.write(imageBytes);
                        bufferedOutputStream.flush(); // Flush the output stream to ensure all data is sent

                        JOptionPane.showMessageDialog(null, "Image Sent Successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Please choose an image first.");
                    }

                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error Sending Image: " + ex.getMessage());
                }
            }
        });

    }
}
