package com.github.smallru8.driver.tuntap.TEST;

import java.io.IOException;

import com.github.smallru8.driver.tuntap.TapDevice;

/*
 * ARP TEST
 * */

public class TEST2 {
	public static void main( String[] args ) throws InterruptedException, IOException
    {
		TapDevice td = new TapDevice();//建立tap跟ARP table
		td.startEthernetDev();//初始化
		while(true) {
			byte[] buffer = td.read(512);//讀

			if(buffer.length==0) {
        		Thread.sleep(1000);
        		continue;
        	}
			System.out.println("Recv : " + buffer.length);
		}
    }
}
