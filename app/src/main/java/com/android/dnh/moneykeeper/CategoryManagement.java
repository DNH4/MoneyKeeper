package com.android.dnh.moneykeeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryManagement extends Activity implements OnItemClickListener {
	
	String myCategory[],myRowID[];
	MyCategoriesList mcl = new MyCategoriesList(this);
	ListView listview;
	ArrayAdapter<String> adapterCatergory;
	final String key = "categoryID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_management);
		initialize();
		loadData();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.categoryman_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.MCancel:
			finish();
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initialize(){
		loadData();
		listview = (ListView) findViewById(R.id.listView1);
		
		//adapterCatergory = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myCategory);
		adapterCatergory = new MyListViewAdapter(this, myCategory);
/*	    final MyListViewAdapter adapter = new MyListViewAdapter(this,
	        android.R.layout.simple_list_item_1, list);*/
	    listview.setAdapter(adapterCatergory);
	    listview.setOnItemClickListener(this);

	}
	
	private void loadData(){
		mcl.open();
/*		int a = mcl.loadData(1).length;// Check if already create table with default value
		if (a == 0) {//if less than 4 entries TO BE CHANGE
			mcl.saveData("New Category...");
			mcl.saveData("Uncategorized");
			mcl.saveData("Food");
			mcl.saveData("Entertainment");
		}*/
		myCategory = mcl.loadData(1); //TODO Need to move some where
		myRowID = mcl.loadData(0); 
		mcl.close();
	}
	
	 private class MyListViewAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;
		public MyListViewAdapter(Context context, String[] values) {
			super(context, R.layout.cate_list_item, values);
			// TODO Auto-generated constructor stub
			this.context = context;
			this.values = values;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View myView = inflater.inflate(R.layout.cate_list_item, parent, false);
			
			TextView tvCate = (TextView) myView.findViewById(R.id.tvCate);
			ImageView ivCateIcon = (ImageView) myView.findViewById(R.id.ivCateIcon);
			
			tvCate.setText(values[position]);//set text to category that passed to adapter
			
			//set icon
			String cateName = values[position];
			if(cateName.startsWith("New Category")){
				ivCateIcon.setImageResource(R.drawable.content_new);
			}else if(cateName.startsWith("Uncategorized")){
				ivCateIcon.setImageResource(R.drawable.category_uncategorize);
			}else if(cateName.startsWith("Food")){
				ivCateIcon.setImageResource(R.drawable.category_food);
			}else if(cateName.startsWith("Clothing")){
				ivCateIcon.setImageResource(R.drawable.category_clothing);
			}else if(cateName.startsWith("Entertainment")){
				ivCateIcon.setImageResource(R.drawable.category_entertainment);
			}else if(cateName.startsWith("Rent")){
				ivCateIcon.setImageResource(R.drawable.category_rent);
			}else if(cateName.startsWith("Salary")){
				ivCateIcon.setImageResource(R.drawable.category_income);
			}else if(cateName.startsWith("Grocery")){
				ivCateIcon.setImageResource(R.drawable.category_grocery);
			}else if(cateName.startsWith("Gift")){
				ivCateIcon.setImageResource(R.drawable.category_gift);
			}else{//already set to default at xml
				ivCateIcon.setImageResource(R.drawable.category_default);
			}						
			return myView;
		}
		
		    
	 }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		
		//long a = arg0.getItemIdAtPosition(position);
		//Toast.makeText(this, "Test:"  + id, Toast.LENGTH_LONG).show();
		if(id == 0){//
			Intent i= new Intent("com.android.dnh.moneykeeper.NEWCATEGORY");
			//startActivity(i);
			startActivityForResult(i, 1);
		}else{
			//Toast.makeText(this, "Category:"  + myCategory[(int) id], Toast.LENGTH_SHORT).show();//Work
			Intent i= new Intent("com.android.dnh.moneykeeper.CATEGORYEDIT");
			i.putExtra(key, myRowID[position]);
			//i.putExtra(key, "2");
			//startActivity(i);
			startActivityForResult(i, 1);
		}	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		         initialize();          
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		}
	}

	
	

}
