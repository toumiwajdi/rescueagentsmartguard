package tekup.practice.android.smartguardrescuegard;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class choice extends AppCompatActivity {

    private TextView intervention,docname;
    private SharedPreferences preferences ;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();
    private RequestQueue requestQueue;
    private Boolean docdone=false;
    private static final String url="http://10.0.2.2:8080/SmartGuard/Model/api/InterventionDoctor.php";
    private static final String urlchoice="http://127.0.0.1:8080/SmartGuard/Model/api/InterventionChoice.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        intervention=(TextView) findViewById(R.id.intervention);
        intervention.append(preferences.getString("intervention",null));
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        docname=(TextView) findViewById(R.id.doctorname);
        startTimer();

    }
    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
    }
    private void setNotification(String msg){
        Intent intent = new Intent(getApplicationContext(), choice.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.healthcheckd)
                .setTicker("Hearty365")
                .setContentTitle("SmartGuard Doctor choice")
                .setContentText("")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    //To start timer
    private void startTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run(){

                        if (!docdone){
                            getDoctor();


                        }
                        else {
                            getChoice();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 2000, 2000);
    }
    private void getDoctor(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonobject = new JSONObject(response);
                    if (!(jsonobject.names().get(0).toString().equals("error"))) {
                        Toast.makeText(getApplicationContext(),jsonobject.get("0").toString()+"Just join you at your mission",Toast.LENGTH_LONG).show();
                        docname.append("Doctor : "+ jsonobject.get("0").toString());
                        docdone=true;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id",preferences.getString("intervention",null));
                return hashMap;

            }
        };
        requestQueue.add(request);
    }
    private void getChoice(){

        StringRequest request = new StringRequest(Request.Method.POST, urlchoice, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonobject = new JSONObject(response);
                    Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();
                    if (!(jsonobject.names().get(0).toString().equals("error"))) {
                        String msg="Response from doctor just Arrive : "+jsonobject.get("0").toString()+"  "+jsonobject.get("0").toString();
                        setNotification(msg);
                        SharedPreferences.Editor edito=preferences.edit();
                        edito.putString("Hospital",jsonobject.get("0").toString());
                        edito.putString("Service",jsonobject.get("1").toString());
                        stopTimer();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id",preferences.getString("intervention",null));
                return hashMap;

            }
        };
        requestQueue.add(request);

    }




}
