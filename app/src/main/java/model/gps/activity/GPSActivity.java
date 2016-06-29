package model.gps.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import eu.kudan.kudansamples.R;
import model.gps.data.ARData;
import model.gps.data.GooglePlacesDataSource;
import model.gps.data.NetworkDataSource;
import model.gps.ui.Marker;

/**
 * This class extends the AugmentedReality and is designed to be an example on
 * how to extends the AugmentedReality class to show multiple data sources.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class GPSActivity extends AugmentedReality {
	
    private static final String TAG = "GPSActivity";
    private static final String locale = Locale.getDefault().getLanguage();
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);
    private static final ThreadPoolExecutor exeService = new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, queue);
    private static final Map<String, NetworkDataSource> sources = new ConcurrentHashMap<String, NetworkDataSource>();

//    private static Toast myToast = null;
//    private static VerticalTextView text = null;

    //alert dailog
    AlertDialog.Builder builder;
    Dialog dialog ;
//    TextView placeInfo;

    // draw google map
    final int RQS_GooglePlayServices = 1;
    private GoogleMap myMap;
    double src_lat ;
    double src_long ;
    double dest_lat;
    double dest_long;
    MarkerOptions markerOptions;
    com.google.android.gms.maps.model.Marker srcMarker=null;
    com.google.android.gms.maps.model.Marker destMarker=null;

    //make destination icon from resourse
//    BitmapDescriptor dsetIcon;

    //to check gps is enabled or not
    LocationManager manager;


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Create toast
//        myToast = new Toast(getApplicationContext());
//        myToast.setGravity(Gravity.CENTER, 0, 0);
//        // Creating our custom text view, and setting text/rotation
//        text = new VerticalTextView(getApplicationContext());
//        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        text.setLayoutParams(params);
//        text.setBackgroundResource(android.R.drawable.toast_frame);
//        text.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Small);
//        text.setShadowLayer(2.75f, 0f, 0f, Color.parseColor("#BB000000"));
//        myToast.setView(text);
//        // Setting duration and displaying the toast
//        myToast.setDuration(Toast.LENGTH_SHORT);

        //create dialog
        dialog = new Dialog(this);//for show information about place
        dialog.setContentView(R.layout.activity_maps);
        dialog.setTitle("Dstination");
        dialog.getWindow().setLayout(900,700);


//        to set name inside it
//        placeInfo = (TextView) dialog.findViewById(R.id.placeInfo);
        /////////
//        builder = new AlertDialog.Builder(GPSActivity.this);//for alert the user to enable gps and internet connection
////        builder.setMessage("Please Check Your Connection.");
//        builder.setMessage("Please Enable GPS and Internet Connection.....")
//                .setTitle("Please Check Your Connection.");
//        builder.setCancelable(true);
//        builder.setPositiveButton(
//                "Setting",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
////                        dialog.cancel();
//                        Intent settingIntent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//
//                        startActivity(settingIntent);
//                        dialog.dismiss();
//                    }
//                });
//
//        builder.setNegativeButton(
//                "Cancl",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
////                        dialog.cancel();
//                        dialog.dismiss();
//                    }
//                });

        //end of


        // Local data
//        LocalDataSource localData = new LocalDataSource(this.getResources());
//        ARData.addMarkers(localData.getMarkers());

//        NetworkDataSource wikipedia = new WikipediaDataSource(this.getResources());
//        sources.put("wiki", wikipedia);
        NetworkDataSource googlePlaces = new GooglePlacesDataSource(this.getResources());
        sources.put("googlePlaces", googlePlaces);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();

        if(haveNetworkConnection()) {//check is there connection or not
            manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {//check if gps is enabled or not
                buildAlertMessageNoGps();


            }else {


                Location last = ARData.getCurrentLocation();
                //my last know location
                src_lat = last.getLatitude();
                src_long = last.getLongitude();

                updateData(last.getLatitude(), last.getLongitude(), last.getAltitude());
            }
        }else{
//            Toast.makeText(getApplicationContext(),"please check your connection",Toast.LENGTH_LONG).show();
//            AlertDialog alert = builder.create();
//            alert.show();
            showAlert("please check your connection");
        }
    }


    /**
     * {@inheritDoc}
     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.v(TAG, "onOptionsItemSelected() item=" + item);
//        switch (item.getItemId()) {
//            case R.id.showRadar:
//                showRadar = !showRadar;
//                item.setTitle(((showRadar) ? "Hide" : "Show") + " Radar");
//                break;
//            case R.id.showZoomBar:
//                showZoomBar = !showZoomBar;
//                item.setTitle(((showZoomBar) ? "Hide" : "Show") + " Zoom Bar");
//                zoomLayout.setVisibility((showZoomBar) ? LinearLayout.VISIBLE : LinearLayout.GONE);
//                break;
//            case R.id.exit:
//                finish();
//                break;
//        }
//        return true;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        //my last know location
        src_lat=location.getLatitude();
        src_long=location.getLongitude();

        updateData(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void markerTouched(final Marker marker) {
//        text.setText(marker.getName());
//        myToast.show();
        /**
         * ayad
         * create pop up dialog to show information of the place which pressed on it
         */


//        if(marker.getSummary()==null){//for google places information
//            placeInfo.setText(marker.getName());
//
//
//        }else{//for wiki information
//            placeInfo.setText(marker.getSummary());
//
//        }
//
//
//        dialog.show();


