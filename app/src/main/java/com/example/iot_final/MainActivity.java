package com.example.iot_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;


public class MainActivity extends AppCompatActivity {
    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;

    TextView txtButton;
    ToggleButton btnLED;

    com.github.ybq.android.spinkit.SpinKitView spinIcon;


    MQTTHelper mqttHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button1 = (ImageButton) findViewById(R.id.button1);
        button2 = (ImageButton) findViewById(R.id.button2);
        button3 = (ImageButton) findViewById(R.id.button3);

        btnLED= (ToggleButton) findViewById(R.id.buttonLed);

        txtButton = (TextView) findViewById(R.id.txtButton);

        spinIcon = (com.github.ybq.android.spinkit.SpinKitView) findViewById(R.id.spin_kit);
        startMQTT();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity1();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });

        btnLED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    txtButton.setText("ON");
                    spinIcon.setVisibility(View.INVISIBLE);
                    Log.d("mqtt","Button : ON");
                    sendDataMQTT("minhducletran2812/feeds/bbc-led","1");
                }
                else{
                    txtButton.setText("OFF");
                    spinIcon.setVisibility(View.VISIBLE);
                    Log.d("mqtt","Button : OFF");
                    sendDataMQTT("minhducletran2812/feeds/bbc-led","0");

                }
            }
        });

    }

    private void startMQTT(){
        mqttHelper = new MQTTHelper(getApplicationContext(),"28122000");
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d("mqtt","Connection is successfull ");
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("mqtt","Mess Received :  "+message.toString());
                if(topic.contains("bbc-led"))
                {
                    if(message.toString().contains("1")) btnLED.setChecked(true);
                    else btnLED.setChecked(false);
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {


            }

        });
    }
    private void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);


        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (Exception e){}

    }
    public void openActivity1() {
        Intent intent = new Intent(this, Activity_1.class);
        startActivity(intent);
    }
    public void openActivity2() {
        Intent intent = new Intent(this, Activity_2.class);
        startActivity(intent);
    }
    public void openActivity3() {
        Intent intent = new Intent(this, Activity_3.class);
        startActivity(intent);
    }
}