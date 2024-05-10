package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private static final int PORT = 6000;

    public static void main(String[] args) throws IOException {
    	//Listening to client request
        ServerSocket socket = new ServerSocket(PORT);
        try {
        	while(true) {
        		//accept client request
        		Socket socketNew = socket.accept();
        		System.out.println("Client is connected...");
        		try {
        			PrintWriter response = new PrintWriter(socketNew.getOutputStream(),true);
        			response.println("This is the response from the server");
        		}
        		finally {
        			socketNew.close();
        		}
        		
        	}
    }finally{
    	socket.close();
    }
    }

}

