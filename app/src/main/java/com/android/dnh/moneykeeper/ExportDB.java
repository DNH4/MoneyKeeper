package com.android.dnh.moneykeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ExportDB extends Activity implements OnClickListener {

	Button bExport,bExportExist;
	EditText etDBname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exportdb);
		initialize();

	}

	private void initialize() {

		// bExistDB = (Button)findViewById(R.id.bExistDB);
		etDBname = (EditText) findViewById(R.id.etDBname);
		bExport = (Button) findViewById(R.id.bExport);
		bExportExist = (Button) findViewById(R.id.bExportExist);
		// bExistDB.setOnClickListener(this);
		bExport.setOnClickListener(this);
		bExportExist.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bExport:
			if (etDBname.getText().toString().equals("")) {
				Dialog d = new Dialog(this);
				d.setTitle("Empty name !");
				TextView tv = new TextView(this);
				tv.setTextSize(15);
				tv.setPadding(10, 0, 0, 10);
				tv.setText("Your database wants a name :(");
				d.setContentView(tv);
				d.show();
			} else {
				alertMessage(0);
			}
			break;
			
		case R.id.bExportExist:
			Intent i = new Intent("com.android.dnh.moneykeeper.EXISTDB");//Reusing this just for export creating a known list of DB
			//startActivity(i);
			startActivityForResult(i, 1);
			break;
		}

	}
	
	
	public void alertMessage(int a) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					String result=etDBname.getText().toString();
			    	Intent returnIntent = new Intent();
			    	returnIntent.putExtra("resultExport",result);
			    	setResult(RESULT_OK, returnIntent);
			    	finish();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// do nothing
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (a == 0) {
			builder.setMessage(
					"Export to: "+etDBname.getText().toString()+" ?")
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		}
	}
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      //come back from choosing existing proj. export right away
		    	 String result=data.getStringExtra("result");
		    	 etDBname.setText(result);		    	 
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //do nothing
		     }
		  }
		}
	
}
