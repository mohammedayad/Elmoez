package model.userData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import eu.kudan.kudansamples.R;
import http_utilities.MySingletonVolley;
import model.eventsFeed.PathValue;

public class Profile extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private static final String TAG = "Profile";
    final static String UPLOAD_URL="http://192.168.43.175:8084/lastformaven/services/file/upload";


    final static  int PICK_IMAGE = 209;
    final static  int CAPTURE_IMAGE = 208;

    private Bitmap bitmap;
    private Uri filePath;
    private ImageView imageView;

    HashMap<String,String> email= new HashMap<String, String>();

    ;
    public static final String ip_address="http://192.168.43.175:8084";

    public static final String REMOVE_IMAGE_URL = ip_address+"/ElmoezWebService/services/elmoez/removeimage";

    public static final String image="http://192.168.43.175:8084//ElmoezWebService/services/elmoez/removeimage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView name = (TextView) findViewById(R.id.user_profile_name);
        imageView =(ImageView)findViewById(R.id.user_image);

        email.put("email","email");
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Profile.this, fab);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){

                            case R.id.Camera:
                                openCamera();

                                break;
                            case R.id.Gallery:

                                showFileChooser();
                                break;
                            case R.id.Remove:
                                removeImage();

                                Toast.makeText(Profile.this, "You Clicked : " + "threeee", Toast.LENGTH_SHORT).show();
                                break;


                        }
//
//                        Toast.makeText(Profile.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                try {
                    Class<?> classPopupMenu = Class.forName(popup.getClass().getName());
                    Field mPopup = classPopupMenu.getDeclaredField("mPopup");
                    mPopup.setAccessible(true);
                    Object menuPopupHelper = mPopup.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.show(); //showing popup menu

            }
        });

        ImageButton editBtn=(ImageButton)findViewById(R.id.edit);


        editBtn.setOnClickListener(new View.OnClickListener() {


           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), UserNameEdit.class);
               startActivity(intent);
           }
       });

    }

    private void removeImage() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        uploadUserDataToServer();
//
                    }
                }, 3000);
    }


    public void uploadUserDataToServer(){


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,REMOVE_IMAGE_URL,new JSONObject(email), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            Log.e("statttttttte",response.getString("state"));
                            if(response.getString("state").equals("registeredSuccessfully")){


//                                progressDialog.dismiss();


                            }else{
//                                progressDialog.dismiss();
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

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAPTURE_IMAGE);

    }


    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){

            case PICK_IMAGE:
                if(resultCode==RESULT_OK){

                    filePath = data.getData();
                    Toast.makeText(Profile.this, filePath.toString(), Toast.LENGTH_LONG).show();
                    Log.e("URL ", "success" + filePath.toString());
                    try {
                        //Getting the Bitmap from Gallery
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        //Setting the Bitmap to ImageView
                        imageView.setImageBitmap(bitmap);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                uploadPicture();

                break;


            case CAPTURE_IMAGE:

                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    filePath = data.getData();
                    Toast.makeText(Profile.this, filePath.toString(), Toast.LENGTH_LONG).show();
                    Log.e("URL ", "success" + filePath.toString());

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                uploadPicture();

                break;
            default:
                break;
        }
    }




    public void uploadPicture() {
        String path= PathValue.getPath(getApplicationContext(), filePath);
        if(path!=null) {

            final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
            Ion.with(getApplicationContext())
                    .load(UPLOAD_URL).setLogging("UPLOAD LOGS", Log.DEBUG)
                    .setMultipartParameter("email", "dodo@gmail.com")
                    .setMultipartFile("file", "application/zip", new File(path)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (e != null) {
                        e.printStackTrace();
                    }
                    loading.dismiss();
                    Log.i("completed", "completed");

                    if (result != null) {

                        Log.i("success", result.get("message").getAsString());

                        loading.dismiss();

                    }
                }
            });
        }
    }

    private void callback(){

        Log.i("CallBack", "i was called");
    }

}
