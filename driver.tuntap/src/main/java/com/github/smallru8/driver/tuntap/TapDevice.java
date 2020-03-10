package com.github.smallru8.driver.tuntap;

import java.net.SocketException;

import com.github.smallru8.driver.tuntap.ARP.ARP;

public class TapDevice {

	public static TunTap tap;
	public static ARP arp;
	
	public TapDevice() {
		tap = new TunTap();
		arp = new ARP();
	}
	
	public void startEthernetDev() {
		tap.tuntap_up();
		
		try {//初始化ARP table
			arp.setARP(tap.tuntap_get_hwaddr());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * read packet 自動處理ARP packet
	 * 若為ARP return null
	 * 若為IPv4 return data
	 * @param len
	 * @return
	 */
	public byte[] read(int len) {//len = 0為 自動調整大小
		byte[] data = tap.tuntap_read(len);
		
		Analysis analyzer = new Analysis();
		analyzer.setFramePacket(data);
		if(analyzer.packetType()==0x06) {//ARP
			byte[] data_send = arp.arpAnalyzer(data);
			if(data_send!=null)
				write(data_send);	
			return null;
		}
		
		return data;
	}
	
	/**
	 * return 送出的byte數
	 * @param data
	 * @return
	 */
	public int write(byte[] data) {
		return tap.tuntap_write(data, data.length);
	}
	
	/**
	 * 查ARP紀錄
	 * 無紀錄自動發 arp request, return null
	 * @param IPAddr
	 * @return
	 */
	public byte[] getMACbyIP(byte[] IPAddr) {
		byte[] mac = arp.searchMACbyIP(IPAddr);
		if(mac!=null)
			return mac;
		write(arp.generateARPrequestPacket(arp.IPAddrs.get(0), arp.MACAddr, IPAddr));//發ARP request
		return null;
	}
}
