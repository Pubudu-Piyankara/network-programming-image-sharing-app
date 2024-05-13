package server;

import javax.swing.*;
import java.awt.*;

public class ServerUI {
    private JFrame frame;
    private JLabel imageLabel;
    private JLabel statusLabel;

    public ServerUI() {
        frame = new JFrame("Image Receiver");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        imageLabel = new JLabel();
        panel.add(imageLabel, BorderLayout.CENTER);

        statusLabel = new JLabel("Waiting for Image...");
        panel.add(statusLabel, BorderLayout.SOUTH);

        frame.add(panel);
    }

    public void display() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    public void setImageIcon(ImageIcon icon) {
        imageLabel.setIcon(icon);
    }

    public void setStatusText(String text) {
        statusLabel.setText(text);
    }

    public static void main(String[] args) {
        ServerUI serverUI = new ServerUI();
        serverUI.display();
    }
}
