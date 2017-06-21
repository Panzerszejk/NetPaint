package com.mygdx.paint;

import java.io.IOException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class ClientThread extends Thread{
    Socket socket;
    public byte[] ReceiveMsg = new byte[1024];
    public byte[] SendMsg = new byte[1024];
    public String IPv4 = new String();
    public void run(){
        SocketHints hints = new SocketHints();
        hints.connectTimeout = 10000;
        try {
           socket = Gdx.net.newClientSocket(Protocol.TCP, IPv4 , 8783, hints );
           Thread.sleep(500, 0);
           } catch (Exception e) {
         	   e.printStackTrace();
           }
		while(true)
        {
            if(socket!=null)
            {
                try {
                    socket.getOutputStream().write(SendMsg); // wiadomosc wysylana
                    socket.getInputStream().read(ReceiveMsg, 0, ReceiveMsg.length); //odebrana od servera
                } catch (IOException e) {
                    e.printStackTrace();
                    socket.dispose();
                }
            }
        }       
    }
} 