package eu.kudan.kudansamples;

import android.os.Bundle;
import android.util.Log;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARVideoNode;
import eu.kudan.kudan.ARVideoTexture;

/**
 * Created by 3yad on 28/06/2016.
 */
public class ARKudanElmoez extends ARActivity implements ARImageTrackableListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // set api key for this package name.
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("GAWAE-FBVCC-XA8ST-GQVZV-93PQB-X7SBD-P6V4W-6RS9C-CQRLH-78YEU-385XP-T6MCG-2CNWB-YK8SR-8UUQ");
//        setContentView(R.layout.activity_main);

    }

    public void setup() {
        // create a trackable from a bundled image.
        ARImageTrackable wavesTrackable = new ARImageTrackable("waves");
        wavesTrackable.loadFromAsset("hakim.jpg");


        // create video texture.
        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromAsset("hakim.mp4");
        ARVideoNode videoNode = new ARVideoNode(videoTexture);

        // add video to the waves trackable.
        wavesTrackable.getWorld().addChild(videoNode);

        // load a set of trackables from a bundled file.
//        ARTrackableSet trackableSet = new ARTrackableSet();
//        trackableSet.loadFromAsset("demo.KARMarker");

        ARImageTracker tracker = ARImageTracker.getInstance();

        // add our trackables to the tracker.
//        tracker.addTrackableSet(trackableSet);
        wavesTrackable.addListener(this);
        tracker.addTrackable(wavesTrackable);

        // create an image node.
//        ARImageTrackable legoTrackable = tracker.findTrackable("lego");
//        ARImageNode imageNode = new ARImageNode("BatmanLegoMovie.png");
//
//        // make it smaller.
//        imageNode.scaleBy(0.5f, 0.5f, 0.5f);
//
//        // add it to the lego trackable.
//        legoTrackable.getWorld().addChild(imageNode);
    }

    @Override
    public void didDetect(ARImageTrackable trackable) {
        Log.i("+++++++++++++++++++++++", "didDetect tracked");

    }


    @Override
    public void didTrack(ARImageTrackable trackable) {
        Log.i("+++++++++++++++++++++++", "didTracktracked");
    }

    @Override
    public void didLose(ARImageTrackable trackable) {
        Log.i("+++++++++++++++++++++++", "lost " + trackable.getName());

    }

}
