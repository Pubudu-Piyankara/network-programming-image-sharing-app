package client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Client {
    public static void main(String[] args) throws IOException {
    	Socket s = new Socket("localhost", 6000);
    	System.out.println("Server connected");
    	
    	JFrame jf = new JFrame("Client");
    	jf.setSize(720, 480);    
    	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	ImageIcon imgIcon = new ImageIcon("C:\\Users\\ASUS\\Downloads\\quality.png");
    	JLabel jLabel = new JLabel(imgIcon);
    	JButton btn = new JButton("Send");
    	
    	jf.add(jLabel);
    	jf.add(btn);
    	jf.setVisible(true);
    	
    	btn.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent event) {
    			try {
    				OutputStream outputStream = s.getOutputStream();
    				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
    				
    				Image image = imgIcon.getImage();
    				BufferedImage bufferedImage = new BufferedImage(image.getHeight(null), image.getWidth(null), BufferedImage.TYPE_INT_RGB);
    				
    				Graphics graphics = bufferedImage.createGraphics();
    				graphics.drawImage(image, 0, 0, null);
    				graphics.dispose();
    				
    				ImageIO.write(bufferedImage, "png", bufferedOutputStream);
    				bufferedOutputStream.close();
    				s.close();
    			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	});
    	}
}
