package model.puzzlegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import eu.kudan.kudansamples.R;
import eu.kudan.kudansamples.AppSetting;


public class WinGame extends AppCompatActivity {

    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;
    String activityName="";
    SharedPreferences settingPreference;
    TextView storyText;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i=new Intent(this,AppSetting.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onResume (){
        super.onResume();

        settingPreference=getSharedPreferences("dataOfApp",MODE_PRIVATE);


        storyText.setTextSize(settingPreference.getFloat("fontSize", 0) + 20);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        activityName=extras.getString("activity_name");


        if(activityName.equals("Al-Hakim")){

            setContentView(R.layout.alhakim_info);
           storyText=(TextView)findViewById(R.id.storyText);
            storyText.setText("\n" +
                    "990-1013 AD\\ 380-403 AH\n"+
                    "Fatimid\n" +
                    "\n" +
                            "The mosque was built by al-Hakim who was one of the most controversial figures of the history of Egypt. It is large a hypostyle mosque with open courtyard surrounded with arcaded bays.");

        }
        else if(activityName.equals("Barquq")){

            setContentView(R.layout.barqoq_info);
            TextView storyText=(TextView)findViewById(R.id.storyText);
            storyText.setText("\n" +
                    "1384-1386 AD\\ 786-788 AH\n" +
                    "Circassian Mamluk\n" +
                    "\n" +
                    "The founder Sultan al-Zahir Barquq was the first Circassian Mamluk Sultan, but the complex follows the style of the earlier Bahri Mamluks and clearly imitates Madrasa of Sultan Hasan.\n");

        }
        else if(activityName.equals("Qalawoun")){

            setContentView(R.layout.qalwoun_info);

            TextView storyText=(TextView)findViewById(R.id.storyText);
            storyText.setText("\n" +
                    "1284-85 AD\\ 683-684 AH\n" +
                    "Bahri Mamluk\n" +
                    "\n" +
                    "This massive complex was built by Sultan al-Mansur Qalawun. It included a maristan (hospital), the first to be built in Cairo and the most lavish and impressive of its time. \n");
        }


        viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);



        fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
//sets auto flipping
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.startFlipping();



    }
}
