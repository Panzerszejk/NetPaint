package com.mygdx.core;

import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.junit.Before;

public class ConstructionGetterTest {
	MyInputProcesor inputprocesor;
	@Before
	public void initialisation(){
		inputprocesor=new MyInputProcesor();
	}
	@Test
	public void isBrushSizeValid() {
		inputprocesor.set_brush_size((byte)8);
		assertSame("should be same", inputprocesor.get_brush_size(), (byte)8);
	}
	@Test
	public void isColorValid() {
		inputprocesor.set_kolor((byte) 100,(byte) 110,(byte) 120);
		assertSame("should be same", inputprocesor.get_r(), (byte)100);
		assertSame("should be same", inputprocesor.get_g(), (byte)110);
		assertSame("should be same", inputprocesor.get_b(), (byte)120);
	}
	
}

