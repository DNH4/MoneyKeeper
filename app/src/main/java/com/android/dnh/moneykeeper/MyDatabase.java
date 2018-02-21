package com.android.dnh.moneykeeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.os.Environment;
//import android.widget.Toast;

public class MyDatabase {
	
	
	public static void exportDB(String dbName) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//" + "com.android.dnh.moneykeeper"+ "//databases//" + "MyMoneydb";//where the db is stored inside the app
				String backupDBPath = "/MoneyKeeperDB/MainDB/"+ dbName;// where you name the new db file
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);
				FileInputStream fis = new FileInputStream(currentDB);
				FileOutputStream fos = new FileOutputStream(backupDB);
				FileChannel src = fis.getChannel();
				FileChannel dst = fos.getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				fis.close();
				dst.close();
				fos.close();
				currentDBPath = "//data//" + "com.android.dnh.moneykeeper"+ "//databases//" + "MyMoneyCategorydb";
				backupDBPath = "/MoneyKeeperDB/CategoryDB/"+ dbName+"Category";
				currentDB = new File(data, currentDBPath);
				backupDB = new File(sd, backupDBPath);
				fis = new FileInputStream(currentDB);
				fos = new FileOutputStream(backupDB);
				src = fis.getChannel();
				dst = fos.getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				fis.close();
				dst.close();
				fos.close();
				//Toast.makeText(getBaseContext(),"Export to: " + backupDB.toString(), Toast.LENGTH_LONG)	.show();
			}
		} catch (Exception e) {

			//Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)					.show();

		}

	}

	public static void importDB(String dbName) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//" + "com.android.dnh.moneykeeper"+ "//databases//" + "MyMoneydb";
				String backupDBPath = "/MoneyKeeperDB/MainDB/"+ dbName;
				File backupDB = new File(data, currentDBPath);
				File currentDB = new File(sd, backupDBPath);
				FileInputStream fis = new FileInputStream(currentDB);
				FileOutputStream fos = new FileOutputStream(backupDB);
				FileChannel src = fis.getChannel();
				FileChannel dst = fos.getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				fis.close();
				dst.close();
				fos.close();
				currentDBPath = "//data//" + "com.android.dnh.moneykeeper"+ "//databases//" + "MyMoneyCategorydb";
				backupDBPath = "/MoneyKeeperDB/CategoryDB/"+ dbName+"Category";
				backupDB = new File(data, currentDBPath);
				currentDB = new File(sd, backupDBPath);
				fis = new FileInputStream(currentDB);
				fos = new FileOutputStream(backupDB);
				src = fis.getChannel();
				dst = fos.getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				fis.close();
				dst.close();
				fos.close();
				//Toast.makeText(getBaseContext(),"Import to: " + backupDB.toString(), Toast.LENGTH_LONG).show();

			}
		} catch (Exception e) {

			// Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			//Toast.makeText(getBaseContext(),"There is no saved database in this device",Toast.LENGTH_LONG).show();

		}
	}
	
	public static void deleteDB(String dbName) {
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		String backupDBPath = "/MoneyKeeperDB/MainDB/"+ dbName;
		File currentDB = new File(sd, backupDBPath);
		boolean deleted = currentDB.delete();
		
		backupDBPath = "/MoneyKeeperDB/CategoryDB/"+ dbName+"Category";
		currentDB = new File(sd, backupDBPath);
		deleted = currentDB.delete();
	}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
	

}
