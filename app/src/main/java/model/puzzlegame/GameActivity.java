package model.puzzlegame;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import eu.kudan.kudansamples.R;


public class GameActivity extends Activity {
    /** Called when the activity is first created. */

    String activityName="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        activityName=extras.getString("activity_name");

        ImageView image=(ImageView)findViewById(R.id.TitleImage);
        if(activityName.equals("Al-Hakim")){

            setContentView(R.layout.alhakim_play);

//            image.setImageBitmap(R.drawable.alhakim_p);

        }
       else if(activityName.equals("Barquq")){

            setContentView(R.layout.barqoq_play);

//            image.setImageBitmap(R.drawable.barkok_p);

        }
        else if(activityName.equals("Qalawoun")){

            setContentView(R.layout.qalwoun_play);

//            image.setImageBitmap(R.drawable.qalwoun_p);

        }

        Button play=(Button) findViewById(R.id.PlayButton);
        play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game.class);
                intent.putExtra("activity_name",activityName);

                startActivity(intent);
            }
        });

        Button skip=(Button) findViewById(R.id.skip);
        skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WinGame.class);
                intent.putExtra("activity_name",activityName);
                startActivity(intent);

            }
        });
        
    }

}