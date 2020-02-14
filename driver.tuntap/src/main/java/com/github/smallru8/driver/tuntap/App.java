package com.github.smallru8.driver.tuntap;

import java.io.IOException;

/**
 * TunTap Test
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
        TunTap tt = new TunTap();
        System.out.println(tt.tuntap_set_ip("192.168.87.2", 24));
        tt.tuntap_up();
        try {
        	System.out.println("fd:"+tt.tuntap_get_fd());
        	System.out.println("press any key to continue...");
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while(true) {
        	byte[] ip = new byte[4];
        	byte[] buffer = tt.tuntap_read(512);
        	if(buffer.length==0) {
        		Thread.sleep(1000);
        		continue;
        	}
        	System.out.println("Recv : " + buffer.length);
        	for(int i=12;i<16;i++)
        		ip[i-12] = buffer[i];
        	for(int i=16;i<20;i++)
        		buffer[i-4] = buffer[i];
        	for(int i=16;i<20;i++)
        		buffer[i] = ip[i-16];
        	buffer[20] = 0;
        	buffer[22]+=8;
        	//System.out.println("Send : " + tt.tuntap_write(buffer,buffer.length));
        }
    }
}
