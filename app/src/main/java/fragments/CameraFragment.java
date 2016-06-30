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

import eu.kudan.kudansamples.ARKudanElmoez;
import eu.kudan.kudansamples.R;
import model.camera.ImageTargets;

/**
 * Created by sh on 6/6/2016.
 */
public class CameraFragment extends Fragment {

    private View cameraView;
    private Button startCameraActivity;
    private Button startVideoActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("cameraView", "called");
        cameraView=inflater.inflate(R.layout.activity_camera,container,false);

        return cameraView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startCameraActivity=(Button)cameraView.findViewById(R.id.startCamera);
        startVideoActivity=(Button)cameraView.findViewById(R.id.startVideo);

        //when click on startCameraActivity button will open gps activity
        startCameraActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ImageTargets.class);
                startActivity(intent);

            }
        });
        //when click on startCameraActivity button will open camera activity
        startVideoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ARKudanElmoez.class);
                startActivity(intent);

            }
        });
    }


}
