package tekup.practice.android.smartguardrescuegard;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class intervention_add extends AppCompatActivity {
    private TextView hos,ser;
    private Button finish;
    private SharedPreferences preferences;
    private static String url="";
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention_add);
        hos=(TextView) findViewById(R.id.hospitalt);
        ser=(TextView) findViewById(R.id.servicet);
        hos.append(preferences.getString("Hospital",null));
        ser.append(preferences.getString("Service",null));
        requestQueue=Volley.newRequestQueue(getApplicationContext());
        setButtonClick();

    }
    private void setButtonClick(){
        finish=(Button) findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),"You just finish thank you to save lifes",Toast.LENGTH_LONG);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("id",preferences.getString("intervention",null));
                        return hashMap;
                    }
                };
                requestQueue.add(request);
            }
        });
    }
}
