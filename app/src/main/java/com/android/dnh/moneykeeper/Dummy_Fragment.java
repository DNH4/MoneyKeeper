package com.android.dnh.moneykeeper;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Dummy_Fragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "placeholder_text";

	View view;
	TextView tv1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.dummy_fragment, container,false);
		//tv1 = (TextView)view.findViewById(R.id.textView1);
		//tv1.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

		return view;
	}

}
