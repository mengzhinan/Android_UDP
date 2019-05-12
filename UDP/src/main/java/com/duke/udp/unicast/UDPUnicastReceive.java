package com.duke.udp.unicast;

import com.duke.udp.interf.UDPReceiveBase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.Charset;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 09:00
 * @Description:
 */
public class UDPUnicastReceive extends UDPUnicastBase implements UDPReceiveBase {

    public UDPUnicastReceive(int port, String ip) {
        super(port, ip);
    }

    public String receive() {
        return receive(1024);
    }

    public String receive(int byteArrayLength) {
        try {
            byte[] bytes = new byte[byteArrayLength];
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            mSocket.receive(packet);
            return new String(packet.getData(), packet.getOffset(), packet.getLength(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }
}
