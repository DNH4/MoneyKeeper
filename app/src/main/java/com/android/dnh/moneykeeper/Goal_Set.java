package com.android.dnh.moneykeeper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class Goal_Set extends Activity implements OnClickListener {

	TextView dayGoal, monthGoal, yearGoal;
	EditText etDay, etMonth, etYear;
	Button bSave, bCancel;
	String chosenGoal;
	float myDailyGoal, myMonthlyGoal, myYearlyGoal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.goal_set);
		initialize();

	}

	private void initialize() {
		dayGoal = (TextView) findViewById(R.id.cbDailyGoal);
		monthGoal = (TextView) findViewById(R.id.cbMonthlyGoal);
		yearGoal = (TextView) findViewById(R.id.cbYearlyGoal);

		etDay = (EditText) findViewById(R.id.etDailyGoal);
		etMonth = (EditText) findViewById(R.id.etMonthlyGoal);
		etYear = (EditText) findViewById(R.id.etYearlyGoal);

		bSave = (Button) findViewById(R.id.BtSave);
		bCancel = (Button) findViewById(R.id.BtCancel);

		bSave.setOnClickListener(this);
		bCancel.setOnClickListener(this);

		loadPref();

	}

	private void loadPref() {
		// TODO Auto-generated method stub
		SharedPreferences mySetting = this.getSharedPreferences("mygoal", MODE_PRIVATE);
		myDailyGoal = mySetting.getFloat("dailygoal", 0);
		myMonthlyGoal = mySetting.getFloat("monthlygoal", 0);
		myYearlyGoal = mySetting.getFloat("yearlygoal", 0);
		// int a = mySetting.getInt("goaledited",0);
		etDay.setHint("Current: " + myDailyGoal + " $");
		etMonth.setHint("Current: " + myMonthlyGoal + " $");
		etYear.setHint("Current: " + myYearlyGoal + " $");
		// Toast.makeText(getApplicationContext(), ""+a, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.BtSave:
			SharedPreferences mySetting = this.getSharedPreferences("mygoal", MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = mySetting.edit();
			


			if (!etDay.getText().toString().equals("")) {
				try {
					float a = Float.parseFloat(etDay.getText().toString());
					prefEditor.putFloat("dailygoal", a);
					prefEditor.putInt("goaledited", 1);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "Invalid Number - Day Goal", Toast.LENGTH_LONG).show();
				}
			}
			if (!etMonth.getText().toString().equals("")) {
				try {
					float a = Float.parseFloat(etMonth.getText().toString());
					prefEditor.putFloat("monthlygoal", a);
					prefEditor.putInt("goaledited", 1);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "Invalid Number - Month Goal", Toast.LENGTH_LONG).show();
				}
/*				prefEditor.putFloat("monthlygoal", Float.parseFloat(etMonth.getText().toString()));
				prefEditor.putInt("goaledited", 1);*/
			}
			if (!etYear.getText().toString().equals("")) {
				try {
					float a = Float.parseFloat(etYear.getText().toString());
					prefEditor.putFloat("yearlygoal", a);
					prefEditor.putInt("goaledited", 1);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "Invalid Number - Year Goal", Toast.LENGTH_LONG).show();
				}
/*				prefEditor.putFloat("yearlygoal", Float.parseFloat(etYear.getText().toString()));
				prefEditor.putInt("goaledited", 1);*/
			}
			prefEditor.commit();
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
			break;
		case R.id.BtCancel:
			Intent returnIntent1 = new Intent();
			setResult(RESULT_CANCELED, returnIntent1);
			finish();
			break;
		}
	}
}
