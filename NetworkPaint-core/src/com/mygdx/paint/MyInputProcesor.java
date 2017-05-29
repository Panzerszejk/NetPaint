package com.mygdx.paint;

//import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcesor implements InputProcessor {
	boolean isPressed;	//bool do trzymania stanu przycisku myszy
	int pointX;	//aktualna pozycja wcisniętego kursora
	int pointY;
	//prawdopodobnie to wszystko powinno byc private, bo obiektowosc
	//a dostep przez metode np. getPointX, getIsPressed
	//jak ktos chce, moze przerobic :D
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
    	isPressed = true;	//ustawienie, ze przycisk myszy wcisniety
    	pointX = x;	//ustawienie poczatkowej pozycji kursora
    	pointY = y;
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
    	isPressed = false;	//ustawienie, że przycisk myszy nie wcisnięty
    	pointX = -1;	//ustawienie niemozliwej pozycji
    	pointY = -1;	//w celu wykrycia / unikniecia bledow
        return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
    	pointX = x;	//ustawienie aktualnej pozycji przy przeciaganiu mysza
    	pointY = y;
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