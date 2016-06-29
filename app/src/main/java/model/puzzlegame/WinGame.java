package model.puzzlegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import eu.kudan.kudansamples.R;


public class WinGame extends AppCompatActivity {

    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;
String activityName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        activityName=extras.getString("activity_name");


        if(activityName.equals("Al-Hakim")){

            setContentView(R.layout.alhakim_info);
            TextView storyText=(TextView)findViewById(R.id.storyText);
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
