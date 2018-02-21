package com.android.dnh.moneykeeper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker.OnDateChangedListener;

public class EditTransaction extends Activity implements OnItemSelectedListener, OnClickListener, OnDateChangedListener{
	
	EditText amount, note;
	ToggleButton sign;
	TextView tv1;
	Button save, cancel,bMyDate;
	Spinner category;
	DatePicker myDatePicker;

	int myDate, mySign;
	int confirmMode=9;
	float myAmount;
	String myCategory, myNote, myCurrency;
	MyCategoriesList mcl = new MyCategoriesList(EditTransaction.this);
	String[] categories;
	int a;

	ArrayAdapter<String> adapterCatergory;
	
	int editRow=6;
	String oldDate, oldAmount, oldCategory, oldNote, oldCurrency;
	String key  = "rowid";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtransaction);
		//editRow = Integer.parseInt(getIntent().getExtras().getString(key));
		editRow=Integer.parseInt(getIntent().getExtras().getString(key));
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edittransaction_menu, menu);
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
			alertMessage(0);
			/*boolean didItWork = true;
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

				MyMoney entry = new MyMoney(EditTransaction.this);
				entry.open();
				if(entryValid){// check if enter correctly to field
					entry.updateEntry(editRow,myDate, myAmount, myCategory, myNote, myCurrency);}
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
					finish();
				}
			}*/
			return true;
		case R.id.MDelete:
			alertMessage(1);
/*			MyMoney entry = new MyMoney(EditTransaction.this);
			entry.open();
			entry.deleteEntry(editRow);
			finish();*/
			return true;
		
		default:
		return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initilize();
	}

	private void initilize() {
		mcl.open();
		int a = mcl.loadData(1).length;// Check if already create table with default value

		if (a == 0) {//if less than 4 entries TO BE CHANGE
			mcl.saveData("New Category...");
			mcl.saveData("Uncategorized");
			mcl.saveData("Food");
			mcl.saveData("Entertainment");
		}
		categories = mcl.loadData(1); //TODO Need to move some where
		mcl.close();
		
		
		
/*		Calendar c = Calendar.getInstance();
		int current_day = c.get(Calendar.DAY_OF_MONTH);
		int current_month = c.get(Calendar.MONTH);// months is from 0-11
		int current_year = c.get(Calendar.YEAR);*/


		amount = (EditText) findViewById(R.id.etAmount);
		note = (EditText) findViewById(R.id.etNote);
		save = (Button) findViewById(R.id.BtSave);
		cancel = (Button) findViewById(R.id.BtCancel);
		sign = (ToggleButton) findViewById(R.id.tbSign);
		category = (Spinner) findViewById(R.id.sCatergories);
		myDatePicker = (DatePicker) findViewById(R.id.datePicker1);
		
		List<String> cat = new ArrayList<String>();

		adapterCatergory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cat);
		// Specify the layout to use when the list of choices appears
		adapterCatergory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		// adapterCatergory.add("TEST");
		for (int i = 1; i < (categories.length); i++) {// TODO Move to Different location to ini everytime update
			adapterCatergory.add(categories[i]);
		}adapterCatergory.add(categories[0]);
		// tv1.setText(categories[0]+" + "+ categories.length);

		category.setAdapter(adapterCatergory);
		category.setOnItemSelectedListener(this);

		
		getInfo();
		
		int current_day = myDateFormat.parseDate2(0, Integer.parseInt(oldDate));
		int current_month = myDateFormat.parseDate2(1, Integer.parseInt(oldDate))-1;// months is from 0-11
		int current_year =myDateFormat.parseDate2(2, Integer.parseInt(oldDate));

		tv1 = (TextView) findViewById(R.id.tvDate);
		tv1.setText(current_day + " - " + (current_month+1) + " - " + current_year);
		myDate = current_year * 100 * 100 + (current_month+1) * 100 + current_day;// set the date in int form YYYYMMDD
		tv1.setOnClickListener(this);
		myDatePicker.init(current_year, current_month, current_day,this);
		
	//	bMyDate = (Button) findViewById(R.id.bDate);
		//bMyDate.setText(current_day + "- " + current_month + "- " + current_year);
//		bMyDate.setOnClickListener(this);

		// ArrayAdapter.createFromResource(this,, android.R.layout.simple_spinner_item);//array is from string.xml
		// List<String> cat = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.default_categories)));
		// can just create empty list
		// List<String> cat = new ArrayList<String>; then
		// cat.add(...); can get from internal or external data
		
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
		// adapterCatergory.add("test");
		switch (arg0.getId()) {
		case R.id.BtSave:// when click save
			alertMessage(0);
			/*boolean didItWork = true;
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

				MyMoney entry = new MyMoney(EditTransaction.this);
				entry.open();

				if(entryValid){
					entry.updateEntry(editRow, myDate, myAmount, myCategory, myNote, myCurrency);}
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
					finish();
				}
			}*/
			break;

		case R.id.BtCancel:
			/*adapterCatergory.add("test");// for spinner testing*/
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
		}

	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		tv1.setText(dayOfMonth + " - " + (monthOfYear+1) + " - " + year);
		myDate = year * 100 * 100 + (monthOfYear+1) * 100 + dayOfMonth;
		
	}
	
	private void getInfo(){// Getting old info from editRow id 
		MyMoney entry = new MyMoney(EditTransaction.this);
		try {
		entry.open();
		oldDate = entry.getData(2, editRow);
		oldAmount = entry.getData(3, editRow);
		oldCategory = entry.getData(4, editRow);
		oldNote = entry.getData(5, editRow);
		//oldCurrency = entry.getData(6, editRow);// DO nothing right now
		entry.close();
		


		
		float oldAmount2 = Float.parseFloat(oldAmount);
		
		if (oldAmount2<0) {
			sign.setChecked(false);
			oldAmount2 = oldAmount2*(-1);
		} else {
			sign.setChecked(true);
		}
		
		if (sign.isChecked()) {
			mySign = 1;
		} else {
			mySign = -1;
		}

		
		amount.setText( String.valueOf(oldAmount2));
		
		int mypos =0;
		category.setSelection(mypos);
		
			while(!category.getSelectedItem().toString().equals(oldCategory)){
				mypos++;
				category.setSelection(mypos);
				if(mypos>100){
					mypos = 0;
					Dialog a = new Dialog(this);
					a.setTitle("!!! Infi loop");
					a.show();
					break;}
			}
		note.setText(oldNote);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String error = e.toString();
			Dialog d = new Dialog(this);
			d.setTitle("Error Getting Old Info");
			TextView tv = new TextView(this);
			tv.setText(error);
			d.setContentView(tv);
			d.show();
		}
		
	}
	
	public void alertMessage(int a) {
		confirmMode=a;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					//Toast.makeText(ImportExportDB.this, "Yes Clicked", Toast.LENGTH_LONG).show();
					if(confirmMode==0){
						boolean didItWork = true;
						boolean entryValid = true;
						try {
							try {// error if user don't input float number
								myAmount = mySign * Float.parseFloat(amount.getText().toString());// Getting float amt

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Dialog d = new Dialog(EditTransaction.this);
								d.setTitle("Not a valid number");
								d.show();
								didItWork = false;
								entryValid = false;
							}
							// myCategory = "Default";
							myCategory = category.getSelectedItem().toString();// Getting from spinner

							myNote = note.getText().toString();
							myCurrency = "CND";// To get from base currency in pref.

							MyMoney entry = new MyMoney(EditTransaction.this);
							entry.open();
							if(entryValid){// check if enter correctly to field
								entry.updateEntry(editRow,myDate, myAmount, myCategory, myNote, myCurrency);}
							entry.close();
							

						} catch (Exception e) {

							didItWork = false;
							String error = e.toString();// to print out error
							Dialog d = new Dialog(EditTransaction.this);
							d.setTitle("Booo..");
							TextView tv = new TextView(EditTransaction.this);
							tv.setText(error);
							d.setContentView(tv);
							d.show();
						} finally {
							if (didItWork) {
								finish();
							}
						}
					}else if(confirmMode==1){
						MyMoney entry = new MyMoney(EditTransaction.this);
						entry.open();
						entry.deleteEntry(editRow);
						entry.close();
						finish();
					}else{}
					 
					break;

				case DialogInterface.BUTTON_NEGATIVE:

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if(a==0){
			builder.setMessage("Are you sure you want to save changes?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
		}else{
			builder.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
		}
	}

}
