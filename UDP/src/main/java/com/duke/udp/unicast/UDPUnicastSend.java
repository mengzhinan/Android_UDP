package com.duke.udp.unicast;

import com.duke.udp.interf.UDPSendBase;
import com.duke.udp.util.UDPUtil;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 09:00
 * @Description:
 */
public class UDPUnicastSend extends UDPUnicastBase implements UDPSendBase {

    public UDPUnicastSend(int port, String ip) {
        super(port, ip);
    }


    public void send(String message) {
        if (UDPUtil.isEmpty(message)) {
            return;
        }
        try {
            byte[] bytes = message.getBytes();
            // 切记，发送的数据报中，必须要携带 端口 和 ip 地址
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, mInetAddress, mPort);
            mSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
