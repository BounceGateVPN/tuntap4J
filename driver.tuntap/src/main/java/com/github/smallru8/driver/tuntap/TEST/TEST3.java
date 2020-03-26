package com.github.smallru8.driver.tuntap.TEST;

import java.io.IOException;

import com.github.smallru8.driver.tuntap.Analysis;
import com.github.smallru8.driver.tuntap.TunTap;

//Windows test
public class TEST3 {
	public static void main( String[] args ) throws IOException, InterruptedException {
		TunTap tt = new TunTap();
		tt.tuntap_up();
		
		byte[] mac = tt.tuntap_get_hwaddr();
        System.out.println(Integer.toHexString(mac[0]&0xFF)+ "-"+Integer.toHexString(mac[1]& 0xFF)+ "-"+Integer.toHexString(mac[2]& 0xFF)+ "-"+Integer.toHexString(mac[3]& 0xFF)+ "-"+Integer.toHexString(mac[4]& 0xFF)+ "-"+Integer.toHexString(mac[5]& 0xFF));
        
        System.out.println("fd:"+tt.tuntap_get_fd());
        System.out.println("press any key to continue...");
		System.in.read();
		
		Thread t = new TEST3Thread(tt);
		t.start();
		
		System.out.println("press any key to continue...");
		System.in.read();
		while(true) {
			if(tt.readable()) {
				byte[] buffer = tt.tuntap_readWIN();
				System.out.println("Recv : " + buffer.length);
				Analysis a = new Analysis();
				a.setFramePacket(buffer);
				byte[] srcMAC = a.getFrameSrcMACAddr();
				a.setFrameSrcMACAddr(a.getFrameDesMACAddr());
				a.setFrameDesMACAddr(srcMAC);
				
				tt.tuntap_writeWIN(buffer);
			}else {
				Thread.sleep(1000);
        		continue;
			}
		}
	}
}
