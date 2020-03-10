package com.github.smallru8.driver.tuntap.TEST;

import java.io.IOException;

import com.github.smallru8.driver.tuntap.TapDevice;

/*
 * ARP TEST
 * */

public class TEST2 {
	public static void main( String[] args ) throws InterruptedException, IOException
    {
		TapDevice td = new TapDevice();
		td.startEthernetDev();
		while(true) {
			byte[] buffer = td.read(512);
			if(buffer==null) {
				continue;
			}
			if(buffer.length==0) {
        		Thread.sleep(1000);
        		continue;
        	}
			System.out.println("Recv : " + buffer.length);
		}
    }
}
