package com.android.dnh.moneykeeper;

public class myDateFormat {
	
	public static String parseDate(int date){
		int day,month,year;
		day = parseDate2(0,date);
		month = parseDate2(1,date);
		year = parseDate2(2,date);
		
		return (monthConvert(month)+" "+day+", "+year);
	}
	public static int parseDate2(int option, int date){ //0 for day,1 for month,2 for year
		int myDate=0;
		switch(option){
		case 0:
			myDate = date%100;//day
	
			break;
		case 1:
			myDate = (date/100)%100;//month

			break;
		case 2:
			myDate = (date/100)/100;//year
			break;
		}
		return myDate;
	}

	public static String dayofweekConvert(int day){
		String myDay = "";
		switch(day){
		case 1:
			myDay= "Sun";
			break;
		case 2:
			myDay= "Mon";
			break;
		case 3:
			myDay= "Tue";
			break;
		case 4:
			myDay= "Wed";
			break;
		case 5:
			myDay= "Thu";
			break;
		case 6:
			myDay= "Fri";
			break;
		case 7:
			myDay= "Sat";
			break;
		}
		
		return myDay;
	}
	public static String monthConvert(int month){//convert month # 1- 12 to String
		String myMonth="Err";
		switch(month){
		case 1:
			myMonth= "Jan";
			break;
		case 2:
			myMonth= "Feb";
			break;
		case 3:
			myMonth= "Mar";
			break;
		case 4:
			myMonth= "Apr";
			break;
		case 5:
			myMonth= "May";
			break;
		case 6:
			myMonth= "Jun";
			break;
		case 7:
			myMonth= "Jul";
			break;
		case 8:
			myMonth= "Aug";
			break;
		case 9:
			myMonth= "Sep";
			break;
		case 10:
			myMonth= "Oct";
			break;
		case 11:
			myMonth= "Nov";
			break;
		case 12:
			myMonth= "Dec";
			break;
		}
		return myMonth;
	}
	
	public static int dayofweekChange(int sign,int dayofweek){//0 for add and 1 for minus
		int myNewDay = dayofweek;
		switch(sign){
		case 0:
			myNewDay = myNewDay+1;
			if(myNewDay>7){
				myNewDay = 1;
			}
			break;
		case 1:
			myNewDay = myNewDay-1;
			if(myNewDay<1){
				myNewDay = 7;
			}
			break;
		}
		return myNewDay;
	}

}
