package com.mygdx.paint;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Rect implements Shape {
	
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
	public Rect(int x, int y, byte brush_size, byte type, byte r, byte g, byte b,byte id)
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
	
	public void copy(Rect Rect)
	{
		this.x = Rect.x;
		this.y = Rect.y;
		this.brush_size = Rect.brush_size;
		this.type = Rect.type;
		this.r = Rect.r;
		this.g = Rect.g;
		this.b = Rect.b;
		this.id=Rect.id;
	}
	
	public boolean equal(Rect Rect){
		if(this.type==Rect.type&&this.x==Rect.x&&this.y==Rect.y&&this.brush_size==Rect.brush_size&&this.r==Rect.r&&this.id==Rect.id){
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
		shapeRenderer.rect(x, height-y, x1, y1);
		shapeRenderer.end();
	}
	
	public Rect(Rect Rect)
	{
		this.x = Rect.x;
		this.y = Rect.y;
		this.brush_size = Rect.brush_size;
		this.type = Rect.type;
		this.r = Rect.r;
		this.g = Rect.g;
		this.b = Rect.b;
		this.id=Rect.id;
	}
}