package com.android.dnh.moneykeeper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AddTransaction extends Activity implements OnItemSelectedListener, OnClickListener, OnDateChangedListener {

	EditText amount, note;
	ToggleButton sign;
	TextView tv1;
	Button save, cancel,bMyDate;
	Spinner category;
	DatePicker myDatePicker;

	int myDate, mySign = -1;
	float myAmount;
	String myCategory, myNote, myCurrency;
	MyCategoriesList mcl = new MyCategoriesList(AddTransaction.this);
	// String[] categories = {"Category","Food","Entertainment"};
	String[] categories;

	ArrayAdapter<String> adapterCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtransaction);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.addtransaction_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.MCancel:
			finish();
			return true;
		case R.id.MSave:
			boolean didItWork = true;
			boolean entryValid = true;
			try {
				try {// error if user don't input float number
					myAmount = mySign * Float.parseFloat(amount.getText().toString());// Getting float amt

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Dialog d = new Dialog(this);
					d.setTitle("Not a valid number");
					d.show();
					didItWork = false;
					entryValid = false;
				}
				// myCategory = "Default";
				myCategory = category.getSelectedItem().toString();// Getting from spinner

				myNote = note.getText().toString();
				myCurrency = "CND";// To get from base currency in pref.

				MyMoney entry = new MyMoney(AddTransaction.this);
				entry.open();
				// entry.createEntry(myDate,myAmount,myCategory,myNote,myCurrency,mySign);
				if(entryValid){// check if enter correctly to field
					entry.createEntry(myDate, myAmount, myCategory, myNote, myCurrency);}
				entry.close();
				

			} catch (Exception e) {

				didItWork = false;
				String error = e.toString();// to print out error
				Dialog d = new Dialog(this);
				d.setTitle("Booo..");
				TextView tv = new TextView(this);
				tv.setText(error);
				d.setContentView(tv);
				d.show();
			} finally {
				if (didItWork) {
/*					Dialog d = new Dialog(this);
					d.setTitle("Heck ya!");
					TextView tv = new TextView(this);
					tv.setText("Success");
					d.setContentView(tv);
					d.show();
					d.cancel();*/
					finish();
				}
			}
			return true;
		
		default:
		return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initialize();
	}

	private void initialize() {

		mcl.open();
	/*	int a = mcl.loadData(1).length;// Check if already create table with default value

		if (a == 0) {//if less than 4 entries TO BE CHANGE
			mcl.saveData("New Category...");
			mcl.saveData("Uncategorized");
			mcl.saveData("Food");
			mcl.saveData("Entertainment");

		}*/
		categories = mcl.loadData(1); //TODO Need to move some where
		mcl.close();

		Calendar c = Calendar.getInstance();
		int current_day = c.get(Calendar.DAY_OF_MONTH);
		int current_month = c.get(Calendar.MONTH);// months is from 0-11
		int current_year = c.get(Calendar.YEAR);

		tv1 = (TextView) findViewById(R.id.tvDate);
		tv1.setText(current_day + " - " + (current_month+1) + " - " + current_year);
		myDate = current_year * 100 * 100 + (current_month+1) * 100 + current_day;// set the date in int form YYYYMMDD
		tv1.setOnClickListener(this);

		amount = (EditText) findViewById(R.id.etAmount);
		note = (EditText) findViewById(R.id.etNote);
		save = (Button) findViewById(R.id.BtSave);
		cancel = (Button) findViewById(R.id.BtCancel);
		sign = (ToggleButton) findViewById(R.id.tbSign);
		category = (Spinner) findViewById(R.id.sCatergories);
		myDatePicker = (DatePicker) findViewById(R.id.datePicker1);
		myDatePicker.init(current_year, current_month, current_day,this);
		
	//	bMyDate = (Button) findViewById(R.id.bDate);
		//bMyDate.setText(current_day + "- " + current_month + "- " + current_year);
//		bMyDate.setOnClickListener(this);

		// ArrayAdapter.createFromResource(this,, android.R.layout.simple_spinner_item);//array is from string.xml
		// List<String> cat = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.default_categories)));
		// can just create empty list
		// List<String> cat = new ArrayList<String>; then
		// cat.add(...); can get from internal or external data
		List<String> cat = new ArrayList<String>();

		adapterCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cat);
		// Specify the layout to use when the list of choices appears
		adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		// adapterCategory.add("TEST");
		for (int i = 1; i < (categories.length); i++) {// TODO Move to Different location to ini everytime update
			adapterCategory.add(categories[i]);
		}
		adapterCategory.add(categories[0]);
		// tv1.setText(categories[0]+" + "+ categories.length);

		category.setAdapter(adapterCategory);
		category.setOnItemSelectedListener(this);

		sign.setOnClickListener(this);
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub

        String selected = arg0.getItemAtPosition(position).toString();

        if(selected.equals("New Category..."))
        {
            //tv1.setText("Work");//TODO LEFT OFF FROM Aug 15
			Intent i= new Intent("com.android.dnh.moneykeeper.NEWCATEGORY");
			startActivity(i);
        }
	}

	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		// adapterCategory.add("test");
		switch (arg0.getId()) {
		case R.id.BtSave:// when click save
			boolean didItWork = true;
			boolean entryValid = true;
			try {
				try {// error if user don't input float number
					myAmount = mySign * Float.parseFloat(amount.getText().toString());// Getting float amt

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Dialog d = new Dialog(this);
					d.setTitle("Please enter a Valid number");
					d.show();
					didItWork = false;
					entryValid = false;
				}
				// myCategory = "Default";
				myCategory = category.getSelectedItem().toString();// Getting from spinner

				myNote = note.getText().toString();
				myCurrency = "CND";// To get from base currency in pref.

				MyMoney entry = new MyMoney(AddTransaction.this);
				entry.open();
				// entry.createEntry(myDate,myAmount,myCategory,myNote,myCurrency,mySign);
				if(entryValid){
					entry.createEntry(myDate, myAmount, myCategory, myNote, myCurrency);}
				entry.close();
				

			} catch (Exception e) {

				didItWork = false;
				String error = e.toString();// to print out error
				Dialog d = new Dialog(this);
				d.setTitle("Booo..");
				TextView tv = new TextView(this);
				tv.setText(error);
				d.setContentView(tv);
				d.show();
			} finally {
				if (didItWork) {
/*					Dialog d = new Dialog(this);
					d.setTitle("Heck ya!");
					TextView tv = new TextView(this);
					tv.setText("Success");
					d.setContentView(tv);
					d.show();
					d.cancel();*/
					finish();
				}
			}
			break;

		case R.id.BtCancel:
			/*adapterCategory.add("test");// for spinner testing*/
			finish();
			break;

		case R.id.tbSign:
			if (sign.isChecked()) {
				mySign = 1;
				// tv1.setText(String.valueOf(mySign));
			} else {
				mySign = -1;
			}
			break;
			
		case R.id.tvDate:

			break;
			
/*		case R.id.bDate:
			 MyDatePicker newFragment = new MyDatePicker();
			 newFragment.show(getSupportFragmentManager(), "datePicker");
			 int date = Integer.parseInt(bMyDate.getText().toString());
			 tv1.setText(String.valueOf(date));
			 
			 //tv1.setText(""+date);
			 //tv1.setText(bMyDate.getText().toString());
			// newFragment.onDateSet(view, year, month, day)
			 //int newDate[] = newFragment.load();
			 //bMyDate.setText(newDate[0] + "- " + newDate[1] + "- " + newDate[2]);
			 //myDate = newDate[2] * 100 * 100 + newDate[1] * 100 + newDate[0];

			break;*/
		}
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		tv1.setText(dayOfMonth + " - " + (monthOfYear+1) + " - " + year);
		myDate = year * 100 * 100 + (monthOfYear+1) * 100 + dayOfMonth;
		
	}

}

