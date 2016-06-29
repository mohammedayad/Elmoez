package model.puzzlegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;

import eu.kudan.kudansamples.MainActivity;
import eu.kudan.kudansamples.R;


@SuppressWarnings("deprecation")
public class Game extends Activity {

	int first=50;
	String activityName="";
	int second=255;
	int last=460;
//	private TextView moveCounter;
// 	private TextView feedbackText;
	Button skip;
 	private Button[] buttons;
    private Boolean bad_move=false;
   	private static final Integer[] goal = new Integer[] {0,1,2,3,4,5,6,7,8};
   	
	private ArrayList<Integer> cells = new ArrayList<Integer>();
    @Override
    public void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		activityName=extras.getString("activity_name");


		if(activityName.equals("Al-Hakim")){

			setContentView(R.layout.alhakim_game);


		}
		else if(activityName.equals("Barquq")){

			setContentView(R.layout.barqoq_game);


		}
		else if(activityName.equals("Qalawoun")){

			setContentView(R.layout.qalwoun_game);


		}
		skip=(Button)findViewById(R.id.skipButton);
		skip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(), WinGame.class);
				intent.putExtra("activity_name",activityName);
				startActivity(intent);


			}
		});

		buttons=findButtons();
       
        for(int i=0;i<9;i++)
        {
            this.cells.add(i);
        }
        Collections.shuffle(this.cells); //random cells array
        
        fill_grid();
       
        
     //  moveCounter = (TextView) findViewById(R.id.MoveCounter);
//	   feedbackText = (TextView) findViewById(R.id.FeedbackText);

		//moveCounter.setText("0");
//		feedbackText.setText(R.string.game_feedback_text);

		for (int i = 1; i < 9; i++) {
			buttons[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					makeMove((Button) v);

//					if(feedbackText.getText().toString().equals("we have a winner")){
//
//
//						Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//						startActivity(intent);
//
//
//					}
				}
			});
		}
		
		

    
}
    public Button[] findButtons() {
		Button[] b = new Button[9];
		
		b[0] = (Button) findViewById(R.id.Button00);
		b[1] = (Button) findViewById(R.id.Button01);
		b[2] = (Button) findViewById(R.id.Button02);
		b[3] = (Button) findViewById(R.id.Button03);
		b[4] = (Button) findViewById(R.id.Button04);
		b[5] = (Button) findViewById(R.id.Button05);
		b[6] = (Button) findViewById(R.id.Button06);
		b[7] = (Button) findViewById(R.id.Button07);
		b[8] = (Button) findViewById(R.id.Button08);
		return b;
	}
   
	public void makeMove(final Button b) {
        bad_move=true;
		int b_text,b_pos,zuk_pos;
		b_text= Integer.parseInt((String) b.getText());
     	b_pos=find_pos(b_text);
   		zuk_pos=find_pos(0);
   		switch(zuk_pos)
   		{
   		case(0):
   			if(b_pos==1||b_pos==3)
   		    bad_move=false;
   		    break;
   		case(1):
   			if(b_pos==0||b_pos==2||b_pos==4)
   		    bad_move=false;
   		    break;
   		case(2):
   			if(b_pos==1||b_pos==5)
   		    bad_move=false;
   		    break;
   		case(3):
   			if(b_pos==0||b_pos==4||b_pos==6)
   		    bad_move=false;
   		    break;
   		case(4):
   			if(b_pos==1||b_pos==3||b_pos==5||b_pos==7)
   		    bad_move=false;
   		    break;
   		case(5):
   			if(b_pos==2||b_pos==4||b_pos==8)
   		    bad_move=false;
   		    break;
   		case(6):
   			if(b_pos==3||b_pos==7)
   		    bad_move=false;
   		    break;
   		case(7):
   			if(b_pos==4||b_pos==6||b_pos==8)
   		    bad_move=false;
   		    break;
   		case(8):
   			if(b_pos==5||b_pos==7)
   		    bad_move=false;
   		    break;
   		}
   		
   		if(bad_move==true)
   		{
//   			feedbackText.setText("Move Not Allowed");
   			return;
   		}
//   		feedbackText.setText("Move OK");
   		cells.remove(b_pos);
   		cells.add(b_pos, 0);
   		cells.remove(zuk_pos);
   		cells.add(zuk_pos, b_text);
   		
	
    	fill_grid();
		//moveCounter.setText(Integer.toString(Integer.parseInt((String) moveCounter.getText()) + 1));

		 for(int i=0;i<9;i++)
	        {
	           if(cells.get(i)!=goal[i])
	           {
	        	        	   return;
	           }
	        }

		Intent intent=new Intent(getApplicationContext(),MainActivity.class);
		intent.putExtra("activity_name",activityName);

		startActivity(intent);
//		 feedbackText.setText("we have a winner");


	}
	
	public void fill_grid()
    {
	 for(int i=0;i<9;i++)
	 {
		 int text=cells.get(i);
		 AbsoluteLayout.LayoutParams absParams =
			    (AbsoluteLayout.LayoutParams)buttons[text].getLayoutParams();
		 switch(i)
		 {
			 case(0):

				 absParams.x = first;
				 absParams.y = first;
				 buttons[text].setLayoutParams(absParams);
				 break;
			 case(1):

				 absParams.x = second;
				 absParams.y = first;
				 buttons[text].setLayoutParams(absParams);
				 break;
			 case(2):

				 absParams.x = last;
				 absParams.y = first;
				 buttons[text].setLayoutParams(absParams);
				 break;
			 case(3):

				 absParams.x = first;
				 absParams.y = second;
				 buttons[text].setLayoutParams(absParams);
				 break;
			 case(4):

				 absParams.x =second;
				 absParams.y =second;
				 buttons[text].setLayoutParams(absParams);
				 break;
			 case(5):

				 absParams.x = last;
				 absParams.y =second;
				 buttons[text].setLayoutParams(absParams);
				 break;
			 case(6):

				 absParams.x = first;
				 absParams.y = last;
				 buttons[text].setLayoutParams(absParams);
				 break;
			 case(7):

				 absParams.x = second;
				 absParams.y = last;
				 buttons[text].setLayoutParams(absParams);
				 break;
			 case(8):

				 absParams.x = last;
				 absParams.y = last;
				 buttons[text].setLayoutParams(absParams);
				 break;

		 }
		
		 
		}
		
	}
	
	public int find_pos(int element)
	{
		int i=0;
		 for(i=0;i<9;i++)
	        {
	           if(cells.get(i)==element)
	           {
	        	        	   break;
	           }
	        }
		 return i;
	}
	}

    