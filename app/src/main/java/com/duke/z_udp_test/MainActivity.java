package com.duke.z_udp_test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.duke.udp.util.UDPConstant;
import com.duke.udp.util.UDPUtil;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMyIP;

    // 端口 和 ip
    private EditText editTextSendPort;
    private EditText editTextReceivePort;
    private EditText editTextIP;

    // 单播 or 多播
    private RadioGroup radioGroupCastType;
    private RadioButton radioButtonUnicast;
    private RadioButton radioButtonMulticast;

    //开始试验
    private Button buttonStart;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMyIP = findViewById(R.id.text_my_ip);
        textViewMyIP.setText("我的IP：" + UDPUtil.getSimpleIP());
        editTextSendPort = findViewById(R.id.edittext_send_port);
        editTextReceivePort = findViewById(R.id.edittext_receive_port);
        editTextIP = findViewById(R.id.edittext_ip);
        radioGroupCastType = findViewById(R.id.radiogroup_cast_type);
        radioButtonUnicast = findViewById(R.id.radiobutton_unicast);
        radioButtonMulticast = findViewById(R.id.radiobutton_multicast);
        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取 端口 (随便给一个)
                int sendPort = 65500;
                try {
                    sendPort = Integer.parseInt(editTextSendPort.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int receivePort = 65501;
                try {
                    receivePort = Integer.parseInt(editTextReceivePort.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // ip
                String ip = editTextIP.getText().toString();

                int castType = UDPConstant.FLAG_CAST_TYPE_UNICAST;
                if (radioButtonUnicast.isChecked()) {
                    castType = UDPConstant.FLAG_CAST_TYPE_UNICAST;
                } else if (radioButtonMulticast.isChecked()) {
                    castType = UDPConstant.FLAG_CAST_TYPE_MULTICAST;
                }

                Intent intent = new Intent(MainActivity.this, DemoActivity.class);
                intent.putExtra(DemoActivity.PARAM_SEND_PORT, sendPort);
                intent.putExtra(DemoActivity.PARAM_RECEIVE_PORT, receivePort);
                intent.putExtra(DemoActivity.PARAM_IP, ip);
                intent.putExtra(DemoActivity.PARAM_CAST_TYPE, castType);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
