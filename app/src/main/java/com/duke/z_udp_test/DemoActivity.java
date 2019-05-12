package com.duke.z_udp_test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.duke.udp.UDPHelper;
import com.duke.udp.unicast.UDPUnicastSend;
import com.duke.udp.util.UDPConstant;
import com.duke.udp.util.UDPListener;
import com.duke.udp.util.UDPUtil;

public class DemoActivity extends AppCompatActivity {
    public static final String TAG = "UDP_TAG";

    public static final String PARAM_CAST_TYPE = "param_cast_type";
    public static final String PARAM_SEND_PORT = "param_send_port";
    public static final String PARAM_RECEIVE_PORT = "param_receive_port";
    public static final String PARAM_IP = "param_ip";

    private int castTypeValue;
    private int sendPort;
    private int receivePort;
    private String ip;

    private UDPHelper udpHelper;

    private TextView textViewMyIP;
    private TextView textViewListenIP;
    private TextView textViewListenSendPort;
    private TextView textViewListenReceivePort;
    private Button buttonStopSend;
    private Button buttonStopReceive;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Intent intent = getIntent();
        castTypeValue = intent.getIntExtra(PARAM_CAST_TYPE, UDPConstant.FLAG_CAST_TYPE_UNICAST);
        sendPort = intent.getIntExtra(PARAM_SEND_PORT, 65500);
        receivePort = intent.getIntExtra(PARAM_RECEIVE_PORT, 65501);
        ip = intent.getStringExtra(PARAM_IP);
        if (UDPUtil.isEmpty(ip)) {
            ip = UDPUnicastSend.DEFAULT_IP;
        }
        initViews();
        udpHelper = new UDPHelper(this, sendPort, receivePort, ip, castTypeValue);
        udpHelper.setUDPListener(udpListener);
        udpHelper.start();
    }

    private void initViews() {
        textViewMyIP = findViewById(R.id.textview_my_ip);
        textViewListenIP = findViewById(R.id.textview_linten_ip);
        textViewListenSendPort = findViewById(R.id.textview_linten_send_port);
        textViewListenReceivePort = findViewById(R.id.textview_linten_receive_port);
        buttonStopSend = findViewById(R.id.button_stop_send);
        buttonStopReceive = findViewById(R.id.button_stop_receive);
        recyclerView = findViewById(R.id.recycler_view);

        adapter = new MyAdapter();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        textViewMyIP.setText("我的IP：" + UDPUtil.getSimpleIP());
        textViewListenIP.setText("监听IP：" + ip);
        textViewListenSendPort.setText("发送端口：" + sendPort);
        textViewListenReceivePort.setText("接收端口：" + receivePort);

        buttonStopSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udpHelper.stopSend();
            }
        });
        buttonStopReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udpHelper.stopReceive();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        udpHelper.onDestroy();
    }

    private UDPListener udpListener = new UDPListener() {
        @Override
        public void onError(String error) {
            buttonStopReceive.setBackgroundColor(Color.RED);
            Toast.makeText(DemoActivity.this, "错误：" + error, Toast.LENGTH_SHORT).show();
            Log.v(TAG, error);
        }

        @Override
        public void onReceive(String content) {
            adapter.addItemToHead("收到内容：" + content);
            Log.v(TAG, content);
        }

        @Override
        public void onSend(String content) {
            Toast.makeText(DemoActivity.this, "发送成功：" + content, Toast.LENGTH_SHORT).show();
            Log.v(TAG, content);
        }
    };
}
