package com.android.dnh.moneykeeper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyMoney {// include the handler
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE = "money_entry";
	public static final String KEY_AMOUNT = "money_amount";
	public static final String KEY_CATEGORY = "money_category";
	public static final String KEY_NOTE = "money_note";
	public static final String KEY_CURRENCY = "money_currency";
			
	private static final String DATABASE_NAME = "MyMoneydb";
	private static final String DATABASE_TABLE = "moneyTable";
	private static final int DATABASE_VERSION = 1;
	
	private dbHelper ourHelper;
	private final Context ourContext; //create the passing context to db
	private SQLiteDatabase ourDatabase;
	
	int iRow,iDate,iAmount,iCategory,iNote,iCurrency;//index of column for the data
	int loop1 = 0,loop2=0;

	public MyMoney(Context c) {//constructor define context
		ourContext = c;
	}

	public MyMoney open() throws SQLException{//open db and return mymoney context
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
							KEY_DATE + " INTEGER NOT NULL, " +
							KEY_AMOUNT + " REAL NOT NULL, " +
							KEY_CATEGORY + " TEXT NOT NULL, " +
							KEY_NOTE +  " TEXT NOT NULL, " +
							KEY_CURRENCY + " TEXT NOT NULL);"
					);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			MyDatabase.exportDB("old");
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			onCreate(db);
			MyDatabase.importDB("old");
		}

	}
	public long createEntry(int date, float amount, String category, String note, String currency) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATE, date);// key same as the table a column name
		cv.put(KEY_AMOUNT,amount);//
		cv.put(KEY_CATEGORY,category);
		cv.put(KEY_NOTE, note);
		cv.put(KEY_CURRENCY, currency);
		
		return ourDatabase.insert(DATABASE_TABLE, null, cv);// insert all the puts

	}
	public void updateEntry(long lRow, int date, float amount, String category, String note, String currency) throws SQLException{
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_DATE, date);// key same as the table a column name
		cvUpdate.put(KEY_AMOUNT,amount);//
		cvUpdate.put(KEY_CATEGORY,category);
		cvUpdate.put(KEY_NOTE, note);
		cvUpdate.put(KEY_CURRENCY, currency);
		ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID+"="+lRow, null);
		
	}

	public void deleteEntry(long lRow1) throws SQLException{
		// TODO Auto-generated method stub
		ourDatabase.delete(DATABASE_TABLE,KEY_ROWID+"="+lRow1, null	);
	}
	
	public void emptyDatabase(){
		ourDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
		ourDatabase.execSQL(
				"CREATE TABLE " + DATABASE_TABLE + " (" +
				KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + //1ST COLUMN
				KEY_DATE + " INTEGER NOT NULL, " +
				KEY_AMOUNT + " REAL NOT NULL, " +
				KEY_CATEGORY + " TEXT NOT NULL, " +
				KEY_NOTE +  " TEXT NOT NULL, " +
				KEY_CURRENCY + " TEXT NOT NULL);"
		);
	}
	
	public String[] getData(int type) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{ KEY_ROWID,KEY_DATE,KEY_AMOUNT,KEY_CATEGORY,KEY_NOTE,KEY_CURRENCY};//HAVE All column of the database
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE + " DESC," + KEY_ROWID + " DESC");//order by highst date first then by id
		//String result;
		setSQLid(c);
		String[] myData = new String[c.getCount()];
		//Float[] myDataFloat = new Float[c.getCount()];
		
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			//result = c.getString(iRow) + " " + c.getString(iDate) + " " + c.getString(iAmount) + " " + c.getString(iCategory) + " " + c.getString(iNote) + " " + c.getString(iCurrency) +"\n";//Testing only 3 val
			
			switch (type){
			case 1:
				myData[i] = c.getString(iRow);
				break;
			case 2:
				myData[i] = c.getString(iDate);
				break;
			case 3:
				//myDataFloat[i] = c.getFloat(iRow);
				myData[i] = c.getString(iAmount);
				
				break;
			case 4:
				myData[i] = c.getString(iCategory);
				break;
			case 5:
				myData[i] = c.getString(iNote);
				break;
			case 6:
				myData[i] = c.getString(iCurrency);
				break;
			default:
				myData[i] = c.getString(iRow) + " " + c.getString(iDate) + " " + c.getString(iAmount) + " " + c.getString(iCategory) + " " + c.getString(iNote) + " " + c.getString(iCurrency) +"\n";//Testing only 3 val
				break;
			}
			c.moveToNext();
		}
		

		return myData;
	}
	
	public String[] getData(int type, int dateBegin,int dateEnd,String category){//0 and "0" is default for no sorting

		String[] columns = new String[]{KEY_ROWID,KEY_DATE,KEY_AMOUNT,KEY_CATEGORY,KEY_NOTE,KEY_CURRENCY};//HAVE All column of the database
				
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_DATE + " DESC," + KEY_ROWID + " DESC");
		if (dateBegin!= 0 || dateEnd!= 0 || !category.equals("0")){
			if(dateBegin== 0 && dateEnd== 0 && !category.equals("0")){//only search category
				c = ourDatabase.query(DATABASE_TABLE, columns, KEY_CATEGORY +" = "+"'"+category+"'",null, null, null, KEY_DATE + " DESC," + KEY_ROWID + " DESC");
			}else if(dateBegin!= 0 && dateEnd!= 0 && category.equals("0")){//no search category only search date
				c = ourDatabase.query(DATABASE_TABLE, columns, KEY_DATE + " BETWEEN " +dateBegin+" AND "+dateEnd,null, null, null, KEY_DATE + " DESC," + KEY_ROWID + " DESC");		
			}else{//search for all
				c = ourDatabase.query(DATABASE_TABLE, columns, KEY_DATE + " BETWEEN " +dateBegin+" AND "+dateEnd +" AND "+ KEY_CATEGORY +" = "+"'"+category+"'",null, null, null, KEY_DATE + " DESC," + KEY_ROWID + " DESC");
			}
		}
		
		
		setSQLid(c);
		
		String[] myData = new String[c.getCount()];
		//Float[] myDataFloat = new Float[c.getCount()];
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			switch (type){
			case 1:
				myData[i] = c.getString(iRow);
				break;
			case 2:
				myData[i] = c.getString(iDate);
				break;
			case 3:
				//myDataFloat[i] = c.getFloat(iRow);
				myData[i] = c.getString(iAmount);
				
				break;
			case 4:
				myData[i] = c.getString(iCategory);
				break;
			case 5:
				myData[i] = c.getString(iNote);
				break;
			case 6:
				myData[i] = c.getString(iCurrency);
				break;
			default:
				myData[i] = c.getString(iRow) + " " + c.getString(iDate) + " " + c.getString(iAmount) + " " + c.getString(iCategory) + " " + c.getString(iNote) + " " + c.getString(iCurrency) +"\n";//Testing only 3 val
				break;
			}
			c.moveToNext();
		}
		

		return myData;
		
	}
	
	public String getData(int type, int rowid) {
		// TODO Auto-generated method stub
		String[] columns = new String[]{ KEY_ROWID,KEY_DATE,KEY_AMOUNT,KEY_CATEGORY,KEY_NOTE,KEY_CURRENCY};//HAVE All column of the database
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" +rowid, null, null, null, KEY_DATE + " DESC," + KEY_ROWID + " DESC");//order by highst date first then by id
		//String result;
		setSQLid(c);
		String myData="";
		//Float[] myDataFloat = new Float[c.getCount()];
		
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			//result = c.getString(iRow) + " " + c.getString(iDate) + " " + c.getString(iAmount) + " " + c.getString(iCategory) + " " + c.getString(iNote) + " " + c.getString(iCurrency) +"\n";//Testing only 3 val
			
			switch (type){
			case 1:
				myData = c.getString(iRow);
				break;
			case 2:
				myData = c.getString(iDate);
				break;
			case 3:
				//myDataFloat[i] = c.getFloat(iRow);
				myData = c.getString(iAmount);
				
				break;
			case 4:
				myData = c.getString(iCategory);
				break;
			case 5:
				myData = c.getString(iNote);
				break;
			case 6:
				myData = c.getString(iCurrency);
				break;
			default:
				myData = c.getString(iRow) + " " + c.getString(iDate) + " " + c.getString(iAmount) + " " + c.getString(iCategory) + " " + c.getString(iNote) + " " + c.getString(iCurrency) +"\n";//Testing only 3 val
				break;
			}
			c.moveToNext();
		}
		

		return myData;
	}
	
	private void setSQLid(Cursor c){
		if (loop1 == 0) {
			iRow = c.getColumnIndex(KEY_ROWID);// get the column of the data
			iDate = c.getColumnIndex(KEY_DATE);
			iAmount = c.getColumnIndex(KEY_AMOUNT);
			iCategory = c.getColumnIndex(KEY_CATEGORY);
			iNote = c.getColumnIndex(KEY_NOTE);
			iCurrency = c.getColumnIndex(KEY_CURRENCY);
			loop1=1;//only execute this once
		}
	}

}
