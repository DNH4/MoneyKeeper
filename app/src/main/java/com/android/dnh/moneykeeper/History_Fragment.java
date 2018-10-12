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

public class History_Fragment extends Fragment implements OnItemSelectedListener, OnClickListener{
	public static final String TAG = "History_Fragment";
	
	View view;
	String data[],dataRow[],dataDate[],dataCategory[],dataNote[],dataCurrency[],myChildren[][],dataAmount[];
	//float dataAmount[];
	float totalMoney,totalIncome,totalExpense;
	Spinner dateFilter1,categoryFilter1;
	ArrayAdapter<String> adapterCatergory,adapterDate;
	String[] categories;
	TextView tvDate,tvBalance,tvBalance2,tvIncome,tvExpense;
	int current_day,current_month,current_year,DayOfWeek;
	
	String chosenCategory="0";// Default to select all
	int chosenDateBegin=0,chosenDateEnd=0;//Default to select all
	int myPos1b=0,myPos2b=0;
	int mygroupPos;
	final String key_PasswordPref = "passpref";
	Button bNext,bPrev;

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		myPos1b =dateFilter1.getSelectedItemPosition();
		myPos2b =categoryFilter1.getSelectedItemPosition();
		//setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		Log.i(TAG,"onCreateView");
		view = inflater.inflate(R.layout.history_fragment, container,false);
		//initialize();
		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		tvDate = (TextView)view.findViewById(R.id.tvHistDate);
		tvBalance= (TextView)view.findViewById(R.id.tvHistBalance);
		tvBalance2= (TextView)view.findViewById(R.id.tvHistBalance2);
		tvIncome= (TextView)view.findViewById(R.id.tvHistIncome);
		tvExpense= (TextView)view.findViewById(R.id.tvHistExpense);
		
		tvBalance.setBackgroundColor(Color.DKGRAY);
		tvBalance.setTextColor(Color.WHITE);
		tvBalance2.setBackgroundColor(Color.DKGRAY);
		tvBalance2.setTextColor(Color.WHITE);
		tvIncome.setBackgroundColor(Color.DKGRAY);
		tvIncome.setTextColor(Color.rgb(60, 200, 20));
		tvExpense.setBackgroundColor(Color.DKGRAY);
		tvExpense.setTextColor(Color.RED);
		
