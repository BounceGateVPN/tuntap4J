package com.github.smallru8.driver.tuntap;

import com.github.smallru8.util.abstracts.Port;

/**
 * This class is for BGV project
 * @author smallru8
 *
 */
public class TapDevice extends Thread{

	public TunTap tap;
	public Port sport;//存vSwitch port
	public boolean runFlag = true;
	
	public TapDevice() {
		tap = new TunTap();
	}
	
	/**
	 * Start tap device and set port(linked to switch)
	 * @param sport
	 */
	public void startEthernetDev(Port sport) {
		tap.tuntap_up();
		this.sport = sport;
	}
	
	/**
	 * read packet 
	 * @param len
	 * @return
	 */
	public byte[] read(int len) {//len = 0為 自動調整大小
		if(len==0)
			len = 1560;
		return tap.tuntap_read(len);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public int write(byte[] data) {
		return tap.tuntap_write(data, data.length);
	}
	
	public void close() {
		runFlag = false;
		tap.tuntap_down();
		tap.tuntap_destroy();
	}
	
	/**
	 * auto send packet to vSwitch
	 */
	@Override
	public void run() {
		while(runFlag) {
			byte[] buffer = tap.tuntap_read(1560);
			if(buffer!=null) {
				sport.sendToVirtualDevice(buffer);
			}
		}
	}
}
