package fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.Random;

import eu.kudan.kudansamples.R;
import model.puzzlegame.GameActivity;
import model.stories.DraggableGridView;
import model.stories.OnRearrangeListener;

/**
 * Created by sh on 6/6/2016.
 */
public class StoryFragment  extends Fragment {


    static Random random = new Random();
//	static String[] words = "the of and a to in is be that was he for it with as his I on have at by not they this had are but from or she an which you one we all were her would there their will when who him been has more if no out do so can what up said about other into than its time only could new them man some these then two first may any like now my such make over our even most me state after also made many did must before back see through way where get much go well your know should down work year because come people just say each those take day good how long Mr own too little use US very great still men here life both between old under last never place same another think house while high right might came off find states since used give against three himself look few general hand school part small American home during number again Mrs around thought went without however govern don't does got public United point end become head once course fact upon need system set every war put form water took".split(" ");
//	static String[] words = "the of and a to in is be that was he for it with as his I on have at by not they this had are but from or she an which you one we all were her would there their will when who him been has more if no out do so can what up said about other into than its time only could new them man some these then two first may any like now my such make over our even most me state after also made many did must before back see through way where get much go well your know should down work year because come people just say each those take day good how long Mr own too little use US very great still men here life both between old under last never place same another think house while high right might came off find states since used give against three himself look few general hand school part small American home during number again Mrs around thought went without however govern don't does got public United point end become head once course fact upon need system set every war put form water took".split(" ");

    public static final String AL_Hakim="Al-Hakim";
//    public static final String Aqmar="Aqmar Mosque";
    public static final String Barquq="Barquq";
    public static final String Qalawoun="Qalawoun";
//    public static final String Selehdar="Soliman Agha Selehdar Mosque";


    static String[] words={AL_Hakim,Barquq,Qalawoun};
    DraggableGridView dgv;

    String activityName;
    View story;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        story=inflater.inflate(R.layout.cards,container,false);


        return story;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dgv = (DraggableGridView) story.findViewById(R.id.vgv);



        createViews();

        setListeners();
    }
    private void setListeners()
    {
        dgv.setOnRearrangeListener(new OnRearrangeListener() {
            public void onRearrange(int oldIndex, int newIndex) {
//				String word = poem.remove(oldIndex);
//				if (oldIndex < newIndex)
//					poem.add(newIndex, word);
//				else
//					poem.add(newIndex, word);
            }
        });
        dgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {



                switch (words[arg2]){

                    case AL_Hakim:
                        activityName=AL_Hakim;
                        break;


                    case Barquq:

                        activityName=Barquq;


                        break;

                    case Qalawoun:
                        activityName=Qalawoun;


                        break;


                }


                Intent intent=new Intent(getActivity(),GameActivity.class);
                intent.putExtra("activity_name",activityName);

                startActivity(intent);



//				dgv.removeViewAt(arg2);
//				poem.remove(arg2);




            }
        });

    }

    private Bitmap getThumb(String s)
    {
//        Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
        Resources res = getResources();
        Bitmap bitmap=null;
        if (s.equals("Al-Hakim")){
            bitmap = BitmapFactory.decodeResource(res, R.drawable.alhakim1);


        }else if(s.equals("Barquq")){
            bitmap = BitmapFactory.decodeResource(res, R.drawable.barqoq5);


        }else{
            bitmap = BitmapFactory.decodeResource(res, R.drawable.qalwoun5);

        }
//        Then make the bitmap mutable and create a canvas over it:

        Canvas canvas = new Canvas(bitmap.copy(Bitmap.Config.ARGB_8888, true));
//        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();

        paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128), random.nextInt(128)));
        paint.setTextSize(18);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        canvas.drawRect(new Rect(0, 0, 200, 200), paint);




        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(s, 75, 75, paint);

        return bitmap;
    }

    public void createViews(){

        for(int i=0;i<words.length;i++){

            String word = words[i];
            ImageView view = new ImageView(getActivity());
            view.setImageBitmap(getThumb(word));
            dgv.addView(view);
//		poem.add(word);

        }
    }


}
