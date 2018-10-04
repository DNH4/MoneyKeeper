package com.android.dnh.moneykeeper.data;

import android.provider.BaseColumns;

public final class MoneyContract
{
    private MoneyContract(){} // no instantiate of contract class

    /**
     * Categories
     */
    public static final String CATEGORY_UNCATEGORIZED = "Uncategorized";
    public static final String CATEGORY_FOOD = "Food";
    public static final String CATEGORY_CLOTHING = "Clothing";
    public static final String CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_RENT = "Rent";
    public static final String CATEGORY_SALARY = "Salary";
    public static final String CATEGORY_GROCERY = "Grocery";
    public static final String CATEGORY_GIFT = "Gift";

    public static final class MoneyEntry implements BaseColumns{

        public static final String TABLE_NAME_TRANSACTION = "transactions";
        public static final String TABLE_NAME_CATEGORY = "category";

        /**
         *  Column name for transaction table
         */
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_CURRENCY = "currency";

        /**
         *  Column name for COLUMN_CATEGORY table
         */
        public static final String COLUMN_CATEGORY_NAME = "category_name";

    }

}