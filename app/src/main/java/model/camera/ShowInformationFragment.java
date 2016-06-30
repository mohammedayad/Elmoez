package model.camera;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import eu.kudan.kudansamples.R;


@SuppressLint("ValidFragment")
public class ShowInformationFragment extends DialogFragment {
	TextView info;
	Button Done;
	View layout;
	String text;
	SharedPreferences settingPreference;

		String data="";


	public ShowInformationFragment(String data){
		this.data=data;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.information_view, null);
		getDialog().setTitle("");

		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

		info = (TextView) layout.findViewById(R.id.textView1);
		Done = (Button) layout.findViewById(R.id.button1);

		info.setText(data);


		settingPreference=this.getActivity().getSharedPreferences("dataOfApp", Context.MODE_PRIVATE);


		info.setTextSize(settingPreference.getFloat("fontSize", 0) + 20);


		info.setMovementMethod(new ScrollingMovementMethod());

		Done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ImageTargets.entered = false;
				dismiss();
			}

		});

		setCancelable(false);
		return layout;
	}
//
//	@Override
//	public void onResume (){
//		super.onResume();
//
//		settingPreference=this.getActivity().getSharedPreferences("dataOfApp", Context.MODE_PRIVATE);
//
//
//        info.setTextSize(settingPreference.getFloat("fontSize",0)+20);
//		Toast.makeText(getContext(), "resuuuuuuuuuuuuume", Toast.LENGTH_LONG).show();
//        Toast.makeText(getContext(),"resumeeeeeee",Toast.LENGTH_SHORT).show();
//
//
//
//
//	}





}
