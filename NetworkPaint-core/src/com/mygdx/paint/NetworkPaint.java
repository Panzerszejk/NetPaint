package com.mygdx.paint;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input; // wywalalo warning, ze niepotrzebne 
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NetworkPaint extends ApplicationAdapter implements ApplicationListener {
	SpriteBatch batch;
	private Pixmap pixmap;
	private Sprite sprite;
	Texture texture;
	private MyInputProcesor inputProcesor;	//inputProcesor globalny na cala klase, zeby uzywac
											//jego pol (isPressed) do decydowania o rysowaniu
	private int previousPointX;	//pole do przechowywania poprzedniej pozycji
	private int previousPointY;	//wcisnietego kursora, zeby rysowac linie od
								//tejze pozycji, do aktualnej pozycji
	@Override
	public void create () {
		batch = new SpriteBatch();		
		
		pixmap = new Pixmap(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);	//ustawienie aktualnego koloru na bialy
        pixmap.fill();					//wypelnienie okna aktualnym kolorem
        
        pixmap.setColor(Color.BLACK);	//ustawienie aktualnego koloru na czarny
        
        inputProcesor = new MyInputProcesor();	//utworzenie procesora obslugi wejsc
        Gdx.input.setInputProcessor(inputProcesor);	//ustawienie procesora wejsc naszego gdx
        											//na ten wlasnie utworzony
        texture = new Texture(pixmap);
                
        sprite = new Sprite(texture);
        sprite.setPosition(0,0);
        previousPointX = -1;	//nie ma aktualnie "poprzedniej" wartosci
        previousPointY = -1;	//wiec w w celu unikniecia lub wykrycia bledow
        						//ustawiam niemozliwe wartosci
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(inputProcesor.isPressed){	//jesli wcisniety przycisk myszy
        	if(previousPointX != -1 && previousPointY != -1){ //oraz istnieje jakis poprzedni punkt
        		//rysujemy linie z poprzedniej pozycji do aktualnej
        		pixmap.drawLine(previousPointX, previousPointY, inputProcesor.pointX, inputProcesor.pointY);
        		pixmap.drawLine(previousPointX, previousPointY, inputProcesor.pointX, inputProcesor.pointY);
        	}
        	previousPointX = inputProcesor.pointX;	//ustawiamy aktualna pozycje jako poprzednia
        	previousPointY = inputProcesor.pointY;
        }
        else{
            previousPointX = -1;	//jesli przycisk nie wcisniety
            previousPointY = -1;	//to "zerujemy" poprzednia pozycje
        }
    	texture.dispose();
        texture = new Texture(pixmap);
        sprite.setTexture(texture);       
        batch.begin();
        sprite.draw(batch);
        batch.end();
	}
	
	public Pixmap getPixmap(){
		return pixmap;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
	}
}


