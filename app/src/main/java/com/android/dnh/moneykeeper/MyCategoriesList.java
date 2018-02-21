package com.android.dnh.moneykeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyCategoriesList {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CATEGORYLIST = "money_categorylist";

			
	private static final String DATABASE_NAME = "MyMoneyCategorydb";
	private static final String DATABASE_TABLE = "categoryTable";
	private static final int DATABASE_VERSION = 1;
	
	private dbHelper ourHelper;
	private final Context ourContext; //create the passing context to db
	private SQLiteDatabase ourDatabase;
	
	int iRow,icategoryList;//index of column for the data
	int loop1 = 0;

	public MyCategoriesList(Context c) {//constructor define context
		ourContext = c;
	}

	public MyCategoriesList open() throws SQLException{//open db and return mymoney context
		ourHelper = new dbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();//create/open db for writing
		return this;
	}
	
	public void close(){	
		ourHelper.close();//close database
	}

	// our handler
	private static class dbHelper extends SQLiteOpenHelper{

//		public dbHelper(Context context, String name, CursorFactory factory, int version) {
			//super(context, name, factory, version);
			// TODO Auto-generated constructor stub
//	 	}
		public dbHelper(Context ourContext) {//constructor using only passing context
			// TODO Auto-generated constructor stub
			super(ourContext,DATABASE_NAME,null,DATABASE_VERSION);
		}
		
		

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(
							"CREATE TABLE " + DATABASE_TABLE + " (" +
							KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + //1ST COLUMN
							KEY_CATEGORYLIST + " TEXT NOT NULL);"
					);

			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			onCreate(db);
		}

	}
	
	public long saveData(String categoryList) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_CATEGORYLIST, categoryList);// key same as the table a column name
		return ourDatabase.insert(DATABASE_TABLE, null, cv);// insert all the puts
	}
	public void updateData(long lRow, String categoryList) throws SQLException{
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_CATEGORYLIST, categoryList);
		ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID+"="+lRow, null);
		
	}

	public void deleteData(long lRow1) throws SQLException{
		// TODO Auto-generated method stub
		ourDatabase.delete(DATABASE_TABLE,KEY_ROWID+"="+lRow1, null	);
	}
	
	public void emptyDatabase(){
		ourDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
		ourDatabase.execSQL(
				"CREATE TABLE " + DATABASE_TABLE + " (" +
				KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + //1ST COLUMN
				KEY_CATEGORYLIST + " TEXT NOT NULL);"
		);
		saveData("New Category...");
		saveData("Uncategorized");
		saveData("Food");
		saveData("Clothing");
		saveData("Entertainment");
		saveData("Rent");
		saveData("Salary");
		saveData("Grocery");
		saveData("Gift");
	}
	
	public String[] loadData(int a) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{ KEY_ROWID,KEY_CATEGORYLIST};//HAVE All column of the database
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		//String result;
		if (loop1 == 0) {
			iRow = c.getColumnIndex(KEY_ROWID);// get the column of the data
			icategoryList = c.getColumnIndex(KEY_CATEGORYLIST);
		}
		String[] myData = new String[c.getCount()];
		
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
				if(a==0){
					myData[i] = c.getString(iRow);
				}else{
					myData[i] = c.getString(icategoryList);
				}
			c.moveToNext();
		}
		return myData;
	}
	
	public String loadData(int a, int rowid) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{ KEY_ROWID,KEY_CATEGORYLIST};//HAVE All column of the database
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" +rowid, null, null, null, null);
		//String result;
		if (loop1 == 0) {
			iRow = c.getColumnIndex(KEY_ROWID);// get the column of the data
			icategoryList = c.getColumnIndex(KEY_CATEGORYLIST);
		}
		String myData = "";
		
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
				if(a==0){
					myData = c.getString(iRow);
				}else{
					myData = c.getString(icategoryList);
				}
			c.moveToNext();
		}
		return myData;
	}
	
	public boolean notExisted(String data){ // check if data is existed in array
		
		String[] existedData = loadData(1);
		for(int i=0;i<existedData.length;i++){
			if(existedData[i].equals(data)){
				return false;
			}
		}return true;
	}
	
	
}
