package com.android.dnh.moneykeeper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Keeper_Fragment extends Fragment implements OnClickListener, OnItemSelectedListener {

	View view;
	String data[],dataRow[],dataDate[],dataCategory[],dataNote[],dataCurrency[],myChildren[][],dataAmount[];
	float totalMoney,totalExpense;
	public static final String TAG = "KeeperFrag";
	Spinner dateFilter,categoryFilter;
	TextView tvTotalMoney,tvGoal,tvTotalMoney2,tvGoal2;
	String chosenCategory="0";// Default to select all
	int chosenDateBegin,chosenDateEnd;//Default to select all
	ArrayAdapter<String> adapterCatergory,adapterDate;
	String[] categories;
	int current_day,current_month,current_year;
	int myPos1=1,myPos2=0;
	Button bNewEntry;
	int mygroupPos;
	float myDailyGoal,myMonthlyGoal,myYearlyGoal;
	int changeGoal=1;
	int passwordMode;
	int fromWelcome=0;
	final String key_PasswordPref = "passpref";

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		myPos1 =dateFilter.getSelectedItemPosition();
		myPos2=categoryFilter.getSelectedItemPosition();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.keeper_fragment, container,false);
		Log.i(TAG,"OncreateView");
		//checkPassword();
		return view;
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		//checkPassword();
		super.onPause();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();	
		initialize();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	private void initialize() {
		// TODO Auto-generated method stub
		bNewEntry = (Button) view.findViewById(R.id.bNewTrans);
		tvTotalMoney = (TextView) view.findViewById(R.id.tvTotalMoney);
		tvGoal = (TextView) view.findViewById(R.id.tvGoal);
		tvTotalMoney2 = (TextView) view.findViewById(R.id.tvTotalMoney2);
		tvGoal2 = (TextView) view.findViewById(R.id.tvGoal2);

        tvTotalMoney.setBackgroundColor(Color.DKGRAY);
        tvTotalMoney2.setTextColor(Color.WHITE);
        tvTotalMoney2.setBackgroundColor(Color.DKGRAY);
        tvGoal.setBackgroundColor(Color.DKGRAY);    
        tvGoal2.setTextColor(Color.WHITE);
        tvGoal2.setBackgroundColor(Color.DKGRAY);
		//mySum(0,0,"0");
		
		bNewEntry.setOnClickListener(this);
		dateFilter = (Spinner) getActivity().findViewById(R.id.SDate2);
		categoryFilter = (Spinner) getActivity().findViewById(R.id.SCategory2);		
		
		MyCategoriesList mcl = new MyCategoriesList(this.getActivity());
		mcl.open();
		/*int a = mcl.loadData().length;// Check if already create table with default value

		if (a == 0) {//if less than 4 entries TO BE CHANGE
			mcl.saveData("New Category...");
			mcl.saveData("Uncategorized");
			mcl.saveData("Food");
			mcl.saveData("Entertainment");
		}*/
		categories = mcl.loadData(1); //TODO Need to move some where
		mcl.close();
		List<String> cat = new ArrayList<String>();
		List<String> cat2 = new ArrayList<String>();
		adapterCatergory = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cat);
		adapterDate = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cat2);
		// Specify the layout to use when the list of choices appears
		adapterCatergory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		// adapterCategory.add("TEST");
		adapterCatergory.add("All Categories");
		for (int i = 1; i < (categories.length); i++) {// TODO Move to Different location to ini everytime update
			adapterCatergory.add(categories[i]);
		};
		
		adapterDate.add("Today");
		adapterDate.add("This Month");
		adapterDate.add("This Year");
		// tv1.setText(categories[0]+" + "+ categories.length);
		categoryFilter.setAdapter(adapterCatergory);
		//categoryFilter.setOnItemSelectedListener(this);
		dateFilter.setAdapter(adapterDate);
				
		
		categoryFilter.setOnItemSelectedListener(this);
		dateFilter.setOnItemSelectedListener(this);
		// set spinner position
		dateFilter.setSelection(myPos1);
		categoryFilter.setSelection(myPos2);
		// pick only for today
		Calendar c = Calendar.getInstance();
		current_day = c.get(Calendar.DAY_OF_MONTH);
		current_month = c.get(Calendar.MONTH)+1;// months is from 0-11
		current_year = c.get(Calendar.YEAR);
		//chosenDateBegin = current_year * 100 * 100 + current_month * 100 + current_day;// set the date in int form YYYYMMDD
		//chosenDateEnd = current_year * 100 * 100 + current_month * 100 + current_day;
		
	}
	
	private void mySum(int dateBegin,int dateEnd,String cate){		
		MyMoney info = new MyMoney(this.getActivity());
		info.open();
		//totalMoney = info.totalMoney(0,0,"0");//begin is smaller date to higher date
		//totalMoney = info.totalMoney(0,20130812,"Entertainment");
		totalMoney = Total_Money.mySum(info.getData(3,dateBegin,dateEnd,cate));
		totalExpense = Total_Money.mySumE(info.getData(3,dateBegin,dateEnd,cate));
		info.close();
		tvTotalMoney.setText(String.format("%.2f $", totalMoney));
        if(totalMoney<0){
        	tvTotalMoney.setTextColor(Color.RED);
        }else{
        	tvTotalMoney.setTextColor(Color.rgb(60, 200, 20));
        }  
        /*Spannable wordtoSpan2 = new SpannableString("Goal: "+a);
        wordtoSpan2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan2.setSpan(new ForegroundColorSpan(Color.YELLOW), 6, 6+Integer.toString(a).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        loadPref();
        float mytotalExpense = totalExpense;
        if(mytotalExpense<0){
        	mytotalExpense = -1*mytotalExpense;
        }
        tvGoal.setText(mytotalExpense+" $/"+myGoal(changeGoal)+" $");
        //tvGoal.setText("0");
        if((mytotalExpense)>myGoal(changeGoal)){
        	tvGoal.setTextColor(Color.rgb(255, 80, 0));
        }else{
        	tvGoal.setTextColor(Color.YELLOW);
        }
	}
	private float myGoal(int i){
		float a=0;
		switch(i){
		case 0:
			a = myDailyGoal;
			break;
		case 1:
			a = myMonthlyGoal;
			break;
		case 2:
			a = myYearlyGoal;
			break;
		}
		return a;
	}
	
	private void loadPref() {
		// TODO Auto-generated method stub	
		SharedPreferences mySetting = this.getActivity().getSharedPreferences("mygoal", Context.MODE_PRIVATE);
		myDailyGoal = mySetting.getFloat("dailygoal",0); 
		myMonthlyGoal = mySetting.getFloat("monthlygoal",0);
		myYearlyGoal = mySetting.getFloat("yearlygoal",0);
	}

	
	private void createList2(int dateBegin,int dateEnd,String cate){
		//TextView tv1 = (TextView)view.findViewById(R.id.tvHistory);
		MyMoney info = new MyMoney(this.getActivity());
		info.open();
		data = info.getData(0,dateBegin,dateEnd,cate);
		dataAmount = info.getData(3,dateBegin,dateEnd,cate);
		dataNote = info.getData(5,dateBegin,dateEnd,cate);
		dataDate = info.getData(2,dateBegin,dateEnd,cate);
		dataCategory = info.getData(4,dateBegin,dateEnd,cate);
		dataRow = info.getData(1,dateBegin,dateEnd,cate);
		myChildren = new String[dataNote.length][1];
		info.close();
		for(int i = 0;i<dataNote.length;i++){
			myChildren[i][0]= dataNote[i];
		}
		
		//tv1.setText(data[0][data[0].length-1]+" " + data[1][data[0].length-1]+" " + data[2][data[0].length-1]+" " + data[3][data[0].length-1]+" " + data[4][data[0].length-1]+" " + data[5][data[0].length-1]);//getting the rowid for the last entry
		//tv1.setText(data[data.length-1]);
	/*	if(data.length>0){
			tv1.setText(data[data.length-1]);
		}else{
			tv1.setText("No Data");
		}*/
		ExpandableListView elv = (ExpandableListView) view.findViewById(R.id.expandableListView2);
		elv.setAdapter(new MyExpandableListAdapter2());
	}
	
	// adapter for expandable list
		public class MyExpandableListAdapter2 extends BaseExpandableListAdapter implements OnClickListener{

			private String[] groups = dataAmount;
			private String[][] children=myChildren;
			//-------------------------------
			@Override
			public Object getChild(int groupPos, int childPos) {
				// TODO Auto-generated method stub
				return children[groupPos][childPos];
			}

			@Override
			public long getChildId(int groupPos, int childPos) {
				// TODO Auto-generated method stub
				return childPos;
			}

			@Override
			public View getChildView(int groupPos, int childPos, boolean isLastChild, View elView, ViewGroup arg4) {
				// TODO Auto-generated method stub
				/*TextView tvChild = new TextView(History_Fragment.this.getActivity());
				tvChild.setText(getChild(groupPos,childPos).toString());
				return tvChild;*/
				mygroupPos = groupPos;
				final String noteData = (String) getChild(groupPos, childPos);
		        LayoutInflater inflater = Keeper_Fragment.this.getActivity().getLayoutInflater();
		 
		        if (elView == null) {
		        	elView = inflater.inflate(R.layout.child_item, null);
		        }
		 
		        TextView myNote = (TextView) elView.findViewById(R.id.tvNote);
		 
		        ImageView editButton = (ImageView) elView.findViewById(R.id.ivEdit);
		        editButton.setOnClickListener(this);
		        myNote.setText(noteData);
		        return elView;
			}

			@Override
			public int getChildrenCount(int groupPos) {
				// TODO Auto-generated method stub
				return children[groupPos].length;
			}

			@Override
			public Object getGroup(int groupPos) {//don't need to use anymore
				// TODO Auto-generated method stub
				return groups[groupPos];
			}

			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return groups.length;
			}

			@Override
			public long getGroupId(int groupPos) {
				// TODO Auto-generated method stub
				return groupPos;
			}

			@Override
			public View getGroupView(int groupPos, boolean childPos, View elView, ViewGroup arg3) {
				// TODO Auto-generated method stub
				/*TextView tvGroups = new TextView(History_Fragment.this.getActivity());
				tvGroups.setText(groups[groupPos].toString());
				return tvGroups;*/
				String myCategory = dataCategory[groupPos];
				String myDate = dataDate[groupPos];
				String myAmount = dataAmount[groupPos];
				float myAmount2;
		        if (elView == null) {
		            LayoutInflater infalInflater = (LayoutInflater) Keeper_Fragment.this.getActivity()
		                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            elView = infalInflater.inflate(R.layout.group_item,null);
		        }
		        TextView tvCategory = (TextView) elView.findViewById(R.id.tvCategory);
		        TextView tvDate = (TextView) elView.findViewById(R.id.tvDate);
		        TextView tvAmount = (TextView) elView.findViewById(R.id.tvAmount);
		        ImageView ivSymbol = (ImageView) elView.findViewById(R.id.ivCategorySymbol);
		        
		        tvCategory.setText(myCategory);
		        tvCategory.setTypeface(null,Typeface.BOLD);
		        tvDate.setText(myDateFormat.parseDate(Integer.parseInt(myDate)));
		        tvDate.setTextColor(Color.GRAY);
		        
		        if(myCategory.equals("Uncategorized")){
		        	ivSymbol.setImageResource(R.drawable.category_uncategorize);
		        }else if(myCategory.equals("Food")){
		        	ivSymbol.setImageResource(R.drawable.category_food);
		        }else if(myCategory.equals("Clothing")){
		        	ivSymbol.setImageResource(R.drawable.category_clothing);
		        }else if(myCategory.equals("Entertainment")){
		        	ivSymbol.setImageResource(R.drawable.category_entertainment);
		        }else if(myCategory.equals("Rent")){
		        	ivSymbol.setImageResource(R.drawable.category_rent);
		        }else if(myCategory.equals("Salary")){
		        	ivSymbol.setImageResource(R.drawable.category_income);
		        }else if(myCategory.equals("Grocery")){
		        	ivSymbol.setImageResource(R.drawable.category_grocery);
		        }else if(myCategory.equals("Gift")){
		        	ivSymbol.setImageResource(R.drawable.category_gift);
		        }else{//Default
		        	ivSymbol.setImageResource(R.drawable.category_default);
		        }
		        
		        
		        myAmount2 = Float.parseFloat(myAmount);
		        tvAmount.setText(String.format("%.2f $", myAmount2));
		        if(myAmount2<0){
		        	tvAmount.setTextColor(Color.RED);
		        }else{
		        	tvAmount.setTextColor(Color.rgb(60, 200, 20));
		        }
		        //we Can make a string add in position to add more text view field
		        //make global field use groupPos to change stuff
		        return elView;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean isChildSelectable(int groupPos, int childPos) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch(arg0.getId()){
				case R.id.ivEdit:
					SharedPreferences mySetting1 = getActivity().getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
					int check = mySetting1.getInt("Restriction1Lock", 0);
					if(check==0){//if not lock then continue
						Intent i= new Intent("com.android.dnh.moneykeeper.EDITTRANSACTION");
						i.putExtra("rowid", dataRow[mygroupPos]);
						//i.putExtra("rowid", "4");
						startActivity(i);
					}else{	
						Intent openStartingPoint = new Intent("com.android.dnh.moneykeeper.CHECKPASS");// This using action
						openStartingPoint.putExtra("welcome", 0);
						startActivity(openStartingPoint);
					}
					break;
				}
			}
			
		}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//temp cuz only 1 button
		//checkPassword();
		SharedPreferences mySetting1 = this.getActivity().getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
		int check = mySetting1.getInt("Restriction1Lock", 0);
		if(check==0){//if not lock then continue
			Intent i= new Intent("com.android.dnh.moneykeeper.ADDTRANSACTION");
			startActivity(i);
		}else{			
			Intent openStartingPoint = new Intent("com.android.dnh.moneykeeper.CHECKPASS");// This using action
			openStartingPoint.putExtra("welcome", 0);
			startActivity(openStartingPoint);
		}
		
	
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.SCategory2:
			 String selected = arg0.getItemAtPosition(position).toString();
		        if(selected.equals("All Categories")){
		        	chosenCategory = "0";
					mySum(chosenDateBegin,chosenDateEnd,chosenCategory);
		        	createList2(chosenDateBegin,chosenDateEnd,chosenCategory);  
		        }else{
		        	chosenCategory = selected;
		        	mySum(chosenDateBegin,chosenDateEnd,chosenCategory);
		        	createList2(chosenDateBegin,chosenDateEnd,chosenCategory);
		        }
			break;
		case R.id.SDate2:
			String selected2 = arg0.getItemAtPosition(position).toString();
			if(selected2.equals("Today")){
				changeGoal =0;
				chosenDateBegin = current_year * 100 * 100 + (current_month) * 100 + current_day;// set the date in int form YYYYMMDD
				chosenDateEnd = current_year * 100 * 100 + (current_month) * 100 + current_day;
				mySum(chosenDateBegin,chosenDateEnd,chosenCategory);
	        	createList2(chosenDateBegin,chosenDateEnd,chosenCategory);        
	        }else if(selected2.equals("This Month")){
	        	changeGoal =1;
	        	chosenDateBegin = current_year * 100 * 100 + (current_month) * 100 + 0;// set the date in int form YYYYMMDD
				chosenDateEnd = current_year * 100 * 100 + (current_month) * 100 + 32;
	        	mySum(chosenDateBegin,chosenDateEnd,chosenCategory);
	        	createList2(chosenDateBegin,chosenDateEnd,chosenCategory);
	        }else if(selected2.equals("This Year")){
	        	changeGoal =2;
	        	chosenDateBegin = current_year * 100 * 100 + 0 * 100 + 0;// set the date in int form YYYYMMDD
				chosenDateEnd = current_year * 100 * 100 + 12 * 100 + 32;
	        	mySum(chosenDateBegin,chosenDateEnd,chosenCategory);
	        	createList2(chosenDateBegin,chosenDateEnd,chosenCategory);
	        }
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	public void update() {
		// TODO Auto-generated method stub
		loadPref();
        float mytotalExpense = totalExpense;
        if(mytotalExpense<0){
        	mytotalExpense = -1*mytotalExpense;
        }
        tvGoal.setText(mytotalExpense+" $/"+myGoal(changeGoal)+" $");
        //tvGoal.setText("0");
        if((mytotalExpense)>myGoal(changeGoal)){
        	tvGoal.setTextColor(Color.rgb(255, 80, 0));
        }else{
        	tvGoal.setTextColor(Color.YELLOW);
        }
	}
	
	/*private void checkPassword(){
		SharedPreferences mySetting1 = this.getActivity().getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
		check = mySetting1.getInt("Restriction1Lock", 0);
		SharedPreferences mySetting1 = this.getActivity().getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
		passwordMode = mySetting1.getInt(key_TypePass, 0);
		fromWelcome++;
		if(passwordMode ==1){
			SharedPreferences mySetting = this.getActivity().getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = mySetting.edit();
			prefEditor.putInt("Restriction1Lock", 1);//lock app to restriction 1
			prefEditor.commit();
		}else if(passwordMode ==2 && fromWelcome !=1){
			Intent openStartingPoint = new Intent("com.android.dnh.moneykeeper.CHECKPASS");// This using action
			openStartingPoint.putExtra("welcome", 0);
			startActivity(openStartingPoint);
			SharedPreferences mySetting = this.getActivity().getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = mySetting.edit();
			prefEditor.putInt("Restriction1Lock", 1);//lock app to restriction 2
			prefEditor.commit();
		}else{
			SharedPreferences mySetting = this.getActivity().getSharedPreferences(key_PasswordPref, Context.MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = mySetting.edit();
			prefEditor.putInt("Restriction1Lock", 0);//lock app to no restsriction
			prefEditor.commit();
		}
	}*/

}
