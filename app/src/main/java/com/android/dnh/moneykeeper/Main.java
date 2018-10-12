package com.android.dnh.moneykeeper;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Context;
import android.content.SharedPreferences;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class Main extends FragmentActivity implements TabListener {
	
	
	int passwordMode,securityMode;
	int fromWelcome=0;
	final String key_PasswordPref = "passpref";
	final String key_TypePass = "typepass";// 0 for no pass, 1 for restriction 1, 2 for.. 2
	final String key_CurrentPass ="currentpass";
	final String key_SecurityMode ="security";

	// private static final int NUM_PAGES = 3;
	int KeeperToCustom=0;//flag 0 

	ViewPager mViewPager;
	MyPagerAdapter mPagerAdapter;

	// private PagerAdapter mPagerAdapter;
	// private MyPagerAdapter mPagerAdapter;
	
/*	Spinner dateFilter,categoryFilter;
	ArrayAdapter<String> adapterCategory,adapterDate;
	String[] categories;*/
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(securityMode ==1){
			checkPassword();}
		super.onStop();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// checkpass
		checkPassword();
		// Get view from xml
		setContentView(R.layout.activity_main);

		// Set up the action bar for tab
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);// using tab navigation
		actionBar.setDisplayOptions(Window.FEATURE_NO_TITLE);
		// actionBar.setTitle("Keeper");// set title in action bar, default is Name of the app

		// Set up pager for swiping
		// mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());// using my custome adapter
		mViewPager = (ViewPager) findViewById(R.id.pager);

		// mPagerAdapter = new ScreenSlidePagerAdapter();

		// Setting up fragment, activate fragment manager
		FragmentManager fm = getSupportFragmentManager();

		// Capture ViewPager page swipes
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// Getting our custom adapter pager class and set it was the ViewPager adapter
		mPagerAdapter = new MyPagerAdapter(fm);
		mViewPager.setAdapter(mPagerAdapter);
		//mViewPager.setOffscreenPageLimit(0);
		// add tab to action bar ** need to put after setup pager
		ActionBar.Tab keeperTab = actionBar.newTab().setText("Keeper").setTabListener(this);// pos 1
		ActionBar.Tab historyTab = actionBar.newTab().setText("History").setTabListener(this);// pos 2
		ActionBar.Tab customTab = actionBar.newTab().setText("Custom").setTabListener(this);// pos 3

		actionBar.addTab(historyTab);// add tab pos 0
		actionBar.addTab(customTab);// add tab pos 1 after change to 2
		actionBar.addTab(keeperTab, 1, true);// add tab as seletected and pos 1
		
		//------------------------------------------
		MyCategoriesList mcl = new MyCategoriesList(this);
		mcl.open();
		int a = mcl.loadData(1).length;// Check if already create table with default value

		if (a == 0) {//if less than 4 entries TO BE CHANGE
			mcl.saveData("New Category...");
			mcl.saveData("Uncategorized");
			mcl.saveData("Food");
			mcl.saveData("Clothing");
			mcl.saveData("Entertainment");
			mcl.saveData("Rent");
			mcl.saveData("Salary");
			mcl.saveData("Grocery");
			mcl.saveData("Gift");
		}
		mcl.close();
		
		
/*		SharedPreferences mySetting = this.getSharedPreferences("mygoal", MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = mySetting.edit();
		prefEditor.putInt("goaledited", 0);
		prefEditor.commit();*/
		
	}


	@Override
	public void onTabSelected(Tab arg0, android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		// Pass the position on tab click to ViewPager
		mViewPager.setCurrentItem(arg0.getPosition());
		

		//Toast.makeText(getApplicationContext(), ""+KeeperToCustom, Toast.LENGTH_SHORT).show();

		if(arg0.getPosition()==1){
			SharedPreferences mySetting = this.getSharedPreferences("mygoal", MODE_PRIVATE);
			KeeperToCustom = mySetting.getInt("goaledited",0);
			if(KeeperToCustom == 1){
				//Toast.makeText(getApplicationContext(), "do stuff", Toast.LENGTH_SHORT).show();
				/*Keeper_Fragment fragment = (Keeper_Fragment) getFragmentManager().findFragmentByTag(tag);
			    fragment.update();*/
				//mViewPager.dostuff();
				mPagerAdapter.notifyDataSetChanged();//IMPORTANT
				KeeperToCustom =0;//set flag
				SharedPreferences.Editor prefEditor = mySetting.edit();
				prefEditor.putInt("goaledited", 0);
				prefEditor.commit();
			}
		}
	}

	@Override
	public void onTabReselected(Tab arg0, android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		// Pass the position on tab click to ViewPager
		// mViewPager.setCurrentItem(arg0.getPosition());		
	}

	@Override
	public void onTabUnselected(Tab arg0, android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
	
	private void checkPassword(){
		/*SharedPreferences mySetting1 = this.getActivity().getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
		check = mySetting1.getInt("Restriction1Lock", 0);*/
		SharedPreferences mySetting1 = this.getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
		passwordMode = mySetting1.getInt(key_TypePass, 0);
		securityMode = mySetting1.getInt(key_SecurityMode, 0);
		fromWelcome++;
		if(passwordMode ==1){
			SharedPreferences mySetting = this.getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = mySetting.edit();
			prefEditor.putInt("Restriction1Lock", 1);//lock app to restriction 1
			prefEditor.commit();
		}//else if(passwordMode ==2 && fromWelcome !=1){
			else if(passwordMode ==2 && securityMode==1){
			/*Intent openStartingPoint = new Intent("com.android.dnh.moneykeeper.CHECKPASS");// This using action
			openStartingPoint.putExtra("welcome", 0);
			startActivity(openStartingPoint);
*/
		}else{
			SharedPreferences mySetting = this.getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = mySetting.edit();
			prefEditor.putInt("Restriction1Lock", 0);//lock app to no restsriction
			prefEditor.commit();
		}
	}
}
