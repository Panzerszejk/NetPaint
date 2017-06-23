package com.mygdx.paint;

public class Point {
	
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

