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

public class activity_login_two extends AppCompatActivity {

    private EditText username,password;
    private RequestQueue requestQueue;
    private static final String url="http://10.0.2.2:8080/SmartGuard/Model/api/login.php";
    private Button login;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_two);
        setButtonclik();
        setLogin();
        requestQueue= Volley.newRequestQueue(activity_login_two.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.v("SP:",preferences.getString("username",null));
    }

    private void setButtonclik() {
        Button login= (Button) findViewById(R.id.signin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_login_two.this,sigin.class));
            }
        });
    }
    private void setLogin(){
        username=(EditText) findViewById(R.id.logintext);
        password=(EditText) findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            if (jsonobject.names().get(0).toString().equals("inactive")) {
                                Toast.makeText(getApplicationContext(), jsonobject.getString("inactive"), Toast.LENGTH_SHORT).show();
                            }
                            else if (jsonobject.names().get(0).toString().equals("error")){
                                Toast.makeText(getApplicationContext(),jsonobject.getString("error"),Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                                SharedPreferences.Editor editor = preferences.edit();
                                Toast.makeText(getApplicationContext(), "Welcome back", Toast.LENGTH_SHORT).show();
                                editor.putString("id",jsonobject.getString("id"));
                                editor.putString("username", jsonobject.getString("username"));
                                editor.apply();
                                Intent intervention = new Intent(activity_login_two.this, startmenu.class);
                                startActivity(intervention);
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
                        hashMap.put("login", username.getText().toString());
                        hashMap.put("password", password.getText().toString());
                        Log.v("Hashmap",hashMap.toString());
                        return hashMap;

                    }
                };
                requestQueue.add(request);
            }
        });

    }

}
