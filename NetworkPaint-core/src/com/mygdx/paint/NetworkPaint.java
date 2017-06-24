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
	public MyInputProcesor inputProcesor;
	private SpriteBatch batch;
	private TextureRegion texture;
	private Sprite sprite;
	
	public String ClientServerSelect;
	public String ClientIP;
	public String ServerIP;
	
	ClientThread client=ClientThread.get();
	ServerThread server=ServerThread.get(); 
	Point temp;
	Point[] current = new Point[2];  //tablica pozycji obecnej
	Point[] previous = new Point[2];	//tablica pozycji poprzedniej
	Point received=new Point(0,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0);
	Point lastreceived=new Point(0,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0);
	public void set_kursor(byte rozmiar,byte r,byte g,byte b)
	{
		byte brushSizePow2=rozmiar;
		brushSizePow2--;
		brushSizePow2 |= brushSizePow2 >> 1;
		brushSizePow2 |= brushSizePow2 >> 2;
		brushSizePow2 |= brushSizePow2 >> 4; //zaokraglenie dziala do 4 bitow + 1. Jak bedziemy robic wieksza wielkosc pedzla niz 32 to trzeba bedzie dodac jeszcze jedna linijke
		brushSizePow2++;

		if(brushSizePow2<16) //rozmiar kursora w �indo�sie musi by� wiekszy od 16 
			brushSizePow2=16;
			
		Pixmap pm = new Pixmap(brushSizePow2,brushSizePow2,Pixmap.Format.RGBA8888); //rozmiar kursora musi byc potega 2
		pm.setColor((r & 0xff)/255f , (g & 0xff)/255f , (b & 0xff)/255f ,1f);
		pm.fillCircle(rozmiar/2, rozmiar/2,rozmiar/2);
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

		Gdx.graphics.setContinuousRendering(true); //wylacza ciagle renderowanie. Renderuje gdy pojawi sie jakis event
		//zmienic na true, przy last wersji!!!
		

		shapeRenderer.setProjectionMatrix(camera.combined);
		batch = new SpriteBatch();
		
		temp = null;
		current[0] = null;
		previous[0] = null;
		current[1] = null;
		previous[1] = null;
		
		//obliczanie najblizszej wiekszej potegi 2. Kod ze stacka :D
		
		inputProcesor = new MyInputProcesor();	//utworzenie procesora obslugi wejsc
		Gdx.input.setInputProcessor(inputProcesor);	//ustawienie procesora wejsc na ten z MyInputProcessor
		set_kursor(inputProcesor.get_brush_size(),inputProcesor.get_r(),inputProcesor.get_g(),inputProcesor.get_b()); //wywolanie funkcji obslugujacej zmiane kursora
		
		texture=ScreenUtils.getFrameBufferTexture(); //sciagam teksture na wstepie zeby nie wywalilo nam NullPointerException przy pierwszym rysowaniu
		if(ClientServerSelect.equals("online")){  //wybor klient/serwer
			client.IPv4=ClientIP;
			client.start();
			server.IPv4=ServerIP;
			server.start();

		}
	}

	@Override
	public void render () {

		temp=inputProcesor.pollFifo();  //Metoda zdejmuje ostatni element z listy fifo. Jesli elementow nie ma zwraca null
		if(temp != null){
			switch(temp.id){
			case (byte)0:{
				current[0] = temp;
				current[1] = null;
				break;
			}
			case (byte)1:{
				current[1] = temp;
				current[0] = null;
				break;
			}
			default:{
				current[0] = null;
				current[1] = null;
			}
			}
		}
		if(ClientServerSelect.equals("online")) {
			if(current[0]!=null) {
				server.punktsend.copy(current[0]);
			}
			received=client.fifoclient.poll();
			if(received!=null){
				inputProcesor.addFifo(received);
			}
		}
		
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
		texture.getTexture().dispose();
		sprite.getTexture().dispose();
		for(int i = 0; i < 2; i++){
			if(current[i] != null){ //Musimy sprawdzic czy przypadkiem PosTab nie jest pusty(null) bo inaczej wywali nam NullPointerException
				set_kursor(current[i].brush_size,current[i].r,current[i].g,current[i].b);
				if(previous[i] != null){ //Sprawdzenie, czy nie jest to pierwszy punkt
					if(current[i].type == 2&&previous[i].type!=0 ){ //Jesli mysz jest przeciagana
						camera.update();
						current[i].draw(shapeRenderer,previous[i].x,height-previous[i].y,height);
					}
					if(previous[i].type == 1){ //Jesli przycisk myszy klikniety
						camera.update();
						shapeRenderer.begin(ShapeType.Filled);
						shapeRenderer.setColor((current[i].r & 0xff)/255f, (current[i].g & 0xff)/255f, (current[i].b & 0xff)/255f, 1f);
						shapeRenderer.circle(current[i].x,height-current[i].y,current[i].brush_size/2); //Rysuj pojedynczy punkt
						shapeRenderer.end();
					}
					if(current[i].type==5){
						Shape bob = new Point(current[i].x, current[i].y, current[i].brush_size, current[i].type, current[i].r, current[i].g, current[i].b,current[i].id);
						bob.draw(shapeRenderer,previous[i].x,height-previous[i].y,height);
						Shape bob1 = new Rect(current[i].x, current[i].y, current[i].brush_size, current[i].type, current[i].r, current[i].g, current[i].b,current[i].id);
						bob1.draw(shapeRenderer, 20, 20, height);
					}
					if(!previous.equals(current)){
						previous[i].copy(current[i]);
					}
				}
				else{
					previous[i] = new Point(current[i]); //Jesli LastTab jest null, to stworz nowy
				}			
			}
		}
		texture=ScreenUtils.getFrameBufferTexture(); //Pobieramy bufor ekranu do tekstury
	}


	@Override
	public void dispose () {
		batch.dispose();
	}
	
	public void addShutdownHook(Thread hook){
		server.server.dispose();
		client.socket.dispose();
	}

}

