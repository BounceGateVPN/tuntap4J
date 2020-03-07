package com.github.smallru8.driver.tuntap.ARP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class ARP extends TimerTask{
	private static Timer timer;
	private ArrayList<ARPTable> table;
	
	ARP(){
		table = new ArrayList<ARPTable>();
		timer = new Timer();
		timer.schedule(this, 1000, 30000);//30s
	}
	
	/**
	 * 搜尋ARP table中該IP address對應的MAC address
	 * @param IPAddr
	 * @return
	 */
	public byte[] searchMACbyIP(int IPAddr) {
		Iterator<ARPTable> it = table.iterator();
		while(it.hasNext()) {
			if(it.next().IPAddr==IPAddr) {
				it.next().flag = true;
				return it.next().MACAddr;
			}
		}
		return null;
	}
	
	/**
	 * generate a ARP request packet
	 * @param desIPAddr
	 * @param srcIPAddr
	 * @param srcMACAddr
	 * @return
	 */
	public byte[] generateARPrequestPacket(int srcIPAddr,byte[] srcMACAddr,int desIPAddr) {//
		
		return null;
	}
	
	public byte[] generateARPreplyPacket(int srcIPAddr,byte[] srcMACAddr,int desIPAddr,byte[] desMACAddr) {//
		
		return null;
	}
	
	public void addARPtable(byte[] packet) {//
		
	}
	
	private void TTLCounter() {
		Iterator<ARPTable> it = table.iterator();
		while(it.hasNext()) {
			if(it.next().flag)
				it.next().flag = false;
			else
				it.remove();
		}
	}
	
	@Override
	public void run() {
		TTLCounter();
	}

}
