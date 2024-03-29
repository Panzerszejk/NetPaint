package com.mygdx.paint;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;


public class MyInputProcesor implements InputProcessor {

    private Queue<Point> fifo = new LinkedList<Point>();
   
    private Point point = new Point(0,0,(byte)8,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0);
    
    public Point pollFifo () {
    	return fifo.poll();
    }
    public void addFifo(Point object){
    	fifo.add(object);
    }
    
    public int getSizeFifo(){
    	return fifo.size();
    }
    
    public byte get_r()
    {
    	return point.r;
    }
    
    public byte get_g()
    {
    	return point.g;
    }
    
    public byte get_b()
    {
    	return point.b;
    }
    
    
    public void set_brush_size(byte rozmiar)
    {
    	
    	point.brush_size = rozmiar;
    }
    
    public void set_kolor(byte r, byte g, byte b)
    {
    point.r = r;
    point.g = g;
    point.b = b;
    }
    
    public byte get_brush_size()
    {
    	return point.brush_size;
    }
    
	@Override
    public boolean keyDown (int keycode) {
        
        if (keycode == Keys.NUM_0) {
        	point.r = (byte)255;
        	point.g = (byte)255;
        	point.b = (byte)255;
    		point.type = (byte)0;
    		fifo.add(point);
        }
        else if (keycode == Keys.NUM_1)
        {
        	point.r = (byte)0;
        	point.g = (byte)0;
        	point.b = (byte)0;
    		point.type = (byte)0;
    		fifo.add(point);
        }
        else if (keycode == Keys.NUM_2)
        {
        	point.r = (byte)246;
        	point.g = (byte)215;
        	point.b = (byte)81;
    		point.type = (byte)0;
    		fifo.add(point);
        }
        else if (keycode == Keys.NUM_3)
        {
        	point.r = (byte)245;
        	point.g = (byte)110;
        	point.b = (byte)68;
    		point.type = (byte)0;
    		fifo.add(point);
        }
        else if (keycode == Keys.NUM_4)
        {
        	point.r = (byte)238;
        	point.g = (byte)97;
        	point.b = (byte)118;
    		point.type = (byte)0;
    		fifo.add(point);
        }
        else if (keycode == Keys.NUM_5)
        {
        	point.r = (byte)144;
        	point.g = (byte)67;
        	point.b = (byte)202;
    		point.type = (byte)0;
    		fifo.add(point);
        }
        else if (keycode == Keys.NUM_6)
        {
        	point.r = (byte)53;
        	point.g = (byte)168;
        	point.b = (byte)192;
    		point.type = (byte)0;
    		fifo.add(point);
        }
        else if (keycode == Keys.NUM_7)
        {
        	point.r = (byte)153;
        	point.g = (byte)201;
        	point.b = (byte)83;
    		point.type = (byte)0;
    		fifo.add(point);
        }
        else if (keycode == Keys.NUM_8)
        {
        	point.r = (byte)131;
        	point.g = (byte)144;
        	point.b = (byte)152;
    		point.type = (byte)0;
    		fifo.add(point);
        }
        else if (keycode == Keys.NUM_9)
        {
        	point.r = (byte)86;
        	point.g = (byte)132;
        	point.b = (byte)227;
    		point.type = (byte)0;
    		fifo.add(point);
        }
       
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
    	
    	return false;
    }

    @Override
    public boolean keyTyped (char character) {
    	if(character == '+' || character == '=') {
    		point.brush_size++;
    		point.type = (byte)0;
    		if(point.brush_size>30){
    			point.brush_size=30;
    		}
    		fifo.add(point);
    	}
    	else if (character == '-') {
    		point.brush_size--;
    		point.type = (byte)0;
    		if(point.brush_size<1){
    			point.brush_size=1;
    		}
    		fifo.add(point);
    	}
    	
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
    		point.x = x;
    		point.y = y;
    		point.type = (byte)5;
    		fifo.add(point);
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
    	if(x!=point.x||y!=point.y||point.type!=(byte)2)
    	{
    		point.x = x;
			point.y = y;
			point.type = (byte)2;
			fifo.add(point);
    	}
    	return false;
    }

    @Override
    public boolean scrolled (int amount) {
        return false;
    }

	@Override
	public boolean mouseMoved(int x, int y) {	

		return false;
	}
}
