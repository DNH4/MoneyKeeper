package com.android.dnh.moneykeeper;

public class Total_Money {

	public static float mySum(String[] Amount) {// for balance
		float myAmount = 0;

		for (int i = 0; i < Amount.length; i++) {
			myAmount = myAmount + Float.parseFloat(Amount[i]);
		}
		return myAmount;
	}

	public static float mySumI(String[] Amount) {// for income only
		float myAmount = 0;

		for (int i = 0; i < Amount.length; i++) {
			if (Float.parseFloat(Amount[i]) > 0) {
				myAmount = myAmount + Float.parseFloat(Amount[i]);
			}
		}
		return myAmount;
	}

	public static float mySumE(String[] Amount) {// for expense only
		float myAmount = 0;

		for (int i = 0; i < Amount.length; i++) {
			if (Float.parseFloat(Amount[i]) < 0) {
				myAmount = myAmount + Float.parseFloat(Amount[i]);
			}
		}
		return myAmount;
	}

}