//        try to make show a map that show the road from current location to marker touch place


        dialog.show();
        //get dstination long and lat
        dest_lat=marker.getLatitude();
        dest_long=marker.getLongitude();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.e("srccccccccccc","lat "+src_lat+" long "+src_long);
                Log.e("desssssssss","lat "+marker.getLatitude()+" long "+marker.getLongitude());
                Log.e("titleeeeeeee",marker.getName());

                //to remove previouse marker and re draw new marker
//                if (srcMarker != null) {
//                    srcMarker.remove();
//                }
//                if (destMarker != null) {
//                    destMarker.remove();
//                }


                googleMap.clear();



                myMap = googleMap;
                LatLng srcLatLng = new LatLng(src_lat, src_long);
                LatLng destLatLng = new LatLng(dest_lat, dest_long);

                srcMarker=myMap.addMarker(new MarkerOptions()
                        .position(srcLatLng).title("you are here!!!!"));

                myMap.animateCamera(CameraUpdateFactory.newLatLng(srcLatLng));


//                dsetIcon = BitmapDescriptorFactory.fromResource(R.drawable.dest);
                destMarker=myMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(destLatLng).title(marker.getName()));

                // Enabling MyLocation in Google Map
                myMap.setMyLocationEnabled(true);
                myMap.getUiSettings().setZoomControlsEnabled(true);
                myMap.getUiSettings().setCompassEnabled(true);
                myMap.getUiSettings().setMyLocationButtonEnabled(true);
                myMap.getUiSettings().setAllGesturesEnabled(true);
                myMap.setTrafficEnabled(true);
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(srcLatLng, 12));
                markerOptions = new MarkerOptions();


                // Polyline line = myMap.addPolyline(new PolylineOptions().add(srcLatLng, destLatLng).width(5).color(Color.RED));



            }
        });



        connectAsyncTask _connectAsyncTask = new connectAsyncTask();
        _connectAsyncTask.execute();



        //////////////////////////////////////
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateDataOnZoom() {
        super.updateDataOnZoom();
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(), last.getLongitude(), last.getAltitude());
    }

    private void updateData(final double lat, final double lon, final double alt) {
        try {
            exeService.execute(new Runnable() {
                @Override
                public void run() {
                    for (NetworkDataSource source : sources.values())
                        download(source, lat, lon, alt);
                }
            });
        } catch (RejectedExecutionException rej) {
            Log.w(TAG, "Not running new download Runnable, queue is full.");
        } catch (Exception e) {
            Log.e(TAG, "Exception running download Runnable.", e);
        }
    }

    private static boolean download(NetworkDataSource source, double lat, double lon, double alt) {
        if (source == null) return false;

        String url = null;
        try {
            url = source.createRequestURL(lat, lon, alt, ARData.getRadius(), locale);
        } catch (NullPointerException e) {
            return false;
        }

        List<Marker> markers = null;
        try {
            markers = source.parse(url);
        } catch (NullPointerException e) {
            return false;
        }

        ARData.addMarkers(markers);
        return true;
    }
    /**
     * author ayad
     * method to check is there internet connection or not
     */
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }

        if(haveConnectedWifi) {
            return true;
        }
        else if(haveConnectedMobile) {
            return true;
        }else{
            return false;
        }

//        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * author ayad
     * method to check is there gps or not
     */

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    //async task to get path between two markers on the map

    private class connectAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(GPSActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            fetchData();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(doc!=null){
                NodeList _nodelist = doc.getElementsByTagName("status");
                Node node1 = _nodelist.item(0);
                String _status1 = node1.getChildNodes().item(0).getNodeValue();
                if(_status1.equalsIgnoreCase("OK")){
                    NodeList _nodelist_path = doc.getElementsByTagName("overview_polyline");
                    Node node_path = _nodelist_path.item(0);
                    Element _status_path = (Element)node_path;
                    NodeList _nodelist_destination_path = _status_path.getElementsByTagName("points");
                    Node _nodelist_dest = _nodelist_destination_path.item(0);
                    String _path = _nodelist_dest.getChildNodes().item(0).getNodeValue();
                    List<LatLng> directionPoint = decodePoly(_path);

                    PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);
                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    // Adding route on the map
                    myMap.addPolyline(rectLine);
                    markerOptions.position(new LatLng(dest_lat, dest_long));
                    markerOptions.draggable(true);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    myMap.addMarker(markerOptions);
                }else{
                    showAlert("Unable to find the route");
                }


            }else{
                showAlert("Unable to find the route");
            }

            progressDialog.dismiss();

        }

    }

    Document doc = null;
    private void fetchData()
    {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.google.com/maps/api/directions/xml?origin=");
        urlString.append(src_lat);
        urlString.append(",");
        urlString.append(src_long);
        urlString.append("&destination=");//to
        urlString.append(dest_lat);
        urlString.append(",");
        urlString.append(dest_long);
        urlString.append("&sensor=true&mode=driving");
        Log.d("url","::"+urlString.toString());
        HttpURLConnection urlConnection= null;
        URL url = null;
        try
        {
            url = new URL(urlString.toString());
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = (Document) db.parse(urlConnection.getInputStream());//Util.XMLfromString(response);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void showAlert(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(GPSActivity.this);
        alert.setTitle("Error");
        alert.setCancelable(false);
        alert.setMessage(message);
        alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        alert.show();
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
    /////////////////////////////////
}
