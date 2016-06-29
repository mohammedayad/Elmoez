package model.userData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import eu.kudan.kudansamples.MainActivity;
import eu.kudan.kudansamples.R;
import http_utilities.HttpHandler;
import http_utilities.MySingletonVolley;
import sessionManagement.SessionManager;

public class UserNameEdit extends AppCompatActivity {


    private static final String TAG = "Profile";
    private HashMap<String, String> editUserName = new HashMap<String, String>();
    @InjectView(R.id.firstName)
    EditText firstName;
    @InjectView(R.id.lastName) EditText lastName;
    private ProgressDialog loading ;
    private SessionManager userSession;
    private HashMap<String, String> user;
    private String firsr_name;
    private String last_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        userSession=new SessionManager(this);
        user=userSession.getUserDetails();
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loading =  new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        loading.setIndeterminate(true);
        loading.setMessage("Loading...");

        Button button=(Button)findViewById(R.id.edit_name);
        button.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view) {




//                new android.os.Handler().postDelayed(
//                        new Runnable() {
//                            public void run() {
                                // On complete call either onSignupSuccess or onSignupFailed
                                // depending on success
                                authenticateUser();//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                            }
//                        }, 3000);


            }
        });

    }

    public void authenticateUser(){
        loading.show();
        firsr_name=firstName.getText().toString();

        last_name=lastName.getText().toString();
        editUserName.put("firstName", firsr_name);
        editUserName.put("lastName", last_name);
        editUserName.put("email", user.get("email"));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, HttpHandler.EditName_URL,new JSONObject(editUserName), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("eeeeee", response.getString("state"));
                    userSession.createLoginSession(firsr_name + " " + last_name, user.get("email"), user.get("userImage"));
                    loading.dismiss();
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_LONG);
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("errrrrrrrrrror",error.getMessage());

            }
        });

        jsObjRequest.setTag(TAG);
        MySingletonVolley.getInstance(this).addToRequestQueue(jsObjRequest);
    }


}
