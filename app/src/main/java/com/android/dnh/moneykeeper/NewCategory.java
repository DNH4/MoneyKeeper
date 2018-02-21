package com.android.dnh.moneykeeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewCategory extends Activity implements OnClickListener {
	EditText newCategory;
	TextView ifExist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newcategory);
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		newCategory = (EditText) findViewById(R.id.etNewCateg);
		ifExist = (TextView) findViewById(R.id.tvExist);
		Button btSave = (Button) findViewById(R.id.BtSave);
		Button btCancel = (Button) findViewById(R.id.BtCancel);

		btSave.setOnClickListener(this);
		btCancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.BtSave:
			String data = newCategory.getText().toString();
			if (data.equals("")) {
				ifExist.setText("Invalid entry, cannot be blank");
				ifExist.setTextColor(Color.RED);
				ifExist.setVisibility(View.VISIBLE);
			} else {
				MyCategoriesList mcl = new MyCategoriesList(this);
				mcl.open();
				if(mcl.notExisted(data)){
					mcl.saveData(data);
					mcl.close();
					Intent returnIntent1 = new Intent();			
					setResult(RESULT_OK,returnIntent1);     
					finish();
				}else{
					ifExist.setText("Category name already exist");
					ifExist.setTextColor(Color.RED);
					ifExist.setVisibility(View.VISIBLE);
					mcl.close();
				}

			}
			break;
		case R.id.BtCancel:
			Intent returnIntent1 = new Intent();			
			setResult(RESULT_CANCELED,returnIntent1);     
			finish();
			break;
		}

	}

}
