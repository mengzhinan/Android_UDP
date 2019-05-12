package com.duke.udp.multicast;

import android.content.Context;

import com.duke.udp.interf.UDPSendBase;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 08:58
 * @Description:
 */
public class UDPMulticastSend extends UDPMulticastBase implements UDPSendBase {

    public UDPMulticastSend(Context context, int port, String ip) {
        super(context, UDPMulticastSend.class.getSimpleName(), port, ip);
    }

    public void send(String message) {
        try {
            byte[] bytes = message.getBytes();
            // TODO: 2019/5/10  数据包上需要设置 组播地址 和 端口 。否则数据包发给谁收？
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, mInetAddress, mPort);
            mMulticastSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
