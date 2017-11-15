package tekup.practice.android.smartguardrescuegard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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



public class startmenu extends AppCompatActivity {
    private EditText commentry;
    private Button start;
    private RequestQueue requestQueue;
    private static final String url="http://10.0.2.2:8080/SmartGuard/Model/api/AddIntervention.php";
    private TextView securename;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startmenu);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        securename=(TextView) findViewById(R.id.name);
        securename.append(preferences.getString("username",null));
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        setIntervention();

    }

    private void setIntervention(){
        commentry=(EditText) findViewById(R.id.commentry);
        start=(Button) findViewById(R.id.startInter);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            if (!(jsonobject.getString("res").isEmpty())){
                                SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("intervention",jsonobject.getString("res"));
                                editor.commit();
                                Intent intent=new Intent(startmenu.this,choice.class);
                                startActivity(intent);

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
                        hashMap.put("rescue_id", preferences.getString("id",null));
                        hashMap.put("commentry", commentry.getText().toString());
                        Log.v("Hashmap",hashMap.toString());
                        return hashMap;

                    }
                };
                requestQueue.add(request);
            }
        });
    }
}
