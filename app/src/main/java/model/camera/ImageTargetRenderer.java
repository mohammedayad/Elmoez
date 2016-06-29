package model.camera;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.vuforia.Renderer;
import com.vuforia.State;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Christena on 5/13/2016.
 */
public class ImageTargetRenderer extends Activity implements GLSurfaceView.Renderer
{
    private static final String LOGTAG = "ImageTargetRenderer";
    public static Handler mainActivityHandler;
    private SampleApplicationSession vuforiaAppSession;
    private ImageTargets mActivity;
    private int shaderProgramID;
    private float kBuildingScale = 12.0f;
   // private SampleApplication3DModel mBuildingsModel;

    private Renderer mRenderer;
    boolean mIsActive = false;
    private static final float OBJECT_SCALE_FLOAT = 3.0f;

    public ImageTargetRenderer(ImageTargets activity,SampleApplicationSession session)
    {
        mActivity = activity;
        vuforiaAppSession = session;
    }


    // Called when the surface is created or recreated.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        Log.d(LOGTAG, "GLRenderer.onSurfaceCreated");

        initRendering();

        // Call Vuforia function to (re)initialize rendering after first use
        // or after OpenGL ES context was lost (e.g. after onPause/onResume):
        vuforiaAppSession.onSurfaceCreated();
    }


    // Called when the surface changed size.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        Log.d(LOGTAG, "GLRenderer.onSurfaceChanged");

        // Call Vuforia function to handle render surface size changes:
        vuforiaAppSession.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mIsActive)
            return;

        // Call our function to render content
        renderFrame();
    }

    private void renderFrame()
    {

        State state = mRenderer.begin();
        mRenderer.drawVideoBackground();


        // handle face culling, we need to detect if we are using reflection
        // to determine the direction of the culling



        // did we find any trackables this frame?
        if(state.getNumTrackableResults()>0){
            for (int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++)
            {
                TrackableResult result = state.getTrackableResult(tIdx);
                Trackable trackable = result.getTrackable();
                displayMessage(trackable.getName());

                printUserData(trackable);

            }
        }
        else{

            displayMessage("gone");

        }

        mRenderer.end();
    }

    public void displayMessage(String text)
    {
        // We use a handler because this thread cannot
        // change the UI
        Message message = new Message();
        message.obj = text;

        Log.e("koooooooooooooooki",text);
        mainActivityHandler.sendMessage(message);
    }


    // Function for initializing the renderer.
    private void initRendering()
    {
        mRenderer = Renderer.getInstance();
        // Hide the Loading Dialog
        mActivity.loadingDialogHandler.sendEmptyMessage(LoadingDialogHandler.HIDE_LOADING_DIALOG);

    }




    private void printUserData(Trackable trackable)
    {
        String userData = (String) trackable.getUserData();
        Log.d(LOGTAG, "UserData:Retreived User Data	\"" + userData + "\"");

    }



}
