package com.mygdx.paint;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class NetworkPaint extends ApplicationAdapter {
	public OrthographicCamera camera;
	ShapeRenderer shapeRenderer;
	public int width;
	public int height;
	int brushSize;
	private MyInputProcesor inputProcesor;	//inputProcesor globalny na cala klase, zeby uzywac
											//jego pol (isPressed) do decydowania o rysowaniu
	
	Integer[] PosTab;  //tablica pozycji obecnej
	Integer[] LastTab;	//tablica pozycji poprzedniej
	
	@Override
	public void create () {
        inputProcesor = new MyInputProcesor();	//utworzenie procesora obslugi wejsc
        Gdx.input.setInputProcessor(inputProcesor);	//ustawienie procesora wejsc naszego gdx
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		camera.update();
		shapeRenderer = new ShapeRenderer();
		Gdx.graphics.setContinuousRendering(false);
		brushSize = 10;
		
		
		PosTab= new Integer[3];
        LastTab= new Integer[2];
        		
        PosTab=null;
        LastTab=null;
	}

	@Override
	public void render () {
		PosTab=inputProcesor.pollFifo();  //Metoda zdejmuje ostatni element z listy fifo. Jesli elementow nie ma zwraca null
		
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(PosTab!=null){ 		//Musimy sprawdzic czy przypadkiem PosTab nie jest pusty(null) bo inaczej wywali nam NullPointerException
	        if(PosTab[2]==1) {
	        	if(LastTab==null) {
	        		LastTab= new Integer[2];
	            	LastTab[0]=PosTab[0];
	        		LastTab[1]=PosTab[1];
	            }
	        	else {
	                camera.update();
	                int x1, x2, y1, y2;
	        	    shapeRenderer.begin(ShapeType.Filled);
	                shapeRenderer.setColor(290/255f, 190/255f,190/255f, 1);
	        		
	        		
		        	x1 = LastTab[0];
		      		x2 = PosTab[0];
		      		y1 = height - LastTab[1];
		      		y2 = height - PosTab[1];
		      		
		      		shapeRenderer.circle(x1,y1,brushSize/2);
		      		shapeRenderer.rectLine(x1,y1,x2,y2,brushSize);
		      		
		      		LastTab[0]=PosTab[0];
	        		LastTab[1]=PosTab[1];
	        		
	        		shapeRenderer.end();
	        	}
	        }
	        else {
	        	LastTab=null; //ciekawe ale jak przypiszemy null to nie musimy usowac obiektu. Robi to za nas garbage colector po jakims czasie
	        }
        }

    
	}
	
	
	@Override
	public void dispose () {
	}
}
