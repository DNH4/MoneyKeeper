package com.android.dnh.moneykeeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CategoryEdit extends Activity implements OnClickListener{
	Button bNameChange,bIconChange,bDelete,bSave;
	TextView tvName;
	EditText etNewName;
	MyCategoriesList mcl = new MyCategoriesList(this);
	String myCategory;
	int rowID;
	final String key = "categoryID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.category_edit);

	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		rowID = Integer.parseInt(getIntent().getExtras().getString(key));
		initialize();
	}
	
	private void initialize(){
		bNameChange = (Button) findViewById(R.id.bChangeName);
		bIconChange = (Button) findViewById(R.id.bChangeIcon);
		bDelete = (Button) findViewById(R.id.bDelete);
		bSave = (Button) findViewById(R.id.bSave);
		tvName = (TextView) findViewById(R.id.tvCateName);
		etNewName = (EditText) findViewById(R.id.etNewName);
		
		mcl.open();
		myCategory = mcl.loadData(1,rowID); //TODO Need to move some where
		mcl.close();
		
		tvName.setText(myCategory);//!@#$
		etNewName.setText(myCategory);
		bNameChange.setOnClickListener(this);
		bIconChange.setOnClickListener(this);
		bSave.setOnClickListener(this);
		bDelete.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.bChangeName:
			etNewName.setVisibility(View.VISIBLE);
			Dialog d = new Dialog(this);
			d.setTitle("WARNING! ");
			TextView tv = new TextView(this);
			tv.setText("This should only be used if no transation with the old category name are logged (as of current version)");
			d.setContentView(tv);
			d.show();
			break;
			
		case R.id.bChangeIcon:
			Dialog d1 = new Dialog(this);
			d1.setTitle("Not yet available :(");
			TextView tv1 = new TextView(this);
			tv1.setText("This will be implement in the future");
			d1.setContentView(tv1);
			d1.show();
			break;
			
		case R.id.bSave:
			mcl.open();
			String a = etNewName.getText().toString();
			if(a.equals("")){
				Dialog d11 = new Dialog(this);
				d11.setTitle("Please enter a New Name");
				d11.show();
			}else{
				MyCategoriesList mcl = new MyCategoriesList(this);
				mcl.open();
				if(mcl.notExisted(a)){
					mcl.updateData(rowID, a);
					mcl.close();
					Intent returnIntent = new Intent();			
					setResult(RESULT_OK,returnIntent);     
					finish();
				}else{
					Dialog d11 = new Dialog(this);
					d11.setTitle("Category already exist");
					d11.show();
					mcl.close();
				}
				
			}
			mcl.close();
			break;
			
		case R.id.bDelete:
			mcl.open();
			mcl.deleteData(rowID);
			mcl.close();
			Intent returnIntent1 = new Intent();			
			setResult(RESULT_OK,returnIntent1);     
			finish();
			break;
		}
	}

}
