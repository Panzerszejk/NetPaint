package com.mygdx.paint;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class ClientThread extends Thread{
    Socket socket;
    static ClientThread instance;
    public byte[] receiveMsg = new byte[13];
    public String IPv4 = new String();
	byte[] byteX=new byte[4];
	byte[] byteY=new byte[4];
    public Queue<Point> fifoclient = new LinkedList<Point>();    
    public static ClientThread get() {
        if (instance == null) instance = new ClientThread();
        return instance;
    }
	
    private ClientThread()
    {
    	
    }
	
    public void receiveData(){
		if(receiveMsg!=null){
			System.arraycopy(receiveMsg, 0, byteX, 0, 4);
			System.arraycopy(receiveMsg, 4, byteY, 0, 4);
			int x=(receiveMsg[0] << 24 | (receiveMsg[1] & 0xFF) << 16 | (receiveMsg[2] & 0xFF) << 8 | (receiveMsg[3] & 0xFF));
			int y=(receiveMsg[4] << 24 | (receiveMsg[5] & 0xFF) << 16 | (receiveMsg[6] & 0xFF) << 8 | (receiveMsg[7] & 0xFF));
			byte s=receiveMsg[8];
			byte t=receiveMsg[9];
			byte r=receiveMsg[10];
			byte g=receiveMsg[11];
			byte b=receiveMsg[12];
			Point punkt=new Point(x, y, s, t, r, g, b,(byte)1);
			if(punkt.x<800&&punkt.x>0&&punkt.y<600&&punkt.y>0)
				fifoclient.add(punkt);
			System.out.println(punkt.x+" "+punkt.y);
			System.out.println("Size: "+fifoclient.size());
		}
	}
    
    public void run(){
    	boolean wait=true;
        SocketHints hints = new SocketHints();
        hints.connectTimeout = 10000;
        hints.tcpNoDelay = false;
        hints.trafficClass = 0x22;
		while (wait) {
	        try {
	        	wait=false;
	            socket = Gdx.net.newClientSocket(Protocol.TCP, IPv4 , 11830, hints );
	            } catch (Exception e) {
	          	   wait=true;
	            }
		}

		while(true)
        {
            if(socket!=null)
            {
                try {
                    socket.getInputStream().read(receiveMsg, 0, receiveMsg.length); //odebrana od server
                    receiveData();
                } catch (IOException e) {
                    e.printStackTrace();
                    socket.dispose();
                }
            }
        }       
    }
} 