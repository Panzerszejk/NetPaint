package com.mygdx.paint;

import java.io.IOException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class ServerThread extends Thread{
	ServerSocketHints hints = new ServerSocketHints();
    SocketHints socketHints = new SocketHints();
    ServerSocket server;
    Socket socket;
    public byte[] ReceiveMsg = new byte[1024];
    public byte[] SendMsg = new byte[1024];
    public String IPv4 = new String();
    public void run()
    {
    	try{
    		hints.acceptTimeout=10000;
    		socketHints = new SocketHints();
    		server = Gdx.net.newServerSocket(Protocol.TCP, IPv4 , 8783, hints);   
    		socket  = server.accept(socketHints);
    		Thread.sleep(500);
        } catch (Exception e) {
     	   e.printStackTrace();
        }
		while(true)
        {
            if(socket != null)
            {
                try {
                    socket.getInputStream().read(ReceiveMsg, 0, ReceiveMsg.length);
                    socket.getOutputStream().write(SendMsg);                                                                          //---

                } catch (IOException e) {
                    e.printStackTrace();
                    server.dispose();
                }
            }
                    
        }
    }
}