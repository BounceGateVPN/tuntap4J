package com.github.smallru8.driver.tuntap;


/**
 * TunTap Test
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        TunTap tt = new TunTap();
        tt.tuntap_set_ip("192.168.87.1", 24);
        tt.tuntap_up();
        while(true) {
        	byte[] ip = new byte[4];
        	byte[] buffer = tt.tuntap_read(4096);
        	System.out.println("Recv : " + buffer.length);
        	if(buffer == null)
        		break;
        	for(int i=12;i<16;i++)
        		ip[i-12] = buffer[i];
        	for(int i=16;i<20;i++)
        		buffer[i-4] = buffer[i];
        	for(int i=16;i<20;i++)
        		buffer[i] = ip[i-16];
        	buffer[20] = 0;
        	buffer[22]+=8;
        	System.out.println("Send : " + tt.tuntap_write(buffer,4096));
        }
    }
}
