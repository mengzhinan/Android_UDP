package com.duke.udp;

import android.annotation.SuppressLint;
import android.content.Context;

import com.duke.udp.interf.UDPReceiveBase;
import com.duke.udp.interf.UDPSendBase;
import com.duke.udp.multicast.UDPMulticastBase;
import com.duke.udp.multicast.UDPMulticastReceive;
import com.duke.udp.multicast.UDPMulticastSend;
import com.duke.udp.unicast.UDPUnicastBase;
import com.duke.udp.unicast.UDPUnicastReceive;
import com.duke.udp.unicast.UDPUnicastSend;
import com.duke.udp.util.DExecutor;
import com.duke.udp.util.InnerHandler;
import com.duke.udp.util.UDPConstant;
import com.duke.udp.util.UDPListener;
import com.duke.udp.util.UDPUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 17:29
 * @Description:
 */
public class UDPHelper {
    private InnerHandler handler;

    private UDPSendBase sendSocket;
    private UDPReceiveBase receiveSocket;

    private volatile boolean isStopSend;
    private volatile boolean isStopReceive;

    private volatile long sendGap = 1000;

    /**
     * 设置发送线程的时间间隔，避免发送速度过快
     *
     * @param sendGap
     */
    private void setSendGap(long sendGap) {
        this.sendGap = sendGap;
    }

    /**
     * 停止发送消息
     */
    public void stopSend() {
        isStopSend = true;
    }

    /**
     * 停止接收消息
     */
    public void stopReceive() {
        isStopReceive = true;
    }

    /**
     * 设置数据回调
     *
     * @param listener
     */
    public void setUDPListener(UDPListener listener) {
        handler.setListener(listener);
    }

    public UDPHelper(Context context, int sendPort, int receivePort, String ip, int castType) {
        handler = new InnerHandler();
        if (castType == UDPConstant.FLAG_CAST_TYPE_MULTICAST) {
            // 多播
            sendSocket = new UDPMulticastSend(context, sendPort, ip);
            receiveSocket = new UDPMulticastReceive(context, receivePort, ip);
        } else {
            // 单播
            sendSocket = new UDPUnicastSend(sendPort, ip);
            receiveSocket = new UDPUnicastReceive(receivePort, ip);
        }
    }

    public void start() {
        //接收线程
        DExecutor.get().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isStopReceive) {
                        handler.receiveSuccess(receiveSocket.receive());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.error(e.getLocalizedMessage());
                } finally {
                    if (receiveSocket instanceof UDPUnicastBase) {
                        ((UDPUnicastBase) receiveSocket).closeAll();
                    } else if (receiveSocket instanceof UDPMulticastBase) {
                        ((UDPMulticastBase) receiveSocket).closeAll();
                    }
                }
            }
        });


        // 发送线程
        DExecutor.get().execute(new Runnable() {
            private String myIP;
            @SuppressLint("SimpleDateFormat")
            private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            @Override
            public void run() {
                try {
                    myIP = UDPUtil.getSimpleIP();
                    Date date = new Date();
                    String text = null;
                    while (!isStopSend) {
                        date.setTime(System.currentTimeMillis());
                        text = myIP + "_" + sdf.format(date);
                        sendSocket.send(text);
                        handler.sendSuccess(text);
                        if (sendGap > 0) {
                            Thread.sleep(sendGap);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.error(e.getLocalizedMessage());
                } finally {
                    if (sendSocket instanceof UDPUnicastBase) {
                        ((UDPUnicastBase) sendSocket).closeAll();
                    } else if (sendSocket instanceof UDPMulticastBase) {
                        ((UDPMulticastBase) sendSocket).closeAll();
                    }
                }
            }

        });
    }

    public void onDestroy() {
        stopSend();
        stopReceive();
        DExecutor.get().shutdownNow();
    }
}
