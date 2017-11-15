package tekup.practice.android.smartguardrescuegard;


import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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
    private static final String url="http://10.0.2.2:8080/SmartGuard/Model/api/InterventionDoctor.php";
    private static final String urlchoice="";
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
        startTimer2();

    }
    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
    }
    private NotificationCompat.Builder setNotification(String msg){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.healthcheckd)
                        .setContentTitle("Smart Guard The choice just came from doctor")
                        .setContentText(msg);
        return mBuilder;
    }

    //To start timer
    private void startTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run(){
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonobject = new JSONObject(response);
                                    if (!(jsonobject.names().get(0).toString().equals("error"))) {
                                        Toast.makeText(getApplicationContext(),jsonobject.getString("docname")+" has join you to the mission",Toast.LENGTH_LONG).show();

                                        docname.append(jsonobject.getString("docname"));
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
                });
            }
        };
        timer.schedule(timerTask, 2000, 2000);
    }
    private void stopTimer2(){
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
    }

    //To start timer
    private void startTimer2(){
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run(){
                        StringRequest request = new StringRequest(Request.Method.POST, urlchoice, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonobject = new JSONObject(response);
                                    if (!(jsonobject.names().get(0).toString().equals("error"))) {
                                        String msg="";
                                        NotificationCompat.Builder mBuilder=setNotification(msg);
                                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mNotifyMgr.notify(001, mBuilder.build());

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
                });
            }
        };
        timer.schedule(timerTask, 2000, 2000);
    }
}
