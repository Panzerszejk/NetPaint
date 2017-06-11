package com.mygdx.paint;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;

public class NetworkPaint extends ApplicationAdapter implements ApplicationListener {
	public OrthographicCamera camera;
	ShapeRenderer shapeRenderer;
	
	public int width;
	public int height;
	int brushSize;
	private MyInputProcesor inputProcesor;	//inputProcesor globalny na cala klase, zeby uzywac
											//jego pol (isPressed) do decydowania o rysowaniu
	
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private Pixmap pixmap;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
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
		brushSize = 10;
	}

	@Override
	public void render () {
		
		pixmap = ScreenUtils.getFrameBufferPixmap(0,0,width,height);
		texture = new Texture(pixmap);
		
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        sprite = new Sprite(texture);
        batch.begin();
        sprite.draw(batch);
        batch.end();
        
        camera.update();
        int x1, x2, y1, y2;
	    shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(290/255f, 190/255f,190/255f, 1);
        int i =inputProcesor.bufferPointer - 1;
        if(inputProcesor.bufferPointer > 2){
        	 if(inputProcesor.Sbuffer[i]) {
        		 x1 = inputProcesor.Xbuffer[i];
        		 x2 = inputProcesor.Xbuffer[i+1];
        		 y1 = height - inputProcesor.Ybuffer[i];
        		 y2 = height - inputProcesor.Ybuffer[i+1];
        	     shapeRenderer.circle(x1,y1,brushSize/2);
        	     shapeRenderer.rectLine(x1,y1,x2,y2,brushSize);
        	     shapeRenderer.circle(x2,y2,brushSize/2);
        	 }
        }
        shapeRenderer.end();

        

        
	}
	
	
	@Override
	public void dispose () {
	}
}


