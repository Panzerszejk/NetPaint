package com.mygdx.paint;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class NetworkPaint extends ApplicationAdapter implements ApplicationListener {
	public OrthographicCamera camera;
	ShapeRenderer shapeRenderer;
	public int width;
	public int height;
	int brushSize;
	private MyInputProcesor inputProcesor;	//inputProcesor globalny na cala klase, zeby uzywac
											//jego pol (isPressed) do decydowania o rysowaniu
	@Override
	public void create () {
        inputProcesor = new MyInputProcesor();	//utworzenie procesora obslugi wejsc
        Gdx.input.setInputProcessor(inputProcesor);	//ustawienie procesora wejsc naszego gdx
		int size = 10000;
		inputProcesor.Xbuffer = new int[size];
		inputProcesor.Ybuffer = new int[size];
		inputProcesor.Sbuffer = new boolean[size];
		inputProcesor.bufferPointer = 0;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		camera.update();
		shapeRenderer = new ShapeRenderer();
		Gdx.graphics.setContinuousRendering(false);
		brushSize = 3;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        int x1, x2, y1, y2;
	    shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(290/255f, 190/255f,190/255f, 1);
        for(int i = 0; i < inputProcesor.bufferPointer; i++){
        	 if(inputProcesor.Sbuffer[i]) {
        		 x1 = inputProcesor.Xbuffer[i];
        		 x2 = inputProcesor.Xbuffer[i+1];
        		 y1 = height - inputProcesor.Ybuffer[i];
        		 y2 = height - inputProcesor.Ybuffer[i+1];
        	     shapeRenderer.circle(x1,y1,brushSize/2);
        	     shapeRenderer.rectLine(x1,y1,x2,y2,brushSize);
        	 }
        }
        shapeRenderer.end();

        

        
	}
	
	
	@Override
	public void dispose () {
	}
}


