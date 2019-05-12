package com.duke.udp.interf;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 18:18
 * @Description:
 */
public interface UDPReceiveBase {
    public abstract String receive();

    public abstract String receive(int byteArrayLength);
}
