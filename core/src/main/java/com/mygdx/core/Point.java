package com.mygdx.core;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;;

public class Point implements Shape {
	
	//parametry oraz rodzaj ruchu kursora(rysowania)
	int x;
	int y; 
	byte brush_size;
	byte type;
	// kolory
	byte r;
	byte g;
	byte b;
	byte id;
	
	
	//kontruktor klasy punktu
	public Point(int x, int y, byte brush_size, byte type, byte r, byte g, byte b,byte id)
	{
	this.x = x;
	this.y = y;
	this.brush_size = brush_size;
	this.type = type;
	this.r = r;
	this.g = g;
	this.b = b;
	this.id=id;
	}
	
	public void copy(Point point)
	{
		this.x = point.x;
		this.y = point.y;
		this.brush_size = point.brush_size;
		this.type = point.type;
		this.r = point.r;
		this.g = point.g;
		this.b = point.b;
		this.id=point.id;
	}
	
	public boolean equal(Point point){
		if(this.type==point.type&&this.x==point.x&&this.y==point.y&&this.brush_size==point.brush_size&&this.r==point.r){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void draw(ShapeRenderer shapeRenderer,int x1,int y1,int height)
	{
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor((r & 0xff)/255f, (g & 0xff)/255f, (b & 0xff)/255f, 1f);
		
		shapeRenderer.circle(x1,y1,brush_size/2);
		shapeRenderer.rectLine(x1,y1,x,height-y,brush_size); //Rysujemy "zaokralona" linie
		shapeRenderer.circle(x,height-y,brush_size/2);
		shapeRenderer.end();
	}
	
	public Point(Point point)
	{
		this.x = point.x;
		this.y = point.y;
		this.brush_size = point.brush_size;
		this.type = point.type;
		this.r = point.r;
		this.g = point.g;
		this.b = point.b;
		this.id=point.id;
	}
}

