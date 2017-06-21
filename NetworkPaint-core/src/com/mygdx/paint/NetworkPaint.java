package com.mygdx.paint;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Pixmap;
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
	private MyInputProcesor inputProcesor;
	private SpriteBatch batch;
	private TextureRegion texture;
	private Sprite sprite;

	Integer[] PosTab;  //tablica pozycji obecnej
	Integer[] LastTab;	//tablica pozycji poprzedniej

	@Override
	public void create () {
		inputProcesor = new MyInputProcesor();	//utworzenie procesora obslugi wejsc
		Gdx.input.setInputProcessor(inputProcesor);	//ustawienie procesora wejsc na ten z MyInputProcessor
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		camera.update();
		shapeRenderer = new ShapeRenderer();

		Gdx.graphics.setContinuousRendering(false); //wylacza ciagle renderowanie. Renderuje gdy pojawi sie jakis event
		brushSize = 20;

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
		
		ServerThread server=new ServerThread();  //uruchamianie serwera i klienta
		ClientThread client=new ClientThread();
		server.start();
		client.start();
	}

	@Override
	public void render () {

		PosTab=inputProcesor.pollFifo();  //Metoda zdejmuje ostatni element z listy fifo. Jesli elementow nie ma zwraca null

		
		//Czyszczenie ekranu
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


		//Rysujemy w kazdej iteracji to co mamy w teksturze
		sprite = new Sprite(texture);
		batch.begin();
		batch.setBlendFunction(GL20.GL_ONE,GL20.GL_ONE_MINUS_SRC_ALPHA); //Zmiana metod laczenia obrazu, zeby nie przyciemnialo kolorow
		//batch.setColor(190/255f, 190/255f, 190/255f, 0f);
		sprite.draw(batch);
		batch.end();

		if(PosTab != null){ //Musimy sprawdzic czy przypadkiem PosTab nie jest pusty(null) bo inaczej wywali nam NullPointerException
			if(LastTab != null){ //Sprawdzenie, czy nie jest to pierwszy punkt
				if(PosTab[2] == 2){ //Jesli mysz jest przeciagana
					camera.update();
					int x1, x2, y1, y2;
					x1 = LastTab[0];
					x2 = PosTab[0];
					y1 = height - LastTab[1];
					y2 = height - PosTab[1];
					shapeRenderer.begin(ShapeType.Filled);
					shapeRenderer.setColor(0.5f, 0.75f, 0.75f, 1f);
					//System.out.println("("+x1+" "+y1+") , ("+x2+" "+y2+")");
					shapeRenderer.circle(x1,y1,brushSize/2);
					shapeRenderer.rectLine(x1,y1,x2,y2,brushSize); //Rysujemy "zaokralona" linie
					shapeRenderer.circle(x2,y2,brushSize/2);
					shapeRenderer.end();
				}
			}
			else{
				LastTab = new Integer[3]; //Jesli LastTab jest null, to stworz nowy
			}
			if(PosTab[2] == 1){ //Jesli przycisk myszy klikniety
				camera.update();
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(0.5f, 0.75f, 0.75f, 1f);
				shapeRenderer.circle(PosTab[0],height-PosTab[1],brushSize/2); //Rysuj pojedynczy punkt
				shapeRenderer.end();
			}
			LastTab[0] = PosTab[0];
			LastTab[1] = PosTab[1]; //Przepisuje punkt za kazdym razem, zeby uniknac problemow z nullem
		}
		texture=ScreenUtils.getFrameBufferTexture(); //Pobieramy bufor ekranu do tekstury
	}


	@Override
	public void dispose () {
		batch.dispose();


	}
}

