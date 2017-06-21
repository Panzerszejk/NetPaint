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
    
    public Point pollFifo () {
    	return fifo.poll();
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
    		fifo.add(new Point(x,y,brush_size,(byte)1,r,g,b));
    	}
    	
    	if(button == Buttons.RIGHT) {
    		
    	}
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
    		fifo.add(new Point(x,y,brush_size,(byte)0,r,g,b));
    	return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
    	fifo.add(new Point(x,y,brush_size,(byte)2,r,g,b));
    	return false;
    }

    @Override
    public boolean scrolled (int amount) {
        return false;
    }

	@Override
	public boolean mouseMoved(int x, int y) {	
		fifo.add(new Point(x,y,brush_size,(byte)3,r,g,b));  
		return false;
	}
}
