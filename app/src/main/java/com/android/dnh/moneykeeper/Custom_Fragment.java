package com.android.dnh.moneykeeper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Custom_Fragment extends Fragment implements OnClickListener {
	
	View view;
	Button bAboutMe,bSetGoal,bCategoryMan,bSetPassword,bImportExport;
	Button bfAutoSched,bfWidget,bfSync;
	TextView tvAboutMe,tvPassword;
	int state =0,passwordMode;
	final String key_PasswordPref = "passpref";
	final String key_TypePass = "typepass";// 0 for no pass, 1 for restriction 1, 2 for.. 2
	final String key_CurrentPass ="currentpass";
	//final String mysettingPrefs = "myActivity";
	//SharedPreferences mySetting;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.custom_fragment, container,false);
		//tv1 = (TextView)view.findViewById(R.id.textView1);
		//tv1.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
		
		initialize();
		checkPasswordMode();
		return view;
	}
	private void initialize(){
		tvPassword = (TextView) view.findViewById(R.id.tvPassword);
		
		bAboutMe = (Button) view.findViewById(R.id.bAboutMe);
		bAboutMe.setOnClickListener(this);
		bSetGoal = (Button) view.findViewById(R.id.bSetGoal);
		bSetGoal.setOnClickListener(this);
		bCategoryMan = (Button) view.findViewById(R.id.bCategoryMan);
		bCategoryMan.setOnClickListener(this);
		bSetPassword = (Button) view.findViewById(R.id.bPassword);
		bSetPassword.setOnClickListener(this);
		bImportExport = (Button) view.findViewById(R.id.bImportExport);
		bImportExport.setOnClickListener(this);
		bfAutoSched= (Button) view.findViewById(R.id.bSched);
		bfWidget= (Button) view.findViewById(R.id.bWidget);
		bfSync= (Button) view.findViewById(R.id.bSync);
		bfAutoSched.setOnClickListener(this);
		bfWidget.setOnClickListener(this);
		bfSync.setOnClickListener(this);
		//SharedPreferences mySetting = this.getActivity().getSharedPreferences(mysettingPrefs, Context.MODE_PRIVATE);
		//SharedPreferences.Editor prefEditor = mySetting.edit();  
		//prefEditor.putString("UserName", "Guest123");  
		//prefEditor.putBoolean("PaidUser", false);  
		//prefEditor.commit();  
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.bAboutMe:
			/*if(state ==0){
				tvAboutMe.setVisibility(View.VISIBLE);
				state =1;
			}else if(state == 1){
				tvAboutMe.setVisibility(View.GONE);
				state =0;
			}*/
			Dialog d = new Dialog(this.getActivity());
			d.setTitle("About Me");
			TextView tv = new TextView(this.getActivity());
			tv.setTextSize(15);
			tv.setPadding(10, 0, 0, 10);
			tv.setText("I am an Electrical Engineer graduate and I develope Android application as a hobby. This is the first one that I've released and it will always be free. I'll try to add more feature to it in the future. For bug report, idea suggestion please contact me at: dnhAndroid@gmail.com");
			d.setContentView(tv);
			d.show();
			break;
		case R.id.bSched:
			Dialog d1 = new Dialog(this.getActivity());
			d1.setTitle("Automatic Schedule Transaction");
			TextView tv1 = new TextView(this.getActivity());
			tv1.setTextSize(15);
			tv1.setPadding(10, 0, 0, 10);
			tv1.setText("This feature is for a time base transaction such as paychecks, bills. Once set up the app will automatically add the transaction at the set date");
			d1.setContentView(tv1);
			d1.show();
			break;
		case R.id.bWidget:
			Dialog d11 = new Dialog(this.getActivity());
			d11.setTitle("Widget Setting");
			TextView tv11 = new TextView(this.getActivity());
			tv11.setTextSize(15);
			tv11.setPadding(10, 0, 0, 10);
			tv11.setText("The widget will provide a quick way to add transaction or to view the current balance and goal");
			d11.setContentView(tv11);
			d11.show();
			break;
		case R.id.bSync:
			Dialog d111 = new Dialog(this.getActivity());
			d111.setTitle("Sync Cloud");
			TextView tv111 = new TextView(this.getActivity());
			tv111.setTextSize(15);
			tv111.setPadding(10, 0, 0, 10);
			tv111.setText("The transaction database will be able to be sync or saved/restored from cloud services such as Dropbox.");
			d111.setContentView(tv111);
			d111.show();
			break;
		case R.id.bImportExport:
			Intent i3= new Intent("com.android.dnh.moneykeeper.IMPORTEXPORTDB");
			startActivity(i3);
			break;
		case R.id.bSetGoal:
			Intent i= new Intent("com.android.dnh.moneykeeper.GOALSET");
			startActivity(i);
			//startActivityForResult(i, 2);
			break;
		case R.id.bCategoryMan:
			Intent i2= new Intent("com.android.dnh.moneykeeper.CATEGORYMANAGEMENT");
			startActivity(i2);
			break;
			
		case R.id.bPassword:
			Intent i1= new Intent("com.android.dnh.moneykeeper.SETPASSWORD");
			//startActivity(i1);
			startActivityForResult(i1, 2);
			break;
		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 2) {

			if(resultCode == FragmentActivity.RESULT_OK){      
		    	 //tvAboutMe.setText("!@#@#@ OH YEAH!");
				checkPasswordMode();
		     }		    
			/*if (resultCode == FragmentActivity.RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }*/
		}
	}
	
	private void checkPasswordMode(){
				
		SharedPreferences mySetting = this.getActivity().getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
		passwordMode = mySetting.getInt(key_TypePass, 0);
		if(passwordMode==1){
			tvPassword.setText("Lock access to: Read-only");
		}else if(passwordMode==2){
			tvPassword.setText("Lock access to: Accessing app");
		}else{
			tvPassword.setText("Access is not locked");
		}
	}

}
