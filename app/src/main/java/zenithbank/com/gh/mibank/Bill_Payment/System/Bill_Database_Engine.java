package zenithbank.com.gh.mibank.Bill_Payment.System;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import zenithbank.com.gh.mibank.Bill_Payment.Classes.BillPaymentClass;
import zenithbank.com.gh.mibank.Bill_Payment.Classes.BillsClass;
import zenithbank.com.gh.mibank.Bill_Payment.Classes.ExpressREsponseClass;
import zenithbank.com.gh.mibank.Bill_Payment.Classes.PaymentHistoryClass;

/**
 * Created by Robby on 5/4/2016.
 */
public class Bill_Database_Engine extends Activity
{
    SQLiteDatabase MyDb;
    String BILL_SETUP_TABLE = "BILL_SETUP_TABLE";
    String BILL_PAYMENT_TABLE = "BILL_PAYMENT_TABLE";
    String PAYMENT_HISTORY_TABLE = "PAYMENT_HISTORY_TABLE";
    String PAYMENT_RESPONSE_TABLE = "PAYMENT_RESPONSE_TABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void Init(Context context)
    {
        try
        {
            MyDb = context.openOrCreateDatabase("BILLS_DB", MODE_PRIVATE, null);
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + BILL_SETUP_TABLE + " (" +
                    "DATE VARCHAR NOT NULL," +
                    "BILL_NUMBER VARCHAR NOT NULL," +
                    "BILL_NAME VARCHAR NOT NULL," +
                    "BILL_TYPE VARCHAR NOT NULL," +
                    "BILL_AMOUNT VARCHAR NOT NULL," +
                    "BILL_STATUS INTEGER NOT NULL);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + PAYMENT_HISTORY_TABLE + " (" +
                    "DATE VARCHAR NOT NULL," +
                    "BILL_NUMBER VARCHAR NOT NULL," +
                    "BILL_TYPE VARCHAR NOT NULL," +
                    "BILL_NAME VARCHAR NOT NULL," +
                    "AMOUNT VARCHAR NOT NULL," +
                    "PAYMENT_STATUS VARCHAR NOT NULL," +
                    "REFERENCE VARCHAR NOT NULL);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + PAYMENT_RESPONSE_TABLE + " (" +
                    "status integer," +
                    "message varchar," +
                    "referenceNumber varchar," +
                    "receiptNumber varchar," +
                    "transactionId varchar," +
                    "timestamp varchar," +
                    "balance varchar," +
                    "currency varchar);");

        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void savePaymentHistory(BillPaymentClass data)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("DATE", data.PaymentDate);
            values.put("BILL_NUMBER", data.billNumber);
            values.put("BILL_TYPE", data.billType);
            values.put("BILL_NAME", data.billName);
            values.put("AMOUNT", data.billAmount);
            values.put("PAYMENT_STATUS", data.PaymentStatus);
            values.put("REFERENCE", data.reference);
            MyDb.insert("PAYMENT_HISTORY_TABLE", null, values);
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }

    public void AddBillPayment(BillsClass data)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());

            MyDb.execSQL("insert into " + BILL_SETUP_TABLE + " (DATE,BILL_NUMBER,BILL_TYPE,BILL_NAME," +
                    "BILL_AMOUNT,BILL_STATUS) VALUES ('" + currentDateandTime + "','" +
                    data.billNumber + "','" +
                    data.billType + "','" +
                    data.billName + "','" +
                    data.billAmount + "',0);");
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public ArrayList<PaymentHistoryClass> getPaymentHistory()
    {
        ArrayList<PaymentHistoryClass> resp = new ArrayList<>();
        try
        {
            Cursor cursor = MyDb.rawQuery("Select * from PAYMENT_HISTORY_TABLE", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                PaymentHistoryClass pay = new PaymentHistoryClass();
                pay.DATE = cursor.getString(0).trim();
                pay.BILL_NUMBER = cursor.getString(1).trim();
                pay.BILL_TYPE = cursor.getString(2).trim();
                pay.AMOUNT = cursor.getString(3).trim();
                pay.PAYMENT_STATUS = cursor.getString(4).trim();
                resp.add(pay);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return resp;
    }

    public ArrayList<BillsClass> getAllBillSetups()
    {
        ArrayList<BillsClass> data = new ArrayList<>();
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from BILL_SETUP_TABLE", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                BillsClass _data = new BillsClass();
                _data.billType = cursor.getString(3).trim();
                _data.billAmount = cursor.getString(4).trim();
                _data.billName = cursor.getString(2).trim();
                _data.billNumber = cursor.getString(1).trim();
                _data.billStatus = Boolean.parseBoolean(cursor.getString(4).trim());
                _data.Date = cursor.getString(0).trim();
                data.add(_data);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return data;
    }

    public boolean ActivateBill(String BillID)
    {
        boolean done = false;
        try
        {
            MyDb.execSQL("Update BILL_SETUP_TABLE set BILL_STATUS = 1 where " +
                    "BILL_NUMBER = '" + BillID + "'");
            done = true;
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return done;
    }

    public boolean DeactivateBill(String BillID)
    {
        boolean done = false;
        try
        {
            MyDb.execSQL("Update BILL_SETUP_TABLE set BILL_STATUS = 0 where " +
                    "BILL_NUMBER = '" + BillID + "'");
            done = true;
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return done;
    }

    public boolean DeleteBillSetup(String BillID)
    {
        boolean done = false;
        try
        {
            MyDb.execSQL("Delete from BILL_SETUP_TABLE where " +
                    "BILL_NUMBER = '" + BillID + "'");
            done = true;
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return done;
    }

    public String getCurrentDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd_HHmmss");
        return sdf.format(new Date());
    }

    public void saveExpressResponse(ExpressREsponseClass.RootObject response)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("status", response.status);
            values.put("message", response.message);
            values.put("referenceNumber", response.receiptNumber);
            values.put("transactionId", response.transactionId);
            values.put("receiptNumber", response.receiptNumber);
            values.put("timestamp", response.timestamp);
            values.put("balance", response.balance);
            values.put("currency", response.currency);
            MyDb.insert("PAYMENT_RESPONSE_TABLE", null, values);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }
}
