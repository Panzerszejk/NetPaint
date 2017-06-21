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

	Point current;  //tablica pozycji obecnej
	Point previous;	//tablica pozycji poprzedniej
	
	public void set_kursor(byte rozmiar)
	{
		byte brushSizePow2=rozmiar;
		brushSizePow2--;
		brushSizePow2 |= brushSizePow2 >> 1;
		brushSizePow2 |= brushSizePow2 >> 2;
		brushSizePow2 |= brushSizePow2 >> 4; //zaokraglenie dziala do 4 bitow + 1. Jak bedziemy robic wieksza wielkosc pedzla niz 32 to trzeba bedzie dodac jeszcze jedna linijke
		brushSizePow2++;

		Pixmap pm = new Pixmap(brushSizePow2,brushSizePow2,Pixmap.Format.RGBA8888); //rozmiar kursora musi byc potega 2
		pm.setColor(Color.BLACK);
		pm.drawCircle(rozmiar/2, rozmiar/2, rozmiar/2);
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, rozmiar/2, rozmiar/2));
		pm.dispose();
	}
	
	@Override
	public void create () {
		
		
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
		
		current = null;
		previous = null;
		//obliczanie najblizszej wiekszej potegi 2. Kod ze stacka :D
	
		set_kursor(brushSize); //wywolanie funkcji obslugujacej zmiane kursora
		
		inputProcesor = new MyInputProcesor();	//utworzenie procesora obslugi wejsc
		inputProcesor.set_brush_size(brushSize);
		inputProcesor.set_kolor((byte)255, (byte)255, (byte)255);
		Gdx.input.setInputProcessor(inputProcesor);	//ustawienie procesora wejsc na ten z MyInputProcessor
		

		
	texture=ScreenUtils.getFrameBufferTexture(); //sciagam teksture na wstepie zeby nie wywalilo nam NullPointerException przy pierwszym rysowaniu
		
		//ServerThread server=new ServerThread();  //uruchamianie serwera i klienta
		//ClientThread client=new ClientThread();
		//server.start();
		//client.start();

	}

	@Override
	public void render () {

		current=inputProcesor.pollFifo();  //Metoda zdejmuje ostatni element z listy fifo. Jesli elementow nie ma zwraca null

		
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

		if(current != null){ //Musimy sprawdzic czy przypadkiem PosTab nie jest pusty(null) bo inaczej wywali nam NullPointerException
			if(previous != null){ //Sprawdzenie, czy nie jest to pierwszy punkt
				if(current.type == 2){ //Jesli mysz jest przeciagana
					camera.update();
					int x1, x2, y1, y2;
					x1 = previous.x;
					x2 = current.x;
					y1 = height - previous.y;
					y2 = height - current.y;
					shapeRenderer.begin(ShapeType.Filled);
					shapeRenderer.setColor(0.5f, 0.75f, 0.75f, 1f);
					//System.out.println("("+x1+" "+y1+") , ("+x2+" "+y2+")");
					shapeRenderer.circle(x1,y1,brushSize/2);
					shapeRenderer.rectLine(x1,y1,x2,y2,brushSize); //Rysujemy "zaokralona" linie
					shapeRenderer.circle(x2,y2,brushSize/2);
					shapeRenderer.end();
				}
				if(previous.type == 1){ //Jesli przycisk myszy klikniety
					camera.update();
					shapeRenderer.begin(ShapeType.Filled);
					shapeRenderer.setColor(0.5f, 0.75f, 0.75f, 1f);
					shapeRenderer.circle(current.x,height-current.y,brushSize/2); //Rysuj pojedynczy punkt
					shapeRenderer.end();
				}
			}
			else{
				//previous = new Point; //Jesli LastTab jest null, to stworz nowy
			
		
			}
			previous = current;  //Przepisuje punkt za kazdym razem, zeby uniknac problemow z nullem
		}
		texture=ScreenUtils.getFrameBufferTexture(); //Pobieramy bufor ekranu do tekstury
	}


	@Override
	public void dispose () {
		batch.dispose();


	}
}

