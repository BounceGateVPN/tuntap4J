package com.github.smallru8.driver.tuntap.TEST;

import com.github.smallru8.driver.tuntap.TunTap;

public class TEST3Thread extends Thread{
	
	TunTap tt_tmp;
	public TEST3Thread(TunTap tt){
		tt_tmp = tt;
	}
	public void run() {
		tt_tmp.tuntap_startReadWrite();
	}
	
}
