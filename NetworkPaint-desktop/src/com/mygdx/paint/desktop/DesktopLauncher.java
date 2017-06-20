package com.mygdx.paint.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.paint.NetworkPaint;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		//config.height = 800;
		//config.width = 1280;
		new LwjglApplication(new NetworkPaint(), config);
	}
}
