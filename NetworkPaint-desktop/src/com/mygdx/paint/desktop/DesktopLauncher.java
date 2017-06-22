package com.mygdx.paint.desktop;

import java.util.Scanner;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.paint.NetworkPaint;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.height = 600;
		config.width = 800;
		NetworkPaint paint=new NetworkPaint();
		
		System.out.println("Are you a server or client?  S/C: ");
		Scanner scanner = new Scanner(System.in);
		String ClientServerSelect=scanner.nextLine();
		
		System.out.println("Enter server IPv4: ");
		String ServerClientIP=scanner.nextLine();
		scanner.close();
		paint.ClientServerSelect=ClientServerSelect;
		paint.ServerClientIP=ServerClientIP;
		
		new LwjglApplication(paint, config);
	}
}
