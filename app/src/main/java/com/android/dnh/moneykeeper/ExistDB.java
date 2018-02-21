package com.android.dnh.moneykeeper;

import java.io.File;


import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExistDB extends ListActivity {

	final String key_currentDBPref = "mydbpref";
	final String key_DBdirectory = "dbdirectory";

	
	String directories[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initialize();
		setListAdapter(new ArrayAdapter<String>(ExistDB.this,
				android.R.layout.simple_list_item_1, directories));
	}
	private void initialize() {
		// TODO Auto-generated method stub
		String path = Environment.getExternalStorageDirectory() + "/MoneyKeeperDB/MainDB";
		File f = new File(path);        
		directories = f.list();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		String myDirectory = directories[position];//
		//saveDBDirectory(myDirectory);
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result",myDirectory);
		setResult(RESULT_OK,returnIntent);  
		finish();
	}
	
	
	/*private void saveDBDirectory(String directoryPath) {

		SharedPreferences currentDB = this.getSharedPreferences(key_currentDBPref, MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = currentDB.edit();
		prefEditor.putString(key_DBdirectory, directoryPath);
		prefEditor.commit();

	}*/

}
