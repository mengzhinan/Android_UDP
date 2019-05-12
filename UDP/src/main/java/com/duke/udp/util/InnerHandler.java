package com.duke.udp.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 17:40
 * @Description:
 */
public class InnerHandler extends Handler {
    private static final int FLAG_SEND = 1;
    private static final int FLAG_RECEIVE = 2;
    private static final int FLAG_ERROR = 3;


    private UDPListener listener;

    public void setListener(UDPListener listener) {
        this.listener = listener;
    }

    public InnerHandler() {
        super(Looper.getMainLooper());
    }

    public void sendSuccess(String text) {
        send(text, FLAG_SEND);
    }

    public void receiveSuccess(String text) {
        send(text, FLAG_RECEIVE);
    }

    public void error(String text) {
        send(text, FLAG_ERROR);
    }

    private void send(String text, int flag) {
        Message message = Message.obtain();
        message.what = flag;
        message.obj = text;
        sendMessage(message);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (listener == null || msg == null || msg.obj == null) {
            return;
        }
        String text = msg.obj.toString();
        switch (msg.what) {
            case FLAG_SEND:
                listener.onSend(text);
                break;
            case FLAG_RECEIVE:
                listener.onReceive(text);
                break;
            case FLAG_ERROR:
                listener.onError(text);
                break;
        }
    }
}
