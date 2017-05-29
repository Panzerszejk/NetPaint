package com.mygdx.paint;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input; // wywalało warning, że niepotrzebne 
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
	private MyInputProcesor inputProcesor;	//inputProcesor globalny na całą klasę, żeby używać
											//jego pól (isPressed) do decydowania o rysowaniu
	private int previousPointX;	//pole do przechowywania poprzedniej pozycji
	private int previousPointY;	//wciśniętego kursora, żeby rysować linię od
								//tejże pozycji, do aktualnej pozycji
	@Override
	public void create () {
		batch = new SpriteBatch();		
		
		pixmap = new Pixmap(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);	//ustawienie aktualnego koloru na biały
        pixmap.fill();					//wypełnienie okna aktualnym kolorem
        
        pixmap.setColor(Color.BLACK);	//ustawienie aktualnego koloru na czarny
        
        inputProcesor = new MyInputProcesor();	//utworzenie procesora obsługi wejść
        Gdx.input.setInputProcessor(inputProcesor);	//ustawienie procesora wejść naszego gdx
        											//na ten wlaśnie utworzony
        texture = new Texture(pixmap);
                
        sprite = new Sprite(texture);
        sprite.setPosition(0,0);
        previousPointX = -1;	//nie ma aktualnie "poprzedniej" wartości
        previousPointY = -1;	//więc w w celu uniknięcia lub wykrycia błędów
        						//ustawiam niemożliwe wartości
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(inputProcesor.isPressed){	//jeśli wciśnięty przycisk myszy
        	if(previousPointX != -1 && previousPointY != -1) //oraz istnieje jakiś poprzedni punkt
        		//rysujemy linię z poprzedniej pozycji do aktualnej
        		pixmap.drawLine(previousPointX, previousPointY, inputProcesor.pointX, inputProcesor.pointY);
        	previousPointX = inputProcesor.pointX;	//ustawiamy aktualną pozycję jako poprzednią
        	previousPointY = inputProcesor.pointY;
        }
        else{
            previousPointX = -1;	//jeśli przycisk nie wciśnięty
            previousPointY = -1;	//to "zerujemy" poprzednią pozycję
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


