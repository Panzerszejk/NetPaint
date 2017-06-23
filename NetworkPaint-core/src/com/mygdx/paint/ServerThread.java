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
    public byte[] receiveMsg = new byte[13];
    public byte[] sendMsg = new byte[13];
    public String IPv4 = new String();
    public Point punktsend=new Point(0,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0); 
    //kiedy dam null wywala nullpointerexception ??
    public Point punktreceive=new Point(0,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0);
	byte[] byteX=new byte[4];
	byte[] byteY=new byte[4];
	private Point lastpunkt=new Point(0,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0);

	public void sendData(Point current) {
		byte[] bytesX = new byte[] { (byte) (current.x >> 24), (byte) (current.x >> 16), (byte) (current.x >> 8),
				(byte) current.x };
		byte[] bytesY = new byte[] { (byte) (current.y >> 24), (byte) (current.y >> 16), (byte) (current.y >> 8),
				(byte) current.y };
		for (int i = 0; i < 4; i++) { // X to bytes
			sendMsg[i] = bytesX[i];
		}
		for (int i = 4; i < 8; i++) { // Y to bytes
			sendMsg[i] = bytesY[i - 4];
		}
		sendMsg[8] = current.brush_size;
		sendMsg[9] = current.type;
		sendMsg[10] = current.r;
		sendMsg[11] = current.g;
		sendMsg[12] = current.b;
	}
    
    
    public void run()
    {
    	try{
    		hints.acceptTimeout=10000;
            socketHints.tcpNoDelay = false;
            socketHints.trafficClass = 0x22;
    		server = Gdx.net.newServerSocket(Protocol.TCP, IPv4 , 71830, hints);   
    		socket  = server.accept(socketHints);
    		Thread.sleep(2000);
        } catch (Exception e) {
     	   e.printStackTrace();
        }
		while(true)
        {
            if(socket != null)
            {
            	System.out.print("");
				if (!lastpunkt.equal(punktsend)) {
					try {
						sendData(punktsend);
						socket.getOutputStream().write(sendMsg);
						lastpunkt.copy(punktsend);
						// chwilowo serwer tylko wysyla, do odbioru/wysylania
						// potrzeba tokena
						// socket.getInputStream().read(receiveMsg, 0,receiveMsg.length);
					} catch (IOException e) {
						e.printStackTrace();
						server.dispose();
					}
				}
				
            }
        }
    }
}