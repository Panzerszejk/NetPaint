package com.mygdx.paint;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	
	@Override
	public void create () {
		batch = new SpriteBatch();		
		
		pixmap = new Pixmap(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        //Fill it red
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        
        //Draw two lines forming an X
        pixmap.setColor(Color.BLACK);
        //pixmap.drawLine(0, 0, pixmap.getWidth()-1, pixmap.getHeight()-1);
        //pixmap.drawLine(0, pixmap.getHeight()-1, pixmap.getWidth()-1, 0);
        
        //pixmap.setColor(Color.YELLOW);
        //pixmap.drawCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 1);
        
        
        texture = new Texture(pixmap);
        
        //pixmap.dispose();
        
        sprite = new Sprite(texture);
        sprite.setPosition(0,0);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        /*if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                sprite.translateX(-1f);
            else
                sprite.translateX(-10.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                sprite.translateX(1f);
            else
                sprite.translateX(10.0f);
        }*/
        
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            //sprite.setPosition(Gdx.input.getX() - sprite.getWidth()/2,
                   // Gdx.graphics.getHeight() - Gdx.input.getY() - sprite.getHeight()/2);
        	pixmap.drawPixel(Gdx.input.getX(), Gdx.input.getY());
        	pixmap.drawCircle(Gdx.input.getX(), Gdx.input.getY(),1);
        	texture.dispose();
            texture = new Texture(pixmap);
            sprite.setTexture(texture);
        	
        }
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
        	pixmap.drawPixel(Gdx.input.getX(), Gdx.input.getY());
        	pixmap.drawCircle(Gdx.input.getX(), Gdx.input.getY(),1);
        	//texture.dispose();
            texture.draw(pixmap, 0, 0);
            sprite.setTexture(texture);
        }
        
        batch.begin();
        sprite.draw(batch);
        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
	}
}
