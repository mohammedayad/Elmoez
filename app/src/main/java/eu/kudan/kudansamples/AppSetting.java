package eu.kudan.kudansamples;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class AppSetting extends AppCompatActivity {

    SeekBar mySeekBarForFont;
    Button saveButton;
    SharedPreferences settingPreference;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mySeekBarForFont=(SeekBar)findViewById(R.id.seekBar1);

        settingPreference=getSharedPreferences("dataOfApp",MODE_PRIVATE);
        mySeekBarForFont.setProgress((int)settingPreference.getFloat("fontSize",0));

        saveButton=(Button)findViewById(R.id.button1);


        mySeekBarForFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settingPreference = getSharedPreferences("dataOfApp", MODE_PRIVATE);
                mEditor = settingPreference.edit();
                mEditor.putFloat("fontSize", mySeekBarForFont.getProgress());
                mEditor.commit();
                onBackPressed();

            }
        });







    }

}
