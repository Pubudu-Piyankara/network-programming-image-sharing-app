package client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import javax.imageio.ImageIO;

public class Client {
    // Define image label and socket as class variables for access within methods
    private static JLabel imageLabel;
    private static Socket socket;

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Image Uploader");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        imageLabel = new JLabel();
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
                    socket = new Socket("localhost", 6000);
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
    
    private static void placeComponents2(JPanel panel) {
        panel.setLayout(null);

        imageLabel = new JLabel();
        imageLabel.setBounds(10, 10, 150, 150);
        panel.add(imageLabel);

    }

    public static void rImageData() {
    	JFrame frame = new JFrame("Image Uploader");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents2(panel);
        frame.setVisible(true);
        try {
        	Socket socket = new Socket("localhost", 6001);
        	
            // Receive the image data from the server
            InputStream inputStream = socket.getInputStream();
            BufferedImage receivedImage = ImageIO.read(inputStream);

            // Display the received image data in the client's UI
            ImageIcon receivedIcon = new ImageIcon(receivedImage);
            imageLabel.setIcon(receivedIcon);
            System.out.println("Received image: " + receivedIcon);
            
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Receiving Image: " + ex.getMessage());
        }
    }
}