		dateFilter1 = (Spinner) getActivity().findViewById(R.id.SDate1);
		categoryFilter1 = (Spinner) getActivity().findViewById(R.id.SCategory1);	
		
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
		List<String> catB = new ArrayList<String>();
		List<String> cat2B = new ArrayList<String>();
		adapterCatergory = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, catB);
		adapterDate = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cat2B);
		// Specify the layout to use when the list of choices appears
		adapterCatergory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		// adapterCategory.add("TEST");
		adapterCatergory.add("All Categories");
		for (int i = 1; i < (categories.length); i++) {// TODO Move to Different location to ini everytime update
			adapterCatergory.add(categories[i]);
		};
		adapterDate.add("Day");
		adapterDate.add("Month");
		adapterDate.add("Year");
		adapterDate.add("All Time");
		// tvDate.setText(categories[0]+" + "+ categories.length);
		// set spinner position
		dateFilter1.setSelection(myPos1b);
		categoryFilter1.setSelection(myPos2b);
		
		categoryFilter1.setAdapter(adapterCatergory);
		//categoryFilter1.setOnItemSelectedListener(this);
		dateFilter1.setAdapter(adapterDate);
		
		
		createList(0,0,"0");
		
		/*ExpandableListView elv = (ExpandableListView) view.findViewById(R.id.expandableListView1);
		elv.setAdapter(new MyExpandableListAdapter());*/
		
		//--------------------------
					
		categoryFilter1.setOnItemSelectedListener(this);
		dateFilter1.setOnItemSelectedListener(this);
		
		//--------
		currentDate();
		//Button for prev next
		bNext = (Button) view.findViewById(R.id.BtNext);
		bPrev = (Button) view.findViewById(R.id.BtPrev);
		
		bNext.setOnClickListener(this);
		bPrev.setOnClickListener(this);// implement ontouch for automatic increment if press long time

	}
	
	private void currentDate(){
		Calendar c = Calendar.getInstance();
		current_day = c.get(Calendar.DAY_OF_MONTH);
		current_month = c.get(Calendar.MONTH)+1;// months is from 0-11
		current_year = c.get(Calendar.YEAR);
		DayOfWeek = c.get(Calendar.DAY_OF_WEEK);
	}
	
	private void mySum2(int dateBegin,int dateEnd,String cate){		
		MyMoney info = new MyMoney(this.getActivity());
		info.open();
		//totalMoney = info.totalMoney(0,0,"0");//begin is smaller date to higher date
		//totalMoney = info.totalMoney(0,20130812,"Entertainment");
		String[] dataAmount2 = (info.getData(3,dateBegin,dateEnd,cate));
		info.close();
		totalMoney = Total_Money.mySum(dataAmount2);
		totalIncome =Total_Money.mySumI(dataAmount2);
		totalExpense = Total_Money.mySumE(dataAmount2);
		tvBalance.setText(String.format(" %.2f $", totalMoney));
        if(totalMoney<0){
        	tvBalance.setTextColor(Color.RED);
        }else{
        	tvBalance.setTextColor(Color.rgb(60, 200, 20));
        }
        tvIncome.setText(String.format(" %.2f $", totalIncome));
        tvExpense.setText(String.format(" %.2f $", totalExpense));
	}
	
	private void createList(int dateBegin,int dateEnd,String cate){

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
		
		//tvDate.setText(data[0][data[0].length-1]+" " + data[1][data[0].length-1]+" " + data[2][data[0].length-1]+" " + data[3][data[0].length-1]+" " + data[4][data[0].length-1]+" " + data[5][data[0].length-1]);//getting the rowid for the last entry
		//tvDate.setText(data[data.length-1]);
/*		if(data.length>0){
			//tvDate.setText(data[data.length-1]);
			tvDate.setText("Date");
		}else{
			tvDate.setText("No Data");
		}*/
		ExpandableListView elv = (ExpandableListView) view.findViewById(R.id.expandableListView1);
		elv.setAdapter(new MyExpandableListAdapter());
	}
	
	
	// adapter for expandable list
	public class MyExpandableListAdapter extends BaseExpandableListAdapter implements OnClickListener{
		//Example...
		 //private String[] groups = { "People Names", "Dog Names", "Cat Names", "Fish Names","test" };
		 
		 /*private String[][] children = {
		 { "Arnold", "Barry", "Chuck", "David" },
		 { "Ace", "Bandit", "Cha-Cha", "Deuce" },
		 { "Fluffy", "Snuggles" },
		 { "Goldy", "Bubbles" }
		 };*/
		//MOdified get group for 2d to add more field to it
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
	        LayoutInflater inflater = History_Fragment.this.getActivity().getLayoutInflater();
	 
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
	            LayoutInflater infalInflater = (LayoutInflater) History_Fragment.this.getActivity()
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

	// For Spinner
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		
		switch(arg0.getId()){
		case R.id.SCategory1:
			 String selected = arg0.getItemAtPosition(position).toString();
			 if(selected.equals("All Categories")){
		        	chosenCategory = "0";
					mySum2(chosenDateBegin,chosenDateEnd,chosenCategory);
		        	createList(chosenDateBegin,chosenDateEnd,chosenCategory);  
		        }else{
		        	chosenCategory = selected;
		        	mySum2(chosenDateBegin,chosenDateEnd,chosenCategory);
		        	createList(chosenDateBegin,chosenDateEnd,chosenCategory);
		        }
			break;
		case R.id.SDate1:
			String selected2 = arg0.getItemAtPosition(position).toString();
			if(selected2.equals("Day")){
				currentDate();//Reset date each time
				myDateDisplay(0);
	        }else if(selected2.equals("Month")){
	        	currentDate();
	        	myDateDisplay(1);
	        }else if(selected2.equals("Year")){
	        	currentDate();
	        	myDateDisplay(2);
	        }else if(selected2.equals("All Time")){
	        	currentDate();
	        	myDateDisplay(3);
	        }
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case(R.id.BtNext):
			switch(dateFilter1.getSelectedItemPosition())
			{
			case 0:
				if(current_day < 31)
				{
					current_day = current_day + 1;
				}
				else
				{
					current_day = 1;
					if(current_month < 12)
					{
						current_month = current_month + 1;
					}
					else
					{
						current_month = 1;
						current_year = current_year + 1;
					}
				}
				DayOfWeek = myDateFormat.dayofweekChange(0, DayOfWeek);
				myDateDisplay(0);
				break;
			case 1:
				if(current_month < 12)
				{
					current_month = current_month + 1;
				}
				else
				{
					current_month = 1;
					current_year = current_year + 1;
				}
				myDateDisplay(1);
				break;
			case 2:
				current_year = current_year + 1;
				myDateDisplay(2);
				break;
			}
			break;
		case(R.id.BtPrev):
			switch(dateFilter1.getSelectedItemPosition())
			{
			case 0:
				if(current_day > 1)
				{
					current_day = current_day - 1;
				}
				else
				{
					current_day = 31;
					if(current_month > 1)
					{
						current_month = current_month - 1;
					}
					else
					{
						current_month = 12;
						current_year = current_year - 1;
					}
				}
				DayOfWeek = myDateFormat.dayofweekChange(1, DayOfWeek);
				myDateDisplay(0);
				break;
			case 1:
				if(current_month > 1)
				{
					current_month = current_month - 1;
				}
				else
				{
					current_month = 12;
					current_year = current_year - 1;
				}
				myDateDisplay(1);
				break;
			case 2:
				current_year = current_year - 1;
				myDateDisplay(2);
				break;
			}
			break;
		}
	}
	
	private void myDateDisplay(int i){
		switch(i){
		case 0://Day
			chosenDateBegin = current_year * 100 * 100 + (current_month) * 100 + current_day;// set the date in int form YYYYMMDD
			chosenDateEnd = current_year * 100 * 100 + (current_month) * 100 + current_day;
			mySum2(chosenDateBegin,chosenDateEnd,chosenCategory);
        	createList(chosenDateBegin,chosenDateEnd,chosenCategory);
        	tvDate.setText(myDateFormat.dayofweekConvert(DayOfWeek)+", "+myDateFormat.monthConvert(current_month)+" " +current_day + " " + current_year);
        	bNext.setVisibility(View.VISIBLE);
        	bPrev.setVisibility(View.VISIBLE);
			break;
		case 1://Month
			chosenDateBegin = current_year * 100 * 100 + (current_month) * 100 + 0;// set the date in int form YYYYMMDD
			chosenDateEnd = current_year * 100 * 100 + (current_month) * 100 + 32;
        	mySum2(chosenDateBegin,chosenDateEnd,chosenCategory);
        	createList(chosenDateBegin,chosenDateEnd,chosenCategory);
        	tvDate.setText(myDateFormat.monthConvert(current_month) + " " + current_year);
        	bNext.setVisibility(View.VISIBLE);
        	bPrev.setVisibility(View.VISIBLE);
			break;
		case 2://Year
			chosenDateBegin = current_year * 100 * 100 + 0 * 100 + 0;// set the date in int form YYYYMMDD
			chosenDateEnd = current_year * 100 * 100 + 12 * 100 + 32;
        	mySum2(chosenDateBegin,chosenDateEnd,chosenCategory);
        	createList(chosenDateBegin,chosenDateEnd,chosenCategory);
        	tvDate.setText(""+current_year);
        	bNext.setVisibility(View.VISIBLE);
        	bPrev.setVisibility(View.VISIBLE);
			break;
		case 3://All time
			chosenDateBegin = chosenDateEnd = 0;
        	mySum2(chosenDateBegin,chosenDateEnd,chosenCategory);
        	createList(chosenDateBegin,chosenDateEnd,chosenCategory);
        	tvDate.setText("All Time");
        	bNext.setVisibility(View.INVISIBLE);
        	bPrev.setVisibility(View.INVISIBLE);
			break;
		}
		
	}
	
}
