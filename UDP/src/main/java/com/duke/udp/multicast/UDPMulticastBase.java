package com.duke.udp.multicast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;

import com.duke.udp.util.UDPUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 08:57
 * @Description:
 */
public abstract class UDPMulticastBase {
    //    public static final String DEFAULT_IP = "239.2.3.6";
    public static final String DEFAULT_IP = "224.0.0.1";
    public static final int DEFAULT_PORT = 18888;

    public static final String WLAN = "wlan0";

    private WifiManager.MulticastLock mMulticastLock = null;
    protected MulticastSocket mMulticastSocket;
    protected Context applicationContext;

    protected int mPort;
    protected String mIp;
    protected InetAddress mInetAddress;

    @SuppressLint("WifiManagerPotentialLeak")
    public UDPMulticastBase(Context context, String lockName, int port, String ip) {
        applicationContext = context.getApplicationContext();
        mInetAddress = UDPUtil.getInetAddressByIP(ip);
        mPort = port;
        mIp = ip;
//        WifiManager wifiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
//        mMulticastLock = wifiManager.createMulticastLock(lockName);
//        mMulticastLock.acquire();
        try {
            mMulticastSocket = new MulticastSocket(port);
            mMulticastSocket.joinGroup(mInetAddress);
//            mMulticastSocket.setNetworkInterface(NetworkInterface.getByName(WLAN));
//            mMulticastSocket.setBroadcast(true);
//            socket.setReuseAddress(true);
//            mMulticastSocket.setLoopbackMode(false);
//            mMulticastSocket.setTimeToLive(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeAll() {
        if (mMulticastSocket != null) {
            try {
                mMulticastSocket.leaveGroup(mInetAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (mMulticastSocket.isConnected()) {
                    mMulticastSocket.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!mMulticastSocket.isClosed()) {
                    mMulticastSocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMulticastSocket = null;
        }
        if (mMulticastLock != null) {
            if (mMulticastLock.isHeld()) {
                mMulticastLock.release();
            }
            mMulticastLock = null;
        }
        mInetAddress = null;
        applicationContext = null;
    }
}
