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
		return null;//table沒有紀錄
	}
	
	/**
	 * generate a ARP request packet
	 * @param desIPAddr
	 * @param srcIPAddr
	 * @param srcMACAddr
	 * @return
	 */
	public byte[] generateARPrequestPacket(int srcIPAddr,byte[] srcMACAddr,int desIPAddr) {
		byte[] packet = new byte[60];
		
		//des MAC addr FF:FF:FF:FF:FF:FF
		for(int i=0;i<6;i++)
			packet[i] = (byte) 0xFF;
		
		//src MAC addr srcMACAddr
		for(int i=0;i<6;i++)
			packet[i+6] = srcMACAddr[i];
		
		//Type = ARP
		packet[12] = 0x08;
		packet[13] = 0x06;
		
		//Hardware type = Ethernet
		packet[14] = 0x00;
		packet[15] = 0x01;
		
		//Protocol type = IPv4
		packet[16] = 0x08;
		packet[17] = 0x00;
		
		//Hardware size = 6
		packet[18] = 0x06;
		
		//Protocol size = 4
		packet[19] = 0x04;
		
		//Opcode = 1
		packet[20] = 0x00;
		packet[21] = 0x01;
		
		//src MAC address = srcMACAddr
		for(int i=0;i<6;i++)
			packet[i+22] = srcMACAddr[i];
		
		//src IP address = srcIPAddr
		packet[28] = (byte) ((srcIPAddr>>24)&0xFF);
		packet[29] = (byte) ((srcIPAddr>>16)&0xFF);
		packet[30] = (byte) ((srcIPAddr>>8)&0xFF);
		packet[31] = (byte) (srcIPAddr&0xFF);
		
		//des MAC address = 00:00:00:00:00:00
		for(int i=32;i<38;i++)
			packet[i] = 0x00;
		
		//des IP address = desIPAddr
		packet[38] = (byte) ((desIPAddr>>24)&0xFF);
		packet[39] = (byte) ((desIPAddr>>16)&0xFF);
		packet[40] = (byte) ((desIPAddr>>8)&0xFF);
		packet[41] = (byte) (desIPAddr&0xFF);
		
		//Padding
		for(int i=42;i<60;i++)
			packet[i] = 0x00;
		
		return packet;
	}
	
	/**
	 * generate a ARP reply packet
	 * @param srcIPAddr
	 * @param srcMACAddr
	 * @param desIPAddr
	 * @param desMACAddr
	 * @return
	 */
	public byte[] generateARPreplyPacket(int srcIPAddr,byte[] srcMACAddr,int desIPAddr,byte[] desMACAddr) {
		byte[] packet = new byte[60];
		
		//des MAC addr desMACAddr
		for(int i=0;i<6;i++)
			packet[i] = desMACAddr[i];
		
		//src MAC addr srcMACAddr
		for(int i=0;i<6;i++)
			packet[i+6] = srcMACAddr[i];
		
		//Type = ARP
		packet[12] = 0x08;
		packet[13] = 0x06;
		
		//Hardware type = Ethernet
		packet[14] = 0x00;
		packet[15] = 0x01;
		
		//Protocol type = IPv4
		packet[16] = 0x08;
		packet[17] = 0x00;
		
		//Hardware size = 6
		packet[18] = 0x06;
		
		//Protocol size = 4
		packet[19] = 0x04;
		
		//Opcode = 2
		packet[20] = 0x00;
		packet[21] = 0x02;
		
		//src MAC address = srcMACAddr
		for(int i=0;i<6;i++)
			packet[i+22] = srcMACAddr[i];
		
		//src IP address = srcIPAddr
		packet[28] = (byte) ((srcIPAddr>>24)&0xFF);
		packet[29] = (byte) ((srcIPAddr>>16)&0xFF);
		packet[30] = (byte) ((srcIPAddr>>8)&0xFF);
		packet[31] = (byte) (srcIPAddr&0xFF);
		
		//des MAC address = desMACAddr
		for(int i=32;i<38;i++)
			packet[i] = desMACAddr[i-32];
		
		//des IP address = desIPAddr
		packet[38] = (byte) ((desIPAddr>>24)&0xFF);
		packet[39] = (byte) ((desIPAddr>>16)&0xFF);
		packet[40] = (byte) ((desIPAddr>>8)&0xFF);
		packet[41] = (byte) (desIPAddr&0xFF);
		
		//Padding
		for(int i=42;i<60;i++)
			packet[i] = 0x00;
		
		return packet;
	}
	
	/**
	 * 傳入ARP reply packet 將結果更新到ARP table
	 * @param framePacket
	 */
	public void addARPtable(byte[] framePacket) {
		if(framePacket[12]==0x08&&framePacket[13]==0x06&&framePacket[21]==0x02) {//為ARP reply
			int IPAddr = (framePacket[28]&0xFF)<<24|(framePacket[29]&0xFF)<<16|(framePacket[30]&0xFF)<<8|(framePacket[31]&0xFF);
			byte[] MACAddr = new byte[6];
			for(int i=22;i<28;i++)
				MACAddr[i-22] = framePacket[i];
			ARPTable tmpTable = new ARPTable(IPAddr,MACAddr);
			Iterator<ARPTable> it = table.iterator();
			while(it.hasNext()) {//檢查重複 並更新
				if(it.next().IPAddr==IPAddr) {
					it.remove();
					break;
				}
			}
			table.add(tmpTable);
		}
	}
	
	/**
	 * 定時刪除過久未使用ARP
	 */
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
