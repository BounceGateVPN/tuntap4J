package com.github.smallru8.driver.tuntap.Test;

import com.github.smallru8.driver.tuntap.TapDevice;
import com.github.smallru8.driver.tuntap.TunTap;

public class Test1 {

	public static void main( String[] args ) throws InterruptedException {
		TunTap tt = new TunTap();
		tt.tuntap_up();
		while(true) {
			byte[] buff = tt.tuntap_read(1560);
			if(buff!=null)
				System.out.println(buff.length);
			//Thread.sleep(100);
		}
	}
	
}
