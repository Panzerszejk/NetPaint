package com.mygdx.paint;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;

public class MyInputProcesor implements InputProcessor {
    private Queue<Integer[]> fifo = new LinkedList<Integer[]>();
    Integer[] tab=new Integer[3];
    
    public Integer[] pollFifo () {
    	Integer[] temp=new Integer[3];
    	temp=fifo.poll();
        return temp;
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
    		tab[0]=x;
        	tab[1]=y;
        	tab[2]=1;
        	fifo.add(tab);
    	}
    	
    	if(button == Buttons.RIGHT) {
    		
    	}
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
    		tab[0]=x;
    		tab[1]=y;
    		tab[2]=0;
    		fifo.add(tab);
        return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
    	tab[0]=x;
    	tab[1]=y;
    	tab[2]=2;
    	fifo.add(tab);
    	
    	return false;
    }

    @Override
    public boolean scrolled (int amount) {
        return false;
    }

	@Override
	public boolean mouseMoved(int arg0, int arg1) {	
		tab[0]=arg0;
    	tab[1]=arg1;
    	tab[2]=3;
    	fifo.add(tab);
		
		return false;
	}
}
