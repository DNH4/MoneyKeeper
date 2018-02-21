package com.android.dnh.moneykeeper;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	int date[]= new int[3];
	int myday;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		date[0]=day;
		date[1]=month;
		date[2]=year;
		myday = day;
		/*Button bDate = (Button)getActivity().findViewById(R.id.bDate);
		//bDate.setText(day+"-"  + month +"-"+ year);
		bDate.setText(String.valueOf(day));*/
		
	}
}