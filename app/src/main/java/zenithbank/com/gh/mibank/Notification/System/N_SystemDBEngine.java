package zenithbank.com.gh.mibank.Notification.System;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Main.Classes.HistoryClass;
import zenithbank.com.gh.mibank.Notification.Classes.N_HistoryClass;
import zenithbank.com.gh.mibank.Notification.Classes.N_NotificationMessage;

/**
 * Created by Robby on 3/11/2016.
 */
public class N_SystemDBEngine extends Activity
{
    SQLiteDatabase MyDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void Init(Context context)
    {
        try
        {
            MyDb = context.openOrCreateDatabase("NOTIFICATION_DB", MODE_PRIVATE, null);
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS ACCOUNTS (ACCTNO varchar not null);");
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS NOTIFICATION_HISTORY_TABLE (" +
                    "trsxDate VARCHAR NOT NULL," +
                    "trsxAcctno VARCHAR NOT NULL," +
                    "trsxAmount VARCHAR NOT NULL," +
                    "trsxBalance VARCHAR NOT NULL," +
                    "trsxDecsription VARCHAR NOT NULL," +
                    "trsxCurrency VARCHAR NOT NULL," +
                    "trsxType VARCHAR NOT NULL," +
                    "trsxBranch VARCHAR NOT NULL);");
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }

    public void DeleteNotificationTable()
    {
        try
        {
            MyDb.delete("NOTIFICATION_HISTORY_TABLE", null, null);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void updateNotificationTable(N_HistoryClass.RootObject data)
    {
        int count = 0;
        try
        {
            MyDb.execSQL("insert into NOTIFICATION_HISTORY_TABLE (trsxDate,trsxAcctno,trsxAmount," +
                    "trsxBalance,trsxDecsription,trsxCurrency,trsxType,trsxBranch) values ('" +
                    data.trsxDate + "','" +
                    data.trsxAcctno + "','" +
                    data.trsxAmount + "','" +
                    data.trsxBalance + "','" +
                    data.trsxDecsription + "','" +
                    data.trsxCurrency + "','" +
                    data.trsxType + "','" +
                    data.trsxBranch + "');");
                /*ContentValues values = new ContentValues();
                values.put("trsxDate", data.trsxDate);
                values.put("trsxAcctno",data.trsxAcctno);
                values.put("trsxAmount", data.trsxAmount);
                values.put("trsxBalance", data.trsxBalance);
                values.put("trsxDecsription", data.trsxDecsription);
                values.put("trsxCurrency", data.trsxCurrency);
                values.put("trsxBranch", data.trsxBranch);
                MyDb.insert("NOTIFICATION_HISTORY_TABLE", null, values);*/

        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void updateNotificationTableNew(N_NotificationMessage.RootObject data)
    {
        int count = 0;
        try
        {
            MyDb.execSQL("insert into NOTIFICATION_HISTORY_TABLE (trsxDate,trsxAcctno,trsxAmount," +
                    "trsxBalance,trsxDecsription,trsxCurrency,trsxType,trsxBranch) values ('" +
                    data.messageBody.date + "','" +
                    data.messageBody.acctNo + "','" +
                    data.messageBody.amount + "','" +
                    data.messageBody.balance + "','" +
                    data.messageBody.description + "','" +
                    data.messageBody.currency + "','" +
                    data.messageBody.tranxType + "','" +
                    data.messageBody.branchName + "');");
                /*ContentValues values = new ContentValues();
                values.put("trsxDate", data.trsxDate);
                values.put("trsxAcctno",data.trsxAcctno);
                values.put("trsxAmount", data.trsxAmount);
                values.put("trsxBalance", data.trsxBalance);
                values.put("trsxDecsription", data.trsxDecsription);
                values.put("trsxCurrency", data.trsxCurrency);
                values.put("trsxBranch", data.trsxBranch);
                MyDb.insert("NOTIFICATION_HISTORY_TABLE", null, values);*/

        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public N_HistoryClass.RootObject getNotificationData()
    {
        N_HistoryClass.RootObject data = new N_HistoryClass.RootObject();
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from NOTIFICATION_HISTORY_TABLE", null);
            cursor.moveToFirst();
            data.trsxAcctno = cursor.getString(2).trim();
            data.trsxAmount = cursor.getString(3).trim();
            data.trsxBalance = cursor.getString(4).trim();
            data.trsxCurrency = cursor.getString(6).trim();
            data.trsxType = cursor.getString(7).trim();
            data.trsxBranch = cursor.getString(8).trim();
            data.trsxDate = cursor.getString(1).trim();
            data.trsxDecsription = cursor.getString(5).trim();
            cursor.close();
        } catch (Exception ex)
        {
            HistoryClass.RootObject _data = new HistoryClass.RootObject();
            _data.error = ex.getMessage();
        }

        return data;
    }

    public ArrayList<N_HistoryClass.RootObject> getAllNotificationData()
    {

        ArrayList<N_HistoryClass.RootObject> _data = new ArrayList<>();
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from NOTIFICATION_HISTORY_TABLE", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                N_HistoryClass.RootObject data = new N_HistoryClass.RootObject();
                data.trsxAcctno = cursor.getString(1).trim();
                data.trsxAmount = cursor.getString(2).trim();
                data.trsxBalance = cursor.getString(3).trim();
                data.trsxCurrency = cursor.getString(5).trim();
                data.trsxBranch = cursor.getString(7).trim();
                data.trsxDate = cursor.getString(0).trim();
                data.trsxDecsription = cursor.getString(4).trim();
                data.trsxType = cursor.getString(6).trim();
                _data.add(data);
                cursor.moveToNext();

            }

            cursor.close();
        } catch (Exception ex)
        {
            HistoryClass.RootObject _data1 = new HistoryClass.RootObject();
            _data1.error = ex.getMessage();
        }

        return _data;
    }


}
