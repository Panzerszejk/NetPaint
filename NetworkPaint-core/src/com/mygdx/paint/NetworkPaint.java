package com.mygdx.paint;

import java.nio.ByteBuffer;

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
	public String ServerClientIP;
	ClientThread client=new ClientThread();
	ServerThread server=new ServerThread();  
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
	
	public void sendData(){
		if(current!=null){
		byte[] bytearray= new byte[13];
		byte[] bytesX = ByteBuffer.allocate(4).putInt(current.x).array();
		byte[] bytesY = ByteBuffer.allocate(4).putInt(current.y).array();
		for(int i=0;i<4;i++){		//X to bytes
			bytearray[i]=bytesX[i];
		}
		for(int i=4;i<8;i++){		//Y to bytes
			int j=0;
			bytearray[i]=bytesY[j];
			j++;
		}
		bytearray[8]=current.brush_size;
		bytearray[9]=current.type;
		bytearray[10]=current.r;
		bytearray[11]=current.g;
		bytearray[12]=current.b;
		if(ClientServerSelect=="C"){
			System.arraycopy( bytearray, 0, server.sendMsg, 0, bytearray.length );
		}
		if(ClientServerSelect=="S"){
			System.arraycopy( bytearray, 0, client.sendMsg, 0, bytearray.length );
		}
		}
	}
	
	public void receiveData(){
		byte[] byteX=new byte[4];
		byte[] byteY=new byte[4];
		if(ClientServerSelect=="C"&&server.receiveMsg!=null){
			System.arraycopy(server.receiveMsg, 0, byteX, 0, 4);
			System.arraycopy(server.receiveMsg, 4, byteY, 4, 4);
			int x=ByteBuffer.wrap(byteX).getInt();
			int y=ByteBuffer.wrap(byteY).getInt();
			byte s=server.receiveMsg[8];
			byte t=server.receiveMsg[9];
			byte r=server.receiveMsg[10];
			byte g=server.receiveMsg[11];
			byte b=server.receiveMsg[12];
			Point punkt=new Point(x, y, s, t, r, g, b);
			inputProcesor.addFifo(punkt);
		}
		if(ClientServerSelect=="S"&&client.receiveMsg!=null){
			System.arraycopy(client.receiveMsg, 0, byteX, 0, 4);
			System.arraycopy(client.receiveMsg, 4, byteY, 4, 4);
			int x=ByteBuffer.wrap(byteX).getInt();
			int y=ByteBuffer.wrap(byteY).getInt();
			byte s=client.receiveMsg[8];
			byte t=client.receiveMsg[9];
			byte r=client.receiveMsg[10];
			byte g=client.receiveMsg[11];
			byte b=client.receiveMsg[12];
			Point punkt=new Point(x, y, s, t, r, g, b);
			inputProcesor.addFifo(punkt);
		}
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
		//zmienic na true, przy last wersji!!!
		

		shapeRenderer.setProjectionMatrix(camera.combined);
		batch = new SpriteBatch();
		
		current = null;
		previous = null;
		//obliczanie najblizszej wiekszej potegi 2. Kod ze stacka :D
		
		inputProcesor = new MyInputProcesor();	//utworzenie procesora obslugi wejsc
		Gdx.input.setInputProcessor(inputProcesor);	//ustawienie procesora wejsc na ten z MyInputProcessor
		set_kursor(inputProcesor.get_brush_size()); //wywolanie funkcji obslugujacej zmiane kursora
		
		texture=ScreenUtils.getFrameBufferTexture(); //sciagam teksture na wstepie zeby nie wywalilo nam NullPointerException przy pierwszym rysowaniu
		
		if(ClientServerSelect=="C"){  //wybor klient/serwer
			client.IPv4=ServerClientIP;
			client.start();
		}
		else if(ClientServerSelect=="S"){
			server.IPv4=ServerClientIP;
			server.start();
		}
	}

	@Override
	public void render () {

		current=inputProcesor.pollFifo();  //Metoda zdejmuje ostatni element z listy fifo. Jesli elementow nie ma zwraca null
		if(ClientServerSelect=="S") sendData();
		if(ClientServerSelect=="C") receiveData();
		//receiveData();
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
					shapeRenderer.setColor((current.r & 0xff)/255f, (current.g & 0xff)/255f, (current.b & 0xff)/255f, 1f);
					
					shapeRenderer.circle(x1,y1,current.brush_size/2);
					shapeRenderer.rectLine(x1,y1,x2,y2,current.brush_size); //Rysujemy "zaokralona" linie
					shapeRenderer.circle(x2,y2,current.brush_size/2);
					shapeRenderer.end();
				}
				if(previous.type == 1){ //Jesli przycisk myszy klikniety
					camera.update();
					shapeRenderer.begin(ShapeType.Filled);
					shapeRenderer.setColor((current.r & 0xff)/255f, (current.g & 0xff)/255f, (current.b & 0xff)/255f, 1f);
					shapeRenderer.circle(current.x,height-current.y,current.brush_size/2); //Rysuj pojedynczy punkt
					shapeRenderer.end();
				}
			}
			else{
				previous = new Point(current); //Jesli LastTab jest null, to stworz nowy
			
		
			}
			previous.copy(current);  //Przepisuje punkt za kazdym razem, zeby uniknac problemow z nullem
		}
		texture=ScreenUtils.getFrameBufferTexture(); //Pobieramy bufor ekranu do tekstury
	}


	@Override
	public void dispose () {
		batch.dispose();


	}
}

