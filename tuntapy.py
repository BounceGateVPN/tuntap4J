import os
import jpype
import numpy as np
# 'require jpype1 and numpy


class JTunTap:

    jTap = None;

    def __init__(self):
        jpype.startJVM(jpype.getDefaultJVMPath(), "-ea", "-Djava.class.path="+os.path.abspath("TunTap4J.jar"));
        tmpJC = jpype.JClass("com.github.smallru8.driver.tuntap.TunTap");
        self.jTap = tmpJC();

    def __del__(self):
        self.jTap.tuntap_destroy();
        jpype.shutdownJVM();

    def version(self):
        return self.jTap.tuntap_version();  # 'return int

    def destroy(self):
        self.jTap.tuntap_destroy();

    def release(self):
        self.jTap.tuntap_release();

    def get_ifname(self):
        return self.jTap.tuntap_get_ifname();  # 'return String

    def set_ifname(self,ifname):  # 'ifname should be a String type.
        return self.jTap.tuntap_set_ifname(ifname);  # 'return int

    def get_hwaddr(self):
        return self.jTap.tuntap_get_hwaddr();  # 'return String

    def set_hwaddr(self,hwaddr):  # 'hwaddr should be a String type.
        return self.jTap.tuntap_set_hwaddr(hwaddr);  # 'return int

    def set_descr(self,desc):  # 'desc should be a String type.
        return self.jTap.tuntap_set_descr(desc);  # 'return int

    def get_descr(self):
        return self.jTap.tuntap_get_descr();  # 'return String

    def up(self):
        return self.jTap.tuntap_up();  # 'return int

    def down(self):
        return self.jTap.tuntap_down();  # 'return int

    def get_mtu(self):
        return self.jTap.tuntap_get_mtu();  # 'return int

    def set_mtu(self,mtu):  # 'mtu should be a int type.
        return self.jTap.tuntap_set_mtu(mtu);  # 'return int

    def set_ip(self,ipaddr,mask):  # 'ipaddr should be a String type, mask should be a int type.
        return self.jTap.tuntap_set_ip(ipaddr,mask);  # 'return int

    def read(self,len):  # 'len should be a int type.
        jByteArray = self.jTap.tuntap_read(len);
        return np.array(jByteArray);  # 'return numpy bytearray

    def write(self,data,len):  # 'data should be a "byte numpy array" type, len should be a int type.
        jByteArray = jpype.JArray(jpype.JByte,data.ndim)(data.tolist());
        return self.jTap.tuntap_write(jByteArray,len);  # 'return int

    def get_readable(self):
        return self.jTap.tuntap_get_readable();  # 'return int

    def set_nonblocking(self,flag):  # 'flag should be a int type.
        return self.jTap.tuntap_set_nonblocking(flag);  # 'return int

    def set_debug(self,flag):  # 'flag should be a int type.
        return self.jTap.tuntap_set_debug(flag);  # 'return int

    def get_fd(self):
        return self.jTap.tuntap_get_fd();  # 'return int
