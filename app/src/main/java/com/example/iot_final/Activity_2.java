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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Activity_2 extends AppCompatActivity {


    MQTTHelper mqttHelper;
    GraphView HumiGraph;
    //Date currentTime = Calendar.getInstance().getTime();
    private LineGraphSeries<DataPoint> HumiGraphValue = new LineGraphSeries<>();
    private int countX=0;


    private void showDataOnGraph(LineGraphSeries<DataPoint> series, GraphView graph){
        if(graph.getSeries().size() > 0){
            graph.getSeries().remove(0);
        }
        graph.addSeries(series);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
    }


    TextView txtTemp, txtHumi;
    ImageView imTemp,imHumi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2); //lay xml bo vo activity tao giao dien, trc do se bi NULL


        txtTemp =findViewById(R.id.txtTemperature);
        txtHumi= findViewById(R.id.txtHumidity);
        imTemp =findViewById(R.id.imageTemperature);
        imHumi = findViewById(R.id.ImageHumidity);


        startMQTT();
        HumiGraph = findViewById(R.id.HumiGraph);
        showDataOnGraph(HumiGraphValue,HumiGraph);

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
                if(topic.contains("bbc-humid"))
                {
                    int humi_temp = Integer.parseInt(message.toString());
                    txtHumi.setText(message.toString()+"%");
                    if(humi_temp <= 25) imHumi.setBackground(getDrawable(R.drawable.humi_1));
                    else  if(humi_temp >25 && humi_temp <= 50 )imHumi.setBackground(getDrawable(R.drawable.humi_2));
                    else  if(humi_temp >50 && humi_temp <= 75 )imHumi.setBackground(getDrawable(R.drawable.humi_3));
                    else  if(humi_temp >75 && humi_temp <= 100 )imHumi.setBackground(getDrawable(R.drawable.humi_4));
                    HumiGraphValue.appendData(new DataPoint(countX++,humi_temp),true,5);
                    showDataOnGraph(HumiGraphValue,HumiGraph);
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
