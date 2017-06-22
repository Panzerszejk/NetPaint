package com.mygdx.paint;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;


public class MyInputProcesor implements InputProcessor {

    private Queue<Point> fifo = new LinkedList<Point>();
    private byte brush_size;
    private byte r;
    private byte g;
    private byte b;
    private Point point = new Point(0,0,(byte)0,(byte)5,(byte)255,(byte)255,(byte)255);
    public Point pollFifo () {
    	return fifo.poll();
    }
    public void addFifo(Point object){
    	fifo.add(object);
    }
    public void set_brush_size(byte rozmiar)
    {
    this.brush_size = rozmiar;
    }
    
    public void set_kolor(byte r, byte g, byte b)
    {
    this.r = r;
    this.g = g;
    this.b = b;
    }
    
	@Override
    public boolean keyDown (int keycode) {
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped (char character) {
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
    	if(button == Buttons.LEFT) {
    		point.x = x;
    		point.y = y;
    		point.type = (byte)1;
    		fifo.add(point);
    	}
    	
    	if(button == Buttons.RIGHT) {
    		
    	}
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
    	point.x = x;
		point.y = y;
		point.type = (byte)0;
		fifo.add(point);
    	return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
    	point.x = x;
		point.y = y;
		point.type = (byte)2;
		fifo.add(point);
    	return false;
    }

    @Override
    public boolean scrolled (int amount) {
        return false;
    }

	@Override
	public boolean mouseMoved(int x, int y) {	
		point.x = x;
		point.y = y;
		point.type = (byte)3;
		fifo.add(point); 
		return false;
	}
}
