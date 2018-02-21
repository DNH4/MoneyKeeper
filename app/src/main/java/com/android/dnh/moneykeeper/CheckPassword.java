package com.android.dnh.moneykeeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckPassword extends Activity implements OnClickListener{
	
	Button bEnter;
	EditText etOldPass;
	TextView tvWrongPass;
	
	int passType,attempt=1;	
	int isWelcome=0;
	
	final String key_PasswordPref = "passpref";
	final String key_TypePass = "typepass";// 0 for no pass, 1 for restriction 1, 2 for.. 2
	final String key_CurrentPass ="currentpass";
	final String masterPass = "iliketoskate";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkpass);
		isWelcome = getIntent().getExtras().getInt("welcome");
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		bEnter = (Button) findViewById(R.id.bEnter);
		etOldPass = (EditText) findViewById(R.id.etOldPass);
		tvWrongPass = (TextView) findViewById(R.id.tvPassWrong);		
		bEnter.setOnClickListener(this);		
		passType = checkPassword();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bEnter:
			SharedPreferences mySetting2 = this.getSharedPreferences(key_PasswordPref, MODE_PRIVATE);
			String oldPass = mySetting2.getString(key_CurrentPass, "err");
			if(oldPass.equals("err")){
				Dialog d = new Dialog(this);
				d.setTitle("Old Pass Error - Not Retrieved");
				d.show();
			}else{
				String enteredOldPass = etOldPass.getText().toString();
				if(oldPass.equals(enteredOldPass)||enteredOldPass.equals(masterPass)){//Password enter correctly then unlock
					if(enteredOldPass.equals(masterPass)){
						Toast.makeText(getApplicationContext(), "Master code entered", Toast.LENGTH_SHORT).show();
					}

					if(isWelcome == 1){// if from welcome screen
						Intent openStartingPoint = new Intent("com.android.dnh.moneykeeper.MYMAIN");// This using action
						startActivity(openStartingPoint);
					}else{
						SharedPreferences mySetting = this.getSharedPreferences(key_PasswordPref, MODE_PRIVATE);
						SharedPreferences.Editor prefEditor = mySetting.edit();
						prefEditor.putInt("Restriction1Lock", 0);//unlock app from restriction 1
						prefEditor.commit();
						finish();
					}

					/*if(passType==2){
						Intent openStartingPoint = new Intent("com.android.dnh.moneykeeper.MYMAIN");// This using action
						startActivity(openStartingPoint);
					}*/
					
				}else{
					Dialog d = new Dialog(this);
					d.setTitle("Wrong Password Inputted !");
					d.show();
					tvWrongPass.setVisibility(View.VISIBLE);
					tvWrongPass.setText("Wrong Password! Attempt: " +attempt);
					tvWrongPass.setTextColor(Color.RED);
					if(attempt==10){
						tvWrongPass.setText("You probably don't know the password :(");
					}
					attempt++;
					
				}
			
			}
			break;
		
		}
	}
	private int checkPassword(){
		SharedPreferences mySetting1 = this.getSharedPreferences(key_PasswordPref, MODE_PRIVATE);
		int oldsaveMode = mySetting1.getInt(key_TypePass, 0);
		return oldsaveMode;
	}
	
	@Override
	public void onBackPressed() {// make back button same as pressing home
		// TODO Auto-generated method stub
		Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

	}
	
}
