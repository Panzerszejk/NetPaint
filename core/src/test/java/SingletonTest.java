package com.mygdx.core;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SingletonTest {
	
    @Test
    public void isSingleton() {
    	ServerThread server1=ServerThread.get(); 
    	ServerThread server2=ServerThread.get(); 

    	assertTrue("failure - should be true", server1==server2);
    }
}

