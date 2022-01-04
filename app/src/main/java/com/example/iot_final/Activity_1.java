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

public class Activity_1 extends AppCompatActivity {
    MQTTHelper mqttHelper;
    GraphView tempGraph;
    //Date currentTime = Calendar.getInstance().getTime();
    private LineGraphSeries<DataPoint> tempGraphValue = new LineGraphSeries<>();
    private int countX=0;
    //private Calendar cal = Calendar.getInstance();

    private void showDataOnGraph(LineGraphSeries<DataPoint> series, GraphView graph){
        if(graph.getSeries().size() > 0){
            graph.getSeries().remove(0);
        }
        graph.addSeries(series);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
    }

    TextView txtTemp, txtHumi;
    ImageView imTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1); //lay xml bo vo activity tao giao dien, trc do se bi NULL


        txtTemp =findViewById(R.id.txtTemperature);
        txtHumi= findViewById(R.id.txtHumidity);
        imTemp =findViewById(R.id.imageTemperature);

        //txtTemp.setText("50°C");
        //txtHumi.setText("60%");

        startMQTT();
        tempGraph = findViewById(R.id.TempGraph);
        showDataOnGraph(tempGraphValue,tempGraph);

        Calendar rightNow = Calendar.getInstance();
        //int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);

        //int currentHourIn12Format = rightNow.get(Calendar.HOUR);

        //countX = currentHourIn24Format;
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
                    txtTemp.setText(message.toString()+"°C");
                    if(temp_temp > 35) imTemp.setBackground(getDrawable(R.drawable.thermo_hot));
                    else  imTemp.setBackground(getDrawable(R.drawable.thermo_cold));
                    //testGraphValue.appendData(new DataPoint(countX++,temp_temp),true,5);
                    tempGraphValue.appendData(new DataPoint(countX++,temp_temp),true,5);
                    showDataOnGraph(tempGraphValue,tempGraph);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
