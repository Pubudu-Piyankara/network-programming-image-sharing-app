package server;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server {
	
	private static final int PORT = 6000;

    public static void main(String[] args) throws IOException {
    	JFrame jf = new JFrame("Server");
    	jf.setSize(720, 480);    
    	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	JLabel jLabel = new JLabel("Waiting...");		
    	
    	jf.add(jLabel, BorderLayout.SOUTH);

    	jf.setVisible(true);
    	//Listening to client request
        ServerSocket serversocket = new ServerSocket(PORT);
        try {
        	while(true) {
        		//accept client request
        		Socket socket = serversocket.accept();
        		System.out.println("Client is connected...");
        		InputStream inputStream = socket.getInputStream();
        		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        		BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
        		bufferedInputStream.close();
        		
        		JLabel jLabelPic = new JLabel(new ImageIcon(bufferedImage));
        		jLabel.setText("Image received");
        		jf.add(jLabelPic, BorderLayout.CENTER);
        		
        		try {
        			PrintWriter response = new PrintWriter(socket.getOutputStream(),true);
        			response.println("This is the response from the server");
        		}
        		finally {
        			socket.close();
        		}
        		
        	}
    }finally{
    	serversocket.close();
    }
    }

}

