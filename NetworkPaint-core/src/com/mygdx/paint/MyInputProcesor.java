package com.mygdx.paint;

import com.badlogic.gdx.InputProcessor;

public class MyInputProcesor implements InputProcessor {
	public int[] Xbuffer;
	public int[] Ybuffer;
	public boolean[] Sbuffer;
	public int bufferPointer;
	
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
    	bufferPointer++;
    	Sbuffer[bufferPointer] = false;
    	Xbuffer[bufferPointer] = x;
    	Ybuffer[bufferPointer] = y;
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
    	bufferPointer++;
    	Sbuffer[bufferPointer] = false;
    	Xbuffer[bufferPointer] = x;
    	Ybuffer[bufferPointer] = y;
        return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
    	bufferPointer++;
    	Sbuffer[bufferPointer] = true;
    	Xbuffer[bufferPointer] = x;
    	Ybuffer[bufferPointer] = y;
        return false;
    }

    @Override
    public boolean scrolled (int amount) {
        return false;
    }

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}