package com.android.dnh.moneykeeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ImportExportDB extends Activity implements OnClickListener {

	final String key_currentDBPref = "mydbpref";
	final String key_DBdirectory = "dbdirectory";
	final String key_DBDefaultIni = "mydefaultdb";
	
	String string_currentDB;	

	Button bImport, bExport,bTest,bSaveCurrent,bDefault;
	TextView tvDBDirectory,tv1;
	int confirmMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.importexport);
		initialize();
	}

	public void initialize() {
		tvDBDirectory = (TextView) findViewById(R.id.tvCurrentDirectory);
		tv1 =(TextView) findViewById(R.id.textView1);
		//tv1.setBackgroundColor(Color.d)
		bImport = (Button) findViewById(R.id.bImport);
		bExport = (Button) findViewById(R.id.bExport);
		bTest = (Button) findViewById(R.id.bDeleteDB);
		bSaveCurrent = (Button)findViewById(R.id.bSaveCurrent);
		bDefault = (Button)findViewById(R.id.bDefault);
		bImport.setOnClickListener(this);
		bExport.setOnClickListener(this);
		bTest.setOnClickListener(this);
		bSaveCurrent.setOnClickListener(this);
		bDefault.setOnClickListener(this);

		if (MyDatabase.isExternalStorageWritable() == false) {
			if (MyDatabase.isExternalStorageReadable() == false) {
				Toast.makeText(getApplicationContext(),
						"External Storage is not Readable !",
						Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(getApplicationContext(),
					"External Storage is not Writable !", Toast.LENGTH_SHORT)
					.show();
		} else {
			// creating a new folder for the database to be backuped to
			// Can create empty initial database here!!!
			//if (CheckIni() == 0) {
				File MyDirectory = new File(
						Environment.getExternalStorageDirectory() + "/MoneyKeeperDB" + "/MainDB");
				File MyDirectoryCate = new File(
						Environment.getExternalStorageDirectory() + "/MoneyKeeperDB" + "/CategoryDB");
				if (!MyDirectory.exists()) {
					MyDirectory.mkdirs();
				}
				if (!MyDirectoryCate.exists()) {
					MyDirectoryCate.mkdirs();
				}
				// Create the base empty data base
				//exportDB("Defaulted");//create an empty db
			//}
		}
		currentDB();// Showing the directory of current chosen DB
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

/*	private int CheckIni() {//check if db directory and empty db is created 
		int check = 0;
		SharedPreferences currentDB = this.getSharedPreferences(key_currentDBPref, MODE_PRIVATE);
		check = currentDB.getInt(key_DBDefaultIni, 0);//if not created then check is 0
		if (check !=1){
			SharedPreferences.Editor prefEditor = currentDB.edit();
			prefEditor.putInt(key_DBDefaultIni, 1);
			prefEditor.commit();
			return 0;//ini db
		}else{		
			return 1;//then don't ini
		}
		
	}*/



/*	 Checks if external storage is available for read and write 
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	 Checks if external storage is available to at least read 
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}*/

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case (R.id.bDefault):
			confirmMode = 1;
			alertMessage(1);
			break;
		case (R.id.bSaveCurrent):
			confirmMode = 0;
			alertMessage(0);
			break;
		case (R.id.bImport):
			//confirmMode = 1;
			//alertMessage(1);		
			/*Intent i1 = new Intent("com.android.dnh.moneykeeper.EXISTDB");
			startActivityForResult(i1, 1);*/
			confirmMode = 2;
			alertMessage(2);
			//MyDatabase.importDB(string_currentDB);
			break;
		case (R.id.bExport):
			/*confirmMode = 3;
			alertMessage(3);*/
			Intent i3 = new Intent("com.android.dnh.moneykeeper.EXPORTDB");
			startActivityForResult(i3, 3);
			//startActivity(i3);
			break;
		case (R.id.bDeleteDB):
			/*Intent i2 = new Intent("com.android.dnh.moneykeeper.EXISTDB");
			startActivityForResult(i2, 1);*/
			confirmMode = 3;
			alertMessage(3);
			break;
		}

	}

	public void alertMessage(int a) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					// Toast.makeText(ImportExportDB.this, "Yes Clicked",
					// Toast.LENGTH_LONG).show();
					if (confirmMode == 0) {
						MyDatabase.exportDB(string_currentDB);
						//exportDB("test");
						finish();
					} else if (confirmMode == 1){
						//importDB("Defaulted");
						//importDB("test");
						MyMoney entry = new MyMoney(ImportExportDB.this);
						entry.open();
						entry.emptyDatabase();
						entry.close();
						MyCategoriesList cate = new MyCategoriesList(ImportExportDB.this);
						cate.open();
						cate.emptyDatabase();
						cate.close();
						currentDBedit("None");
						currentDB();
						//finish();
					}else if (confirmMode ==2){
						Intent i2 = new Intent("com.android.dnh.moneykeeper.EXISTDB");
						startActivityForResult(i2, 2);						
						//MyDatabase.importDB(string_currentDB);
					}else if (confirmMode ==3){
						Intent i2 = new Intent("com.android.dnh.moneykeeper.EXISTDB");
						startActivityForResult(i2, 1);						
						//MyDatabase.importDB(string_currentDB);
					}
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					// do nothing
					// Toast.makeText(ImportExportDB.this, "No Clicked",
					// Toast.LENGTH_LONG).show();
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (a == 0) {
			builder.setMessage(
					"Are you sure? This will override current saved database: "+string_currentDB)
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		} else if(a==1){
			builder.setMessage(
					"Are you sure? delete all data currently in the database")
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		}else if (a==2){
			builder.setMessage(
					"Are you sure? Unsaved data will be erased")
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		}else if (a==3){
			builder.setMessage(
					"Are you sure ? This will delete the database permanently")
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		}
	}

	// Check for current chosen database
	private void currentDB() {
		//String string_currentDB;
		SharedPreferences currentDB = this.getSharedPreferences(key_currentDBPref, MODE_PRIVATE);
		string_currentDB = currentDB.getString(key_DBdirectory, "Default");
		tvDBDirectory.setText(string_currentDB);
		// SharedPreferences.Editor prefEditor = currentDB.edit();
		// getData();

	}
	private void currentDBedit(String myString) {
		//String string_currentDB;
		
		SharedPreferences currentDB = this.getSharedPreferences(key_currentDBPref, MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = currentDB.edit();
		prefEditor.putString(key_DBdirectory, myString);
		prefEditor.commit();

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {// this is for Delete

		     if(resultCode == RESULT_OK){      //Just use to refresh the screen after go back from activity
		    	 String result=data.getStringExtra("result");
		    	 currentDBedit(result);
		    	 currentDB();
		    	 MyDatabase.deleteDB(string_currentDB);
		    	 Toast.makeText(getBaseContext(),"Deleted database: "+ string_currentDB,Toast.LENGTH_LONG).show();
		    	 currentDBedit("none");
		    	 currentDB();	 
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		  if (requestCode == 2) {// this is for Import

			     if(resultCode == RESULT_OK){      //Just use to refresh the screen after go back from activity
			         String result=data.getStringExtra("result");
			    	 currentDBedit(result);
			    	 currentDB();
			    	 MyDatabase.importDB(string_currentDB);
			    	 Toast.makeText(getBaseContext(),"Imported database: "+ string_currentDB,Toast.LENGTH_LONG).show();
			     }
			     if (resultCode == RESULT_CANCELED) {    
			         //Write your code if there's no result
			     }
		  }
		  if (requestCode == 3) {// this is for Export

			     if(resultCode == RESULT_OK){      //Just use to refresh the screen after go back from activity
			         String result=data.getStringExtra("resultExport");
			    	 currentDBedit(result);
			    	 currentDB();
			    	 MyDatabase.exportDB(string_currentDB);
			    	 Toast.makeText(getBaseContext(),"Exported database: "+ string_currentDB,Toast.LENGTH_LONG).show();
			     }
			     if (resultCode == RESULT_CANCELED) {    
			         //Write your code if there's no result
			     }
		  }
		}//onActivityResult

}
