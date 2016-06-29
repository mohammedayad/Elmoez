package model.userData;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import eu.kudan.kudansamples.R;
import http_utilities.HttpHandler;
import http_utilities.MySingletonVolley;

public class Signup extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private HashMap<String, String> signUpParams ;
    private ProgressDialog progressDialog;

    @InjectView(R.id.input_fName) EditText _firstNameText;
    @InjectView(R.id.input_lName) EditText _lastNameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        signUpParams = new HashMap<String, String>();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(Signup.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        //initalize hash map with user data
        signUpParams.put("firstName", firstName);
        signUpParams.put("lastName", lastName);
        signUpParams.put("email",email);
        signUpParams.put("password",password);
//        signUpParams.put("image","ayad.jpg");
//        uploadUserDataToServer();



        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        uploadUserDataToServer();
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Thank you for your Register", Toast.LENGTH_LONG).show();

//        setResult(RESULT_OK, null);
//        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            _firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lastNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void uploadUserDataToServer(){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, HttpHandler.SIGNUP_URL,new JSONObject(signUpParams), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            Log.e("statttttttte",response.getString("state"));
                            if(response.getString("state").equals("registeredSuccessfully")){


                                progressDialog.dismiss();
                                onSignupSuccess();

                            }else{
                                progressDialog.dismiss();
                                onSignupFailed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
    @Override
    protected void onStop() {
        super.onStop();
        MySingletonVolley.getInstance(this).getRequestQueue().cancelAll(TAG);
        Log.i("inffffffffffffffffffformation", "request canceled");

    }
}
