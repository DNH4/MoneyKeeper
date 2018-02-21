package com.android.dnh.moneykeeper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

	// Declare # of ViewPager Page
	final int PAGE_COUNT = 3;
	
	// Setup Fragment
	Fragment historyF,keeperF,CustomF;

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		// Fragment fragment = new Dummy_Fragment();
		// Bundle args = new Bundle();
		switch (arg0) {

		case 0:
			historyF = new History_Fragment();
			// args.putInt(Dummy_Fragment.ARG_SECTION_NUMBER, 3);
			// frag1.setArguments(args);
			return historyF;

		case 1:
			keeperF = new Keeper_Fragment();
			// args.putInt(Dummy_Fragment.ARG_SECTION_NUMBER, 1);
			// frag2.setArguments(args);
			return keeperF;

		case 2:
			CustomF = new Custom_Fragment();
			// args.putInt(Dummy_Fragment.ARG_SECTION_NUMBER, 2);
			// frag3.setArguments(args);
			return CustomF;
		default:
			return null;
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;
	}
	
	@Override
	public int getItemPosition(Object item) {
		if (item instanceof Keeper_Fragment) {
	        ((Keeper_Fragment) item).update();
	    }
	    //don't return POSITION_NONE, avoid fragment recreation. 
	    return super.getItemPosition(item);
    }
	

}
