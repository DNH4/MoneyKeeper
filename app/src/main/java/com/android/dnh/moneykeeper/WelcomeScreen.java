package com.android.dnh.moneykeeper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class WelcomeScreen  extends Activity{
	final String key_PasswordPref = "passpref";
	final String key_TypePass = "typepass";// 0 for no pass, 1 for restriction 1, 2 for.. 2
	final String key_CurrentPass ="currentpass";
	int passwordMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
		
		checkPassword();
		
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(1000);// try sleep 5s

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {// after try and catch then :
					if(passwordMode == 2){//if restrict all of app content
						Intent openStartingPoint = new Intent("com.android.dnh.moneykeeper.CHECKPASS");// This using action
						openStartingPoint.putExtra("welcome", 1);
						startActivity(openStartingPoint);
					}else{
						Intent openStartingPoint = new Intent("com.android.dnh.moneykeeper.MYMAIN");// This using action
						startActivity(openStartingPoint);
					}
				}

			}
		};
		timer.start();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	private void checkPassword(){
		SharedPreferences mySetting1 = this.getSharedPreferences(key_PasswordPref, MODE_PRIVATE);
		passwordMode = mySetting1.getInt(key_TypePass, 0);
		
/*		if(passwordMode ==1){
			SharedPreferences mySetting = this.getSharedPreferences(key_PasswordPref, MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = mySetting.edit();
			prefEditor.putInt("Restriction1Lock", 1);//lock app to restriction 1
			prefEditor.commit();
		}else{

		}*/
	}

}
