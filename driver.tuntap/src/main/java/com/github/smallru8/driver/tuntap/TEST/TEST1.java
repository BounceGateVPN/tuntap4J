package com.github.smallru8.driver.tuntap.TEST;

import java.io.IOException;

import com.github.smallru8.driver.tuntap.Analysis;
import com.github.smallru8.driver.tuntap.TunTap;

public class TEST1 {
	public static void main( String[] args ) throws InterruptedException, IOException
    {  	
    	Analysis packet = new Analysis();
    	
        TunTap tt = new TunTap();
        System.out.println(tt.tuntap_set_ip("192.168.87.2", 24));
        
        tt.tuntap_up();
        byte[] mac = tt.tuntap_get_hwaddr();
        System.out.println(Integer.toHexString(mac[0]&0xFF)+ "-"+Integer.toHexString(mac[1]& 0xFF)+ "-"+Integer.toHexString(mac[2]& 0xFF)+ "-"+Integer.toHexString(mac[3]& 0xFF)+ "-"+Integer.toHexString(mac[4]& 0xFF)+ "-"+Integer.toHexString(mac[5]& 0xFF));
        
        System.out.println("fd:"+tt.tuntap_get_fd());
        System.out.println("press any key to continue...");
		System.in.read();
        
		while(true) {
        	//byte[] ip = new byte[4];
        	byte[] buffer = tt.tuntap_read(512);
        	packet.setFramePacket(buffer);
        	if(buffer.length==0) {
        		Thread.sleep(1000);
        		continue;
        	}
        	int desAddr = packet.getDesIPaddress();
        	int srcAddr = packet.getSrcIPaddress();
        	System.out.println(desAddr);
        	System.out.println(srcAddr);
        	
        	System.out.println("Recv : " + buffer.length);
        	/*if(packet.packetType()==0x00) {
	        	packet.setDesIPaddress((byte)(srcAddr>>24), (byte)(srcAddr>>16), (byte)(srcAddr>>8), (byte)srcAddr);
	        	packet.setSrcIPaddress((byte)(desAddr>>24), (byte)(desAddr>>16), (byte)(desAddr>>8), (byte)desAddr);
	        	byte[] srcMAC = packet.getFrameSrcMACAddr();
	        	byte[] desMAC = packet.getFrameDesMACAddr();
	        	packet.setFrameDesMACAddr(srcMAC);
	        	packet.setFrameSrcMACAddr(desMAC);
	        	buffer = packet.getFramePacket();
	        	//System.out.println("Send : " + tt.tuntap_write(buffer,buffer.length));
        	}*/
        }
    }
}
