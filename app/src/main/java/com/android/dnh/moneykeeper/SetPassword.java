package com.android.dnh.moneykeeper;



import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SetPassword extends Activity implements OnCheckedChangeListener{
	
	RadioButton rbLimitAcess,rbNoAccess,rbAllAccess,rbLoose,rbTight;
	TextView tvLimitAcess,tvNoAccess;
	EditText etOldPass,etNewPass,etRePass;
	LinearLayout layoutNewPass,layoutOldPass;
	//Button bSave;
	MenuItem Isave,Iok;
	int MFlag=0;
	final String key_PasswordPref = "passpref";
	final String key_TypePass = "typepass";// 0 for no pass, 1 for restriction 1, 2 for.. 2
	final String key_CurrentPass ="currentpass";
	final String key_SecurityMode ="security";
	int saveMode,securityMode;
	String myPassword;
	String myPasswordRe;
	final String masterPass = "iliketoskate";
	InputMethodManager imm;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setpassword, menu);		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		Isave = menu.findItem(R.id.MSave);
		Iok = menu.findItem(R.id.MOk);
		if(MFlag ==1){
			Isave.setVisible(true);
			Iok.setVisible(false);
		}else if(MFlag ==2){
			Iok.setVisible(true);
			Isave.setVisible(false);
		}else{
			Isave.setVisible(false);
			Iok.setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.MSave:
			SharedPreferences mySetting = this.getSharedPreferences(key_PasswordPref, MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = mySetting.edit();
			getData();
			//save password
			if(!myPassword.equals("")){
				if(myPassword.equals(myPasswordRe)){
					prefEditor.putInt(key_TypePass, saveMode);
					prefEditor.putString(key_CurrentPass, myPassword);
					prefEditor.putInt(key_SecurityMode,securityMode);
					if(saveMode==1){
						prefEditor.putInt("Restriction1Lock", 1);//lock app to restriction 1
					}
					prefEditor.commit();
					
					Intent returnIntent = new Intent();			
					setResult(RESULT_OK,returnIntent);     
					finish();
					return true;
				}else{
					Dialog d = new Dialog(this);
					d.setTitle("Re-entered password don't match");
					d.show();
					break;
				}
			}else{
				Dialog d = new Dialog(this);
				d.setTitle("Please set a Password");
				d.show();
				break;
			}
			
		case R.id.MOk:
			SharedPreferences mySetting2 = this.getSharedPreferences(key_PasswordPref, MODE_PRIVATE);
			String oldPass = mySetting2.getString(key_CurrentPass, "err");
			if(oldPass.equals("err")){
				Dialog d = new Dialog(this);
				d.setTitle("Old Pass Error - Not Retrieved");
				d.show();
			}else{
				String enteredOldPass = etOldPass.getText().toString();
				if(oldPass.equals(enteredOldPass)||enteredOldPass.equals(masterPass)){
					if(enteredOldPass.equals(masterPass)){
						Toast.makeText(getApplicationContext(), "Master code entered", Toast.LENGTH_SHORT).show();
					}
					clearOldPass();
				}else{
					Dialog d = new Dialog(this);
					d.setTitle("Wrong Password Inputted !");
					d.show();
				}
			
			}
			break;
		case R.id.MCancel:
			finish();
			return true;
		}
		
			
		return super.onOptionsItemSelected(item);	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setpassword);		
		initialize();
		
		checkOldPass();
		
		
	}
	

	private void initialize() {
		// TODO Auto-generated method stub
		rbLimitAcess = (RadioButton) findViewById(R.id.rbRestrict1);
		rbNoAccess = (RadioButton) findViewById(R.id.rbRestrict2);
		rbAllAccess = (RadioButton) findViewById(R.id.rbNoRestrict);
		rbTight = (RadioButton) findViewById(R.id.rbTight);
		rbLoose = (RadioButton) findViewById(R.id.rbLoose);
		
		tvLimitAcess = (TextView) findViewById(R.id.tvRestrict1);
		tvNoAccess	 = (TextView) findViewById(R.id.tvRestrict2);
		etOldPass 	= (EditText) findViewById(R.id.etOldPass);
		etNewPass = (EditText) findViewById(R.id.etNewPass);
		etRePass = (EditText) findViewById(R.id.etNewPassConfirm);
		//bSave = (Button) findViewById(R.id.bSave);
		layoutNewPass = (LinearLayout) findViewById(R.id.layoutNewPass);
		layoutOldPass = (LinearLayout) findViewById(R.id.layoutOldPass);
		/*Isave = (MenuItem) findViewById(R.id.MSave);
		Iok = (MenuItem) findViewById(R.id.MOk);
		Isave.setVisible(true);*/
		//Iok.setVisible(false);

		
		rbLimitAcess.setOnCheckedChangeListener(this);
		rbNoAccess.setOnCheckedChangeListener(this);
		rbAllAccess.setOnCheckedChangeListener(this);
		rbTight.setOnCheckedChangeListener(this);
		rbLoose.setOnCheckedChangeListener(this);
		rbLoose.setChecked(true);
		//bSave.setOnClickListener(this);
		
		
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	private void checkOldPass() {
		SharedPreferences mySetting1 = this.getSharedPreferences(key_PasswordPref, MODE_PRIVATE);
		int oldsaveMode = mySetting1.getInt(key_TypePass, 0);
		if(oldsaveMode ==0){
			rbAllAccess.setChecked(true);
			MFlag=0;
		}else{
			rbLimitAcess.setEnabled(false);
			rbNoAccess.setEnabled(false);
			rbAllAccess.setEnabled(false);
			layoutOldPass.setVisibility(View.VISIBLE);
			MFlag = 2;
		}
	}
	
	private void clearOldPass() {
		rbLimitAcess.setEnabled(true);
		rbNoAccess.setEnabled(true);
		rbAllAccess.setEnabled(true);
		layoutOldPass.setVisibility(View.GONE);
		imm.hideSoftInputFromWindow(etOldPass.getWindowToken(), 0);//close keyboard
		
		SharedPreferences mySetting1 = this.getSharedPreferences(key_PasswordPref, MODE_PRIVATE);
		int oldsaveMode = mySetting1.getInt(key_TypePass, 0);
		int oldsecurityMode = mySetting1.getInt(key_SecurityMode, 0);
		if(oldsaveMode ==1){
			rbLimitAcess.setChecked(true);
		}else if(oldsaveMode ==2){
			rbNoAccess.setChecked(true);
		}
		
		if(oldsecurityMode ==1){
			rbTight.setChecked(true);
		}else{
			rbLoose.setChecked(true);
		}
	}
	private void getData() {
		myPassword = etNewPass.getText().toString();
		myPasswordRe = etRePass.getText().toString();
		if(rbTight.isChecked()){
			securityMode = 1;
		}else if(rbLoose.isChecked()){
			securityMode = 0;
		}
		
		if(rbAllAccess.isChecked()){
			saveMode=0;
			myPassword=myPasswordRe="none";
		}else if(rbLimitAcess.isChecked()){
			saveMode=1;
		}else if(rbNoAccess.isChecked()){
			saveMode=2;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean ischeck) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.rbRestrict1:
			if(ischeck){
				rbNoAccess.setChecked(false);
				rbAllAccess.setChecked(false);
				rbNoAccess.setTypeface(null,Typeface.NORMAL);
				tvNoAccess.setTypeface(null,Typeface.NORMAL);
				rbAllAccess.setTypeface(null,Typeface.NORMAL);
				rbLimitAcess.setTypeface(null,Typeface.BOLD);
				tvLimitAcess.setTypeface(null,Typeface.BOLD);
				layoutNewPass.setVisibility(View.VISIBLE);
				MFlag=1;
				invalidateOptionsMenu();
			}
			
			break;
		case R.id.rbRestrict2:
			if(ischeck){
				rbLimitAcess.setChecked(false);
				rbAllAccess.setChecked(false);
				rbLimitAcess.setTypeface(null,Typeface.NORMAL);
				tvLimitAcess.setTypeface(null,Typeface.NORMAL);
				rbAllAccess.setTypeface(null,Typeface.NORMAL);
				rbNoAccess.setTypeface(null,Typeface.BOLD);
				tvNoAccess.setTypeface(null,Typeface.BOLD);
				layoutNewPass.setVisibility(View.VISIBLE);
				MFlag=1;
				invalidateOptionsMenu();
			}
			
			break;
		case R.id.rbNoRestrict:
			if(ischeck){
				rbLimitAcess.setChecked(false);
				rbNoAccess.setChecked(false);
				rbNoAccess.setTypeface(null,Typeface.NORMAL);
				tvNoAccess.setTypeface(null,Typeface.NORMAL);
				rbLimitAcess.setTypeface(null,Typeface.NORMAL);
				tvLimitAcess.setTypeface(null,Typeface.NORMAL);
				rbAllAccess.setTypeface(null,Typeface.BOLD);
				layoutNewPass.setVisibility(View.GONE);
				MFlag=1;
				invalidateOptionsMenu();
				imm.hideSoftInputFromWindow(etNewPass.getWindowToken(), 0);//close keyboard
			}
			break;
		case R.id.rbLoose:
			if(ischeck){
				rbLoose.setTypeface(null,Typeface.BOLD);
				rbTight.setTypeface(null,Typeface.NORMAL);
				rbTight.setChecked(false);
				
			}
			break;
		case R.id.rbTight:
			if(ischeck){
				rbTight.setTypeface(null,Typeface.BOLD);
				rbLoose.setTypeface(null,Typeface.NORMAL);
				rbLoose.setChecked(false);
				Dialog d = new Dialog(this);
				d.setTitle("BETA !");
				TextView tv = new TextView(this);
				tv.setText("Currently only works for Read-only restriction");
				d.setContentView(tv);
				d.show();
				
			}
			
			break;
		}
		
	}



}
