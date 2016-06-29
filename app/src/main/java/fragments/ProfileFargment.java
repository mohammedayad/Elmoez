package fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
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
import http_utilities.AppController;
import http_utilities.HttpHandler;
import model.eventsFeed.PathValue;
import model.userData.UserNameEdit;
import sessionManagement.SessionManager;

/**
 * Created by sh on 6/6/2016.
 */
public class ProfileFargment  extends Fragment {
    private View profileView;;
    private TextView name;
    private ImageView imageView;
    private HashMap<String,String> email;
    private FloatingActionButton fab;
    private ImageButton editBtn;
    private ProgressDialog progressDialog;
    private static final String TAG = "Profile";
    private Bitmap bitmap;
    private Uri filePath;
    private SessionManager userSession;
    private HashMap<String, String> user;
    private NetworkImageView mNetworkImageView;
    private Bitmap userImageBitmap;
    private TextView userEmail;
    private Button userSignOut;
    private CardView cardViewLayout;
    private ViewPager viewPager ;
    private ProgressDialog loading ;
    private Bitmap myImage;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("gpsView", "called");
        profileView=inflater.inflate(R.layout.activity_profile,container,false);

        return profileView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userSession=new SessionManager(getContext());
        if(userSession.isLoggedIn()){
            name = (TextView) profileView.findViewById(R.id.user_profile_name);
            imageView =(ImageView)profileView.findViewById(R.id.user_image);
            userEmail =(TextView)profileView.findViewById(R.id.userEmail);
            userSignOut =(Button)profileView.findViewById(R.id.userSignOut);
            viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
            user=userSession.getUserDetails();
            loading =  new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            loading.setIndeterminate(true);
            loading.setMessage("Loading...");
//            email= new HashMap<String, String>();
//            email.put("email",user.get("email"));
            fab = (FloatingActionButton) profileView.findViewById(R.id.fab);
            editBtn=(ImageButton)profileView.findViewById(R.id.edit);
//            cardViewLayout = (CardView) View.inflate(getActivity(), R.layout.activity_feeds, null);
//            mNetworkImageView = (NetworkImageView) cardViewLayout.findViewById(R.id.userImage);
//            userImageBitmap = ((BitmapDrawable) mNetworkImageView.getDrawable()).getBitmap();
//            if (myImage==null) {
                getUserImage();
//            }else{
//                imageView.setImageBitmap(myImage);
//
//            }
            name.setText(user.get("name"));
            userEmail.setText(user.get("email"));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(getActivity(), fab);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.Camera:
                                    openCamera();

                                    break;
                                case R.id.Gallery:

                                    showFileChooser();
                                    break;
                                case R.id.Remove:
                                    removeImage();

                                    Toast.makeText(getActivity(), "You Clicked : " + "threeee", Toast.LENGTH_SHORT).show();
                                    break;


                            }
//
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





        editBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserNameEdit.class);
                intent.putExtra("email", user.get("email"));
                startActivity(intent);
            }
        });

            userSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("=========", "user logged out");
                    userSession.logoutUser();
                    viewPager.setCurrentItem(0);


                }
            });


        }
        else{
            userSession.checkLogin();
        }


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

        loading.setMessage("removing.....");
        loading.show();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,HttpHandler.REMOVE_IMAGE_URL,new JSONObject(user), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            Log.e("statttttttte",response.getString("state"));
                            if(response.getString("state").equals("registeredSuccessfully")){


                                loading.dismiss();


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
        AppController.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, HttpHandler.CAPTURE_IMAGE);

    }


    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), HttpHandler.PICK_IMAGE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){

            case HttpHandler.PICK_IMAGE:
                if(resultCode== Activity.RESULT_OK){

                    filePath = data.getData();
                    Toast.makeText(getActivity(), filePath.toString(), Toast.LENGTH_LONG).show();
                    Log.e("URL ", "success" + filePath.toString());
                    try {
                        //Getting the Bitmap from Gallery
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                        //Setting the Bitmap to ImageView
                        imageView.setImageBitmap(bitmap);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                uploadPicture();

                break;


            case HttpHandler.CAPTURE_IMAGE:

                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    filePath = data.getData();
                    Toast.makeText(getActivity(), filePath.toString(), Toast.LENGTH_LONG).show();
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
        loading.setMessage("uploading.....");
        loading.show();
        String path= PathValue.getPath(getContext(), filePath);
        final String imageName = path.substring(path.lastIndexOf("/") + 1, path.length());


        if(path!=null) {

//            final ProgressDialog loading = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);
            Ion.with(getContext())
                    .load(HttpHandler.UPLOAD_PROFILE_PICTURE).setLogging("UPLOAD LOGS", Log.DEBUG)
                    .setMultipartParameter("email", user.get("email"))
                    .setMultipartFile("file", "application/zip", new File(path)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    Log.e("=======",imageName);
                    userSession.createLoginSession(user.get("name"),user.get("email"),imageName);
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

    public void getUserImage(){


// Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(HttpHandler.userImagesUrl + user.get("userImage"),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        myImage=bitmap;
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.user);
                    }
                });



        request.setTag(TAG);
// Access the RequestQueue through your singleton class.
        AppController.getInstance(getContext()).addToRequestQueue(request);



    }

    @Override
    public void onStop() {
        super.onStop();
//        AppController.getInstance(getContext()).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
//            @Override
//            public boolean apply(Request<?> request) {
//                Log.d("DEBUG", "request running: " + request.getTag().toString());
//                return true;
//            }
//        });

    }


}
