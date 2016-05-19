package zenithbank.com.gh.mibank.TopUps.T_System;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.TopUps.Clasees.T_HistoryClass;

/**
 * Created by Robby on 4/7/2016.
 */
public class T_SystemDatabase extends Activity
{
    SQLiteDatabase MyDb;
    String CONTACTS_LIST_TABLE = "CONTACTS_LIST_TABLE";
    String TRANSACTION_LOGS_TABLE = "TRANSACTION_LOGS";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void Init(Context context)
    {
        MyDb = context.openOrCreateDatabase("TOPUPS_DB", MODE_PRIVATE, null);
        MyDb.execSQL("create table if not exists " + CONTACTS_LIST_TABLE + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME VARCHAR NOT NULL," +
                "NUMBER VARCHAR NOT NULL," +
                "SETUP_DATE VARCHAR NOT NULL);");

        MyDb.execSQL("create table if not exists " + TRANSACTION_LOGS_TABLE + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME VARCHAR NOT NULL," +
                "NUMBER VARCHAR NOT NULL," +
                "AMOUNT VARCHAR NOT NULL," +
                "NETWORK VARCHAR NOT NULL," +
                "TRSX_DATE VARCHAR NOT NULL," +
                "TRSX_STATUS INTEGER);");
    }

    public void saveTransaction(T_HistoryClass.transaction data)
    {
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("NAME", data.name);
            contentValues.put("NUMBER", data.number);
            contentValues.put("AMOUNT", data.amount);
            contentValues.put("NETWORK", data.network);
            contentValues.put("TRSX_DATE", data.trsxDate);
            contentValues.put("TRSX_STATUS", data.status);
            MyDb.insert(TRANSACTION_LOGS_TABLE, null, contentValues);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public ArrayList<T_HistoryClass.transaction> getTopUps()
    {
        ArrayList<T_HistoryClass.transaction> data = new ArrayList<>();
        T_HistoryClass.transaction row;
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from " + TRANSACTION_LOGS_TABLE, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                row = new T_HistoryClass.transaction();
                row.name = cursor.getString(0).trim();
                row.number = cursor.getString(1).trim();
                row.amount = cursor.getString(2).trim();
                row.network = cursor.getString(3).trim();
                row.trsxDate = cursor.getString(4).trim();
                row.status = cursor.getString(5).trim();
                data.add(row);
                cursor.moveToNext();
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return data;
    }

}
