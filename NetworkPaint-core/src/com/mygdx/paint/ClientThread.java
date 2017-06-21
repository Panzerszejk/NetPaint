package com.mygdx.paint;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class ClientThread extends Thread{
    Socket socket;
    SocketHints hints = new SocketHints();
    byte[] ReceiveMsg = new byte[1024];
    byte[] SendMsg = new byte[1024];
    public void run(){
        hints.connectTimeout = 11000;
		while(true)
        {
            socket = Gdx.net.newClientSocket(Protocol.TCP, "localhost", 8784, hints );
            if(socket!=null)
            {
                try {
                    socket.getOutputStream().write( SendMsg.length); // wiadomosc wysylana
                    socket.getInputStream().read(ReceiveMsg, 0, ReceiveMsg.length); //odebrana od servera
                } catch (IOException e) {
                    e.printStackTrace();
                    socket.dispose();
                }
            }
        }       
    }
} 