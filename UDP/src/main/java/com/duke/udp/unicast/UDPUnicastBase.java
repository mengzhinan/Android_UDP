package com.duke.udp.unicast;

import android.annotation.SuppressLint;
import android.content.Context;

import com.duke.udp.util.UDPUtil;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 08:59
 * @Description:
 */
public abstract class UDPUnicastBase {
    public static final String DEFAULT_IP = "255.255.255.255";
    public static final int DEFAULT_PORT = 65500;
    protected DatagramSocket mSocket;
    protected Context applicationContext;

    protected int mPort;
    protected String mIp;
    protected InetAddress mInetAddress;

    @SuppressLint("WifiManagerPotentialLeak")
    public UDPUnicastBase(int port, String ip) {
        mInetAddress = UDPUtil.getInetAddressByIP(ip);
        mPort = port;
        mIp = ip;
        try {
            // 先设置 null
            mSocket = new DatagramSocket(null);
            // TODO: 2019/5/12 此方法无效果
            // 绑定之前先设置Reuse
            mSocket.setReuseAddress(true);
            // 然后再绑定
            mSocket.bind(new InetSocketAddress(port));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void closeAll() {
        if (mSocket != null) {
            try {
                if (mSocket.isConnected()) {
                    mSocket.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!mSocket.isClosed()) {
                    mSocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSocket = null;
        }
        mInetAddress = null;
        applicationContext = null;
    }
}
