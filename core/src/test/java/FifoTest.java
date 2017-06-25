package com.mygdx.core;

import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertNull;
public class FifoTest{
	MyInputProcesor inputprocesor;
	Point punkt;
	@Before
	public void initialisation(){
		inputprocesor=new MyInputProcesor();
		punkt=new Point(0, 0, (byte)8, (byte)0,(byte)0,(byte)0,(byte)0,(byte)0);

	}
	@Test
	public void FifoPointValidation() {
		inputprocesor.addFifo(punkt);
		assertSame("should be same", punkt, inputprocesor.pollFifo());
	}
	@Test
	public void FifoNullValidation() {
		assertNull("should be null", inputprocesor.pollFifo());
	}
	
}