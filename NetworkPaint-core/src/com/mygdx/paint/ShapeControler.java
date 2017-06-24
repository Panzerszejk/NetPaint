package com.mygdx.paint;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ShapeControler {
	Shape shapeInstance;
	
	public ShapeControler(int shp,int x,int y,byte brush,byte type,byte r,byte g,byte b,byte id)
	{
		switch(shp)
		{
		case 0:
		shapeInstance = new Point(x, y, brush, type ,r,g,b,id);
		break;
		case 1:
		shapeInstance = new Rect(x, y, brush, type ,r,g,b,id);
		break;
		default:
		shapeInstance = new Rect(x, y, brush, type ,r,g,b,id);
		}
		
	}
	
	public void draw(ShapeRenderer shapeRenderer,int x1,int y1,int height)
	{
		shapeInstance.draw(shapeRenderer, x1, y1, height);
	}
	

}
