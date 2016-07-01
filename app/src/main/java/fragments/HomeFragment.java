package fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import eu.kudan.kudansamples.R;
import http_utilities.AppController;
import http_utilities.HttpHandler;
import model.eventsFeed.FeedListAdapter;
import model.eventsFeed.PathValue;
import model.pojos.Feeds;
import sessionManagement.SessionManager;

/**
 * Created by sh on 6/6/2016.
 */
public class HomeFragment  extends Fragment {

    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<Feeds> feedItems;

    private ProgressDialog progressBar;
    private EditText feedMsg;
    private ImageButton postFeed;
    private String userImage;
    private ImageButton imagePost;


    private Bitmap bitmap;
    private Uri filePath;
    //create dialog
    private Dialog dialog ;
    private ImageView postImageView;
    private LinearLayout linearLayoutPostImage;
    private Button acceptPostImage;
    private Button cancelPostImage;
    private HashMap<String, String> userFeedWithoutImage ;
    private ProgressDialog loading ;
    private View homeView;
//    feed time
    private DateFormat df ;
    private String date ;
//    session for the user
    private SessionManager userSession;
    HashMap<String, String> user;
    ImageLoader mImageLoader;
    NetworkImageView mNetworkImageView;
    private static final String TAG = "Home";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView=inflater.inflate(R.layout.activity_feeds,container,false);
        return homeView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("========", "home");
        userSession=new SessionManager(getContext());
        user=userSession.getUserDetails();
        // Get the ImageLoader through your singleton class.
        mImageLoader = AppController.getInstance(getActivity()).getImageLoader();
        // Get the NetworkImageView that will display the image.
        mNetworkImageView = (NetworkImageView) homeView.findViewById(R.id.userImage);
        listView = (ListView) homeView.findViewById(R.id.list);
        feedMsg = (EditText) homeView.findViewById(R.id.writeFeed);
        postFeed = (ImageButton) homeView.findViewById(R.id.post);
        df= new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        linearLayoutPostImage = (LinearLayout) View.inflate(getActivity(), R.layout.activity_post_image, null);
        linearLayoutPostImage.setBackgroundColor(Color.GRAY);
        // Gets a reference to the postImageView
        postImageView = (ImageView) linearLayoutPostImage.findViewById(R.id.postImageView);
        acceptPostImage = (Button) linearLayoutPostImage.findViewById(R.id.acceptPostImage);
        cancelPostImage = (Button) linearLayoutPostImage.findViewById(R.id.cancelPostImage);


        //create dialog
//        postImageView= (ImageView) findViewById(R.id.postImageView);
        dialog = new Dialog(getActivity());//for show information about place
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(linearLayoutPostImage);
//        dialog.setTitle("Confirmation");
//        dialog.getWindow().setLayout(700,700);
        userFeedWithoutImage = new HashMap<String, String>();
        loading =  new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        loading.setIndeterminate(true);
        loading.setMessage("Loading...");
        feedItems = new ArrayList<>();

        imagePost = (ImageButton) homeView.findViewById(R.id.imagePost);
        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);


        // These two lines not needed,
        // just to get the look of facebook (changing background color & hiding the icon)
//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#108070")));
//        getActionBar().setIcon(
//                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        // We first check for cached request
        Cache cache = AppController.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(HttpHandler.URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONArray(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {

//            progressBar=ProgressDialog.show(getActivity(),"Processing","Please Wait",true,false);
            loading.show();
            getGsonArray();

        }
        if (user.get("userImage")!=null){//if user already logged in
            getUserImage();

        }else{
            mNetworkImageView.setDefaultImageResId(R.drawable.user);

        }

        postFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userSession.isLoggedIn()){//check if the user login

                    String feed = feedMsg.getText().toString();
//                    user=userSession.getUserDetails();
                    //get user image
//                    getUserImage();
                    if(!feed.equals("")||bitmap!=null) {
                        final Feeds newFeed = new Feeds();
                        newFeed.setUserName(user.get("name"));
                        newFeed.setUserImage(user.get("userImage"));
                        date = df.format(Calendar.getInstance().getTime());
                        newFeed.setFeedTime(date);
                        if(!feed.equals("")&&bitmap==null){


                            newFeed.setFeed(feed);
                            Log.e("yes", "only feed");


                        }else if(bitmap!=null&&feed.equals("")){
                            newFeed.setOfflineImage(bitmap);
                            Log.e("yes","only bitmap");


                        }else{
                            newFeed.setFeed(feed);
                            newFeed.setOfflineImage(bitmap);
                            Log.e("yes","only both");


                        }
                        uploadFeedComponent();//upload post component to server
                        feedItems.add(0,newFeed);
                        listAdapter.notifyDataSetChanged();
                        feedMsg.setText("");
//					uploadPicture();
                    }else {
                    Toast.makeText(getActivity(), "please you must write something", Toast.LENGTH_LONG).show();

                }
            }else{//if the user did not login
                    Toast.makeText(getActivity(), "please you must login", Toast.LENGTH_LONG).show();
                    userSession.checkLogin();

                }
            }
        });


        //		create image multi choose
        imagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), imagePost);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.feeds_menu, popup.getMenu());

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
                    java.lang.reflect.Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.show(); //showing popup menu

            }
        });
