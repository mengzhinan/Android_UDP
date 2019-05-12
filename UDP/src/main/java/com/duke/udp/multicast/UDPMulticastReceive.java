package com.duke.udp.multicast;

import android.content.Context;

import com.duke.udp.interf.UDPReceiveBase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.Charset;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 08:58
 * @Description:
 */
public class UDPMulticastReceive extends UDPMulticastBase implements UDPReceiveBase {

    public UDPMulticastReceive(Context context, int port, String ip) {
        super(context, UDPMulticastReceive.class.getSimpleName(), port, ip);
    }

    public String receive() {
        return receive(1024);
    }

    public String receive(int byteArrayLength) {
        try {
            byte[] bytes = new byte[byteArrayLength];
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            mMulticastSocket.receive(packet);
            return new String(packet.getData(), packet.getOffset(), packet.getLength(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }
}
