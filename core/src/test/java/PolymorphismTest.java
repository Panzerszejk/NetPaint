package com.mygdx.core;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PolymorphismTest {

    @Test
    public void isPoint() {
        Shape bob = new Point(0, 0, (byte)8, (byte)0,(byte)0,(byte)0,(byte)0,(byte)0);


    	assertTrue("failure - should be true", bob instanceof Point);
    }
}