//		handle post Image
//		accept post image

        acceptPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"accept image",Toast.LENGTH_LONG).show();

            }
        });

//		refuse post image
        cancelPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = null;
                dialog.dismiss();
                Toast.makeText(getActivity(), "cancel image", Toast.LENGTH_LONG).show();


            }
        });

    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONArray response) {
        try {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jo = response.getJSONObject(i);
//								String name = jo.getString("monumentsName");
                    Feeds item = new Feeds();


                    item.setUserName(jo.getString("userName"));
                    userImage=jo.getString("userImage");
                    item.setUserImage(userImage);

                    // Image might be null sometimes
                    String image = jo.isNull("image") ? null : jo.getString("image");
                    item.setFeedImage(image);
                    item.setFeed(jo.getString("feed"));
//					Log.e("timeeeeeeeeeeeee", jo.getString("feedTime"));
                    item.setFeedTime(jo.getString("feedTime"));

                    feedItems.add(item);

//								Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            progressBar.dismiss();
            loading.dismiss();

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





//	test

    public void getGsonArray(){
        JsonArrayRequest jreq = new JsonArrayRequest(Request.Method.GET,HttpHandler.URL_FEED,null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        parseJsonFeed(response);




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        jreq.setTag(HttpHandler.TAG);
//		MySingleton.getInstance(this).addToRequestQueue(jreq);
        AppController.getInstance(getActivity()).addToRequestQueue(jreq);



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

//						reduce size of image
                        bitmap = getResizedBitmap(bitmap,450,450);

                        //Setting the Bitmap to ImageView
                        postImageView.setImageBitmap(bitmap);
                        dialog.show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                uploadPicture();

                break;


            case HttpHandler.CAPTURE_IMAGE:

                try {


                    bitmap = (Bitmap) data.getExtras().get("data");
//					reduce size of image
                    bitmap = getResizedBitmap(bitmap,500,500);

                    postImageView.setImageBitmap(bitmap);
                    dialog.show();
                    filePath = data.getData();
                    Toast.makeText(getActivity(), filePath.toString(), Toast.LENGTH_LONG).show();
                    Log.e("URL ", "success" + filePath.toString());


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



                break;
            default:
                break;
        }
    }


    /** getResizedBitmap method is used to Resized the Image according to custom width and height
     * @param image
     * @param newHeight (new desired height)
     * @param newWidth (new desired Width)
     * @return image (new resized image)
     * */
    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }


    //	upload feed component
    public void uploadFeedComponent() {
        String path= PathValue.getPath(getActivity(), filePath);
        if(path!=null) {


//            final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
            loading.setMessage("posting...");
            loading.show();
            Ion.with(getActivity())
                    .load(HttpHandler.UPLOAD_URL).setLogging("UPLOAD LOGS", Log.DEBUG)
                    .setMultipartParameter("email", user.get("email"))
                    .setMultipartParameter("likeFeed","0")
                    .setMultipartParameter("feedTime",date)
                    .setMultipartParameter("feed", feedMsg.getText().toString())
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


        else{
            Log.i("result of imageeeeeee", "nullllll");
            uploadFeedComponentWithoutImage();





        }
    }




//    private void callback(){
//
//        Log.i("CallBack", "i was called");
//    }
    public void uploadFeedComponentWithoutImage(){
//        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
//        String date = df.format(Calendar.getInstance().getTime());

        userFeedWithoutImage.put("email", user.get("email"));
        userFeedWithoutImage.put("likeFeed", "0");
        userFeedWithoutImage.put("feedTime", date);
        userFeedWithoutImage.put("feed", feedMsg.getText().toString());




        loading.setMessage("posting...");
        loading.show();


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,HttpHandler.UPLOAD_URL_WITHOUT_IMAGE,new JSONObject(userFeedWithoutImage), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            Log.e("statttttttte",response.getString("state"));
                            loading.dismiss();
                            Toast.makeText(getActivity(), response.getString("state"), Toast.LENGTH_LONG).show();

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
        AppController.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

//    get user image
public void getUserImage(){

// Set the URL of the image that should be loaded into this view, and
    Log.e("++++++++++++", user.get("userImage"));
// specify the ImageLoader that will be used to make the request.
    mNetworkImageView.setImageUrl(HttpHandler.userImagesUrl + user.get("userImage"), mImageLoader);
//    Bitmap bitmap = ((BitmapDrawable) mNetworkImageView.getDrawable()).getBitmap();








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
