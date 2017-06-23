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
		
		System.out.println("For online write: online ");
		Scanner scanner = new Scanner(System.in);
		String ClientServerSelect=scanner.nextLine();
		
		System.out.println("Enter your IPv4: ");
		String ServerIP=scanner.nextLine();
		System.out.println("Enter foreign IPv4: ");
		String ClientIP=scanner.nextLine();
		scanner.close();
		paint.ClientServerSelect=ClientServerSelect;
		paint.ServerIP=ServerIP;
		paint.ClientIP=ClientIP;
		new LwjglApplication(paint, config);
	}
}
