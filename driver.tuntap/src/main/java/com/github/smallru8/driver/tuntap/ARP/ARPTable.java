package com.github.smallru8.driver.tuntap.ARP;

public class ARPTable {
	public int IPAddr;
	public byte[] MACAddr;
	public boolean flag;//被用到就設為true
	
	ARPTable(int IPAddr,byte[] MACAddr){
		this.IPAddr = IPAddr;
		this.MACAddr = MACAddr;
		flag = true;
	}
}
