package tekup.practice.android.smartguardrescuegard;

import android.content.Intent;
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

public class sigin extends AppCompatActivity {

    private EditText username,password,work_id,mail,firstname,lastname;
    private RequestQueue requestQueue;
    private static final String url="http://10.0.2.2:8080/SmartGuard/Model/api/SignupRescueGuard.php";
    private Button sigin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);
        requestQueue= Volley.newRequestQueue(sigin.this);
        setSignin();
    }
    private void setSignin(){
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        work_id=(EditText) findViewById(R.id.workid);
        mail=(EditText) findViewById(R.id.mail);
        firstname=(EditText) findViewById(R.id.firstname);
        lastname=(EditText) findViewById(R.id.lastname);
        sigin=(Button) findViewById(R.id.signin);
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.v("Response:", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).toString().equals("error")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(),"Thank u for registration please wait for your approvole",Toast.LENGTH_LONG).show();
                            }
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
                    protected Map<String,String> getParams() throws AuthFailureError{
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("firstname",firstname.getText().toString());
                        hashMap.put("lastname",lastname.getText().toString());
                        hashMap.put("mail",mail.getText().toString());
                        hashMap.put("work_id",work_id.getText().toString());
                        hashMap.put("username",username.getText().toString());
                        hashMap.put("password",password.getText().toString());
                        hashMap.put("phone","5555555");
                        return hashMap;
                    }
                };
                requestQueue.add(request);
            }
        });

    }
}
