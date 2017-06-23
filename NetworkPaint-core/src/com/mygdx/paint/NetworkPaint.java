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
	ClientThread client=new ClientThread();
	ServerThread server=new ServerThread(); 
	Point temp;
	Point current0;  //tablica pozycji obecnej
	Point previous0;	//tablica pozycji poprzedniej
	Point current1;
	Point previous1;
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
		current0 = null;
		previous0 = null;
		current1 = null;
		previous1 = null;
		
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
			current0 = temp;
			current1 = null;
			break;
		}
		case (byte)1:{
			current1 = temp;
			current0 = null;
			break;
		}
		default:{
			current0 = null;
			current1 = null;
		}
		}
		}
		if(ClientServerSelect.equals("online")) {
			if(current0!=null) {
				server.punktsend.copy(current0);
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
		if(current0 != null){ //Musimy sprawdzic czy przypadkiem PosTab nie jest pusty(null) bo inaczej wywali nam NullPointerException
			set_kursor(current0.brush_size,current0.r,current0.g,current0.b);
			if(previous0 != null){ //Sprawdzenie, czy nie jest to pierwszy punkt
				if(current0.type == 2&&previous0.type!=0 ){ //Jesli mysz jest przeciagana
					camera.update();
					int x1, x2, y1, y2;
					x1 = previous0.x;
					x2 = current0.x;
					y1 = height - previous0.y;
					y2 = height - current0.y;
					shapeRenderer.begin(ShapeType.Filled);
					shapeRenderer.setColor((current0.r & 0xff)/255f, (current0.g & 0xff)/255f, (current0.b & 0xff)/255f, 1f);
					
					shapeRenderer.circle(x1,y1,current0.brush_size/2);
					shapeRenderer.rectLine(x1,y1,x2,y2,current0.brush_size); //Rysujemy "zaokralona" linie
					shapeRenderer.circle(x2,y2,current0.brush_size/2);
					shapeRenderer.end();
				}
				if(previous0.type == 1){ //Jesli przycisk myszy klikniety
					camera.update();
					shapeRenderer.begin(ShapeType.Filled);
					shapeRenderer.setColor((current0.r & 0xff)/255f, (current0.g & 0xff)/255f, (current0.b & 0xff)/255f, 1f);
					shapeRenderer.circle(current0.x,height-current0.y,current0.brush_size/2); //Rysuj pojedynczy punkt
					shapeRenderer.end();
				}
			}
			else{
				previous0 = new Point(current0); //Jesli LastTab jest null, to stworz nowy
			
		
			}
			previous0.copy(current0);  //Przepisuje punkt za kazdym razem, zeby uniknac problemow z nullem
		}
		
		if(current1 != null){ //Musimy sprawdzic czy przypadkiem PosTab nie jest pusty(null) bo inaczej wywali nam NullPointerException
			if(previous1 != null){ //Sprawdzenie, czy nie jest to pierwszy punkt
				if(current1.type == 2&&previous1.type!=0 ){ //Jesli mysz jest przeciagana
					camera.update();
					int x1, x2, y1, y2;
					x1 = previous1.x;
					x2 = current1.x;
					y1 = height - previous1.y;
					y2 = height - current1.y;
					shapeRenderer.begin(ShapeType.Filled);
					shapeRenderer.setColor((current1.r & 0xff)/255f, (current1.g & 0xff)/255f, (current1.b & 0xff)/255f, 1f);
					
					shapeRenderer.circle(x1,y1,current1.brush_size/2);
					shapeRenderer.rectLine(x1,y1,x2,y2,current1.brush_size); //Rysujemy "zaokralona" linie
					shapeRenderer.circle(x2,y2,current1.brush_size/2);
					shapeRenderer.end();
				}
				if(previous1.type == 1){ //Jesli przycisk myszy klikniety
					camera.update();
					shapeRenderer.begin(ShapeType.Filled);
					shapeRenderer.setColor((current1.r & 0xff)/255f, (current1.g & 0xff)/255f, (current1.b & 0xff)/255f, 1f);
					shapeRenderer.circle(current1.x,height-current1.y,current1.brush_size/2); //Rysuj pojedynczy punkt
					shapeRenderer.end();
				}
			}
			else{
				previous1 = new Point(current1); //Jesli LastTab jest null, to stworz nowy
			
		
			}
			previous1.copy(current1);  //Przepisuje punkt za kazdym razem, zeby uniknac problemow z nullem
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

