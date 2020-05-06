package com.github.smallru8.driver.tuntap;

public class TapDevice {

	public TunTap tap;
	
	public TapDevice() {
		tap = new TunTap();
	}
	
	public void startEthernetDev() {
		tap.tuntap_up();
	}
	
	/**
	 * read packet 
	 * @param len
	 * @return
	 */
	public byte[] read(int len) {//len = 0為 自動調整大小
		return tap.tuntap_read(len);
	}
	
	/**
	 * return 送出的byte數
	 * @param data
	 * @return
	 */
	public int write(byte[] data) {
		return tap.tuntap_write(data, data.length);
			
	}
}
