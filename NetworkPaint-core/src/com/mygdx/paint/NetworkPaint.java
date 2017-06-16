package com.mygdx.paint;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;


public class NetworkPaint extends ApplicationAdapter {
	public OrthographicCamera camera; 
	ShapeRenderer shapeRenderer;
	public int width;
	public int height;
	byte brushSize;
	private MyInputProcesor inputProcesor;	//inputProcesor globalny na cala klase, zeby uzywac
											//jego pol (isPressed) do decydowania o rysowaniu
	private SpriteBatch batch;
    private TextureRegion texture;
    private Sprite sprite;
    private Pixmap pixmap;

	Integer[] PosTab;  //tablica pozycji obecnej
	Integer[] LastTab;	//tablica pozycji poprzedniej
	
	FileHandle fh;
	int i=0;
	
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
		Gdx.graphics.setContinuousRendering(false); //wy³¹cza ci¹g³e renderowanie. Renderuje gdy pojawi siê jakiœ event
		brushSize = 10;
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		batch = new SpriteBatch();
        
		//obliczanie najblizszej wiekszej potegi 2. Kod ze stacka :D
		byte brushSizePow2=brushSize; 
		brushSizePow2--;
		brushSizePow2 |= brushSizePow2 >> 1;
		brushSizePow2 |= brushSizePow2 >> 2;
		brushSizePow2 |= brushSizePow2 >> 4; //zaokraglenie dziala do 4 bitow + 1. Jak bedziemy robic wieksza wielkosc pedzla niz 32 to trzeba bedzie dodac jeszcze jedna linijke  
		brushSizePow2++;  
		
		Pixmap pm = new Pixmap(brushSizePow2,brushSizePow2,Pixmap.Format.RGBA8888); //rozmiar kursora musi byc potega 2
		pm.setColor(Color.BLACK);
		pm.drawCircle(brushSize/2, brushSize/2, brushSize/2);
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, brushSize/2, brushSize/2));
		pm.dispose();
		 
		
		PosTab= new Integer[3];
        LastTab= new Integer[2];
        		
        PosTab=null;
        LastTab=null;
        
        texture=ScreenUtils.getFrameBufferTexture(); //sciagam teksture na wstepie zeby nie wywalilo nam NullPointerException przy pierwszym rysowaniu 

	}

	@Override
	public void render () {
	
		PosTab=inputProcesor.pollFifo();  //Metoda zdejmuje ostatni element z listy fifo. Jesli elementow nie ma zwraca null
	

		//Czyszczenie ekranu
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Rysujemy w ka¿dej iteracji to co mamy w teksturze
		sprite = new Sprite(texture); 
        batch.begin();
        //batch.setColor(190/255f, 190/255f, 190/255f, 0f);
        sprite.draw(batch);
        batch.end();
		

	        if(PosTab!=null){ 		//Musimy sprawdzic czy przypadkiem PosTab nie jest pusty(null) bo inaczej wywali nam NullPointerException
		        if(PosTab[2]==1) {  //jesli jest rowny 1 czyli przeciagniecie to bedziemy laczyc kolejne punkty
		        	if(LastTab==null) {  //jesli to pierwszy punkt to zapisujemy do talbicy ponktow poprzednich
		        		LastTab= new Integer[2];
		            	LastTab[0]=PosTab[0];
		        		LastTab[1]=PosTab[1];
		        		//dodane zeby klikniecie tez rysowa³o punkt ale bedzie to trzeba poprawic bo czasami laczy punkty ktorych nie ma laczyc 
		        		camera.update();  
		        	    shapeRenderer.begin(ShapeType.Filled);
		        	    shapeRenderer.setColor(190/255f, 190/255f,190/255f, 1);
		        		shapeRenderer.circle(PosTab[0],height-PosTab[1],brushSize/2);
		        		shapeRenderer.end();
		        		
		            }
		        	else {  //jesli to nie pierwszy punkt to wykonujemy takie operacje
		                camera.update();
		                int x1, x2, y1, y2;
		        	    shapeRenderer.begin(ShapeType.Filled);
		                shapeRenderer.setColor(190/255f, 190/255f,190/255f, 1);
		        		
		        		
			        	x1 = LastTab[0];
			      		x2 = PosTab[0];
			      		y1 = height - LastTab[1];
			      		y2 = height - PosTab[1];
			      		
			      		//System.out.println("("+x1+" "+y1+") , ("+x2+" "+y2+")");
			      		
			      		shapeRenderer.circle(x1,y1,brushSize/2);
			      		shapeRenderer.rectLine(x1,y1,x2,y2,brushSize);
			      		shapeRenderer.circle(x2,y2,brushSize/2);
			      		
			      		LastTab[0]=PosTab[0];
		        		LastTab[1]=PosTab[1];
		        		
		        		shapeRenderer.end();
		        	}
		        }
		        if(PosTab[2]==0) { 	//jesli to cos innego niz PosTab[2]==1 to znnaczy ze przycisk nie jest przycisniety
		        	LastTab=null; //ciekawe ale jak przypiszemy null to nie musimy usowac obiektu. Robi to za nas garbage colector po jakims czasie

		        }
	        }

	texture=ScreenUtils.getFrameBufferTexture(); //Pobieramy bufor ekranu do tekstury
	/*
	if(PosTab!=null){ 		//Musimy sprawdzic czy przypadkiem PosTab nie jest pusty(null) bo inaczej wywali nam NullPointerException
        if(PosTab[2]==2){
        	camera.update();   
    	    shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(0f, 0f,0f, 1);
      		shapeRenderer.circle(PosTab[0],height-PosTab[1],brushSize/2);
    		shapeRenderer.end();
        }
    }*/
	
	}
	
	
	@Override
	public void dispose () {
		 batch.dispose();
	    
	}
}

