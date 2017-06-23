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
    public byte[] receiveMsg = new byte[13];
    public byte[] sendMsg = new byte[13];
    public String IPv4 = new String();
    public Point punktsend=new Point(0,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0);
    public Point punktreceive=new Point(0,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0);
	byte[] byteX=new byte[4];
	byte[] byteY=new byte[4];
    public Queue<Point> fifoclient = new LinkedList<Point>();
	public Point lastpunkt= new Point(0,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0);
    
    public void sendData(Point current){
		if(current!=null&&current.type!=0){
			if (lastpunkt.x != current.x && lastpunkt.y != current.y) {
				byte[] bytesX = new byte[] { (byte) (current.x >> 24), (byte) (current.x >> 16),
						(byte) (current.x >> 8), (byte) current.x };
				byte[] bytesY = new byte[] { (byte) (current.y >> 24), (byte) (current.y >> 16),
						(byte) (current.y >> 8), (byte) current.y };
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
				lastpunkt.copy(current);
			}
		}
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
			Point punkt=new Point(x, y, s, t, r, g, b);
			fifoclient.add(punkt);
		}
	}
    
    public void run(){
        SocketHints hints = new SocketHints();
        hints.connectTimeout = 10000;
        try {
           socket = Gdx.net.newClientSocket(Protocol.TCP, IPv4 , 11564, hints );
           Thread.sleep(500, 0);
           } catch (Exception e) {
         	   e.printStackTrace();
           }
		while(true)
        {
            if(socket!=null)
            {
                try {
                	//chwilowo klient tylko czyta, do odbioru/czytania potrzeba tokena
                    //socket.getOutputStream().write(sendMsg); // wiadomosc wysylana
                    socket.getInputStream().read(receiveMsg, 0, receiveMsg.length); //odebrana od servera
                	receiveData();
                } catch (IOException e) {
                    e.printStackTrace();
                    socket.dispose();
                }
            }
        }       
    }
} 