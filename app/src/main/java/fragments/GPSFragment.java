package fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import eu.kudan.kudansamples.R;
import model.gps.activity.GPSActivity;


/**
 * Created by sh on 6/6/2016.
 */
public class GPSFragment extends Fragment {
    View gpsView;
    Button startGPSActivity;
    TextView GPS_info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("gpsView","called");
        gpsView=inflater.inflate(R.layout.activity_gps,container,false);

        return gpsView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startGPSActivity=(Button)gpsView.findViewById(R.id.startGPS);
        GPS_info = (TextView) gpsView.findViewById(R.id.view_gps);
        GPS_info.setText("We will help you to find each place you search about");
        //when click on startGPSActivity button will open gps activity
        startGPSActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GPSActivity.class);
                startActivity(intent);


            }
        });
    }
}
