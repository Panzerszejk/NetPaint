package com.mygdx.paint;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcesor implements InputProcessor {
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
    	System.out.println("Wcisniety");
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
    	System.out.println("Upuszczony");
        return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
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