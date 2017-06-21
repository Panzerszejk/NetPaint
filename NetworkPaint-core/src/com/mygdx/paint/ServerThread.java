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
    byte[] ReceiveMsg = new byte[1024];
    byte[] SendMsg = new byte[1024];
    public void run()
    {
        int tmp = 1;
		while(true)
        {
			if(tmp == 1) 
            {
                tmp=2;
                server = Gdx.net.newServerSocket(Protocol.TCP, "localhost", 8783, hints);   
                socket  = server.accept(socketHints);
                hints.acceptTimeout = 12000;
            }

            if(socket != null)
            {

                try {
                    socket.getInputStream().read(ReceiveMsg, 0, ReceiveMsg.length);
                    socket.getOutputStream().write( (SendMsg).length);                                                                          //---

                } catch (IOException e) {
                    e.printStackTrace();
                    server.dispose();
                }
            }
                    
        }
    }
}