package com.example.iot_final;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class Activity_3 extends AppCompatActivity {
    MQTTHelper mqttHelper;
    GraphView graphTest;
    Date currentTime = Calendar.getInstance().getTime();

    private LineGraphSeries<DataPoint> testGraphValue = new LineGraphSeries<>();
    private int countX=0;


    private void showDataOnGraph(LineGraphSeries<DataPoint> series, GraphView graph){
        if(graph.getSeries().size() > 0){
            graph.getSeries().remove(0);
        }
        graph.addSeries(series);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        startMQTT();

        graphTest = findViewById(R.id.graphTest);

        showDataOnGraph(testGraphValue,graphTest);
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
                if(topic.contains("bbc-temp"))
                {
                    int temp_temp = Integer.parseInt(message.toString());
                    //txtTemp.setText(message.toString()+"°C");
                    //if(temp_temp > 35) imTemp.setBackground(getDrawable(R.drawable.thermo_hot));
                    //else  imTemp.setBackground(getDrawable(R.drawable.thermo_cold));
                    testGraphValue.appendData(new DataPoint(countX++,temp_temp),true,5);
                    showDataOnGraph(testGraphValue,graphTest);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {


            }

        });
    }

}