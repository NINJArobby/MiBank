package zenithbank.com.gh.mibank.Main.System;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Main.Classes.AuthenticateResponseClass;
import zenithbank.com.gh.mibank.Main.Classes.HistoryClass;
import zenithbank.com.gh.mibank.Main.Classes.SystemConfigurationClass;

public class System_DBEngine extends Activity
{
    public String LOGIN_TABLE = "LOGIN_TABLE";

    SQLiteDatabase MyDb;

    public void Init(Context context)
    {
        try
        {
            MyDb = context.openOrCreateDatabase("IBank_DB", MODE_PRIVATE, null);

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + LOGIN_TABLE +
                    " (username varchar not null," +
                    "password varchar not null," +
                    "status integer not null);");
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS RIM_TABLE (rimNo varchar not null);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS DEVICE_ID_TABLE (ID varchar not null);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS TEMP_HISTORY_TABLE (" +
                    "trsxDate VARCHAR NOT NULL," +
                    "trsxAcctno VARCHAR NOT NULL," +
                    "trsxAmount VARCHAR NOT NULL," +
                    "trsxBalance VARCHAR NOT NULL," +
                    "trsxDecsription VARCHAR NOT NULL," +
                    "trsxCurrency VARCHAR NOT NULL," +
                    "trsxBranch VARCHAR NOT NULL);");

            MyDb.execSQL("create table if not exists USER_DATA" +
                    " (NAME VARCHAR NOT NULL,RIM VARCHAR NOT NULL);");

            MyDb.execSQL("create table if not exists FIRST_USE" +
                    " (IS_FIRST VARCHAR NOT NULL);");

            MyDb.execSQL("create table if not exists CONFIGURATIONS_TABLE" +
                    " (PROMPT_ENABLE INTEGER NOT NULL," +
                    "HOME_ALERT_ENABLE INTEGER NOT NULL," +
                    "ALERT_SOUND_ENABLE INTEGER NOT NULL," +
                    "INTERBANK_ENABLE INTEGER NOT NULL," +
                    "IBANK_EASY_LOGIN_ENABLE INTEGER NOT NULL," +
                    "STMT_DOWNLOAD_ENABLE INTEGER NOT NULL," +
                    "SYSTEM_PASS_ENABLE INTEGER NOT NULL," +
                    "SYSTEM_EASY_LOGIN INTEGER NOT NULL," +
                    "TOKEN_ENABLE INTEGER NOT NULL);");
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }

    public boolean saveAccessData(String username, String password)
    {
        boolean done = false;
        try
        {
            MyDb.execSQL("insert into " + LOGIN_TABLE + " (username,password,status) values ('" +
                    username + "','" +
                    password + "',1);");
            done = true;
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return done;
    }

    public boolean saveDevID(String devid)
    {
        boolean done = false;
        try
        {
            ContentValues values = new ContentValues();
            values.put("ID", devid);
            MyDb.insert("DEVICE_ID_TABLE", null, values);
            done = true;
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return done;
    }

    public boolean isFirstUse()
    {
        boolean done = true;
        try
        {
            Cursor cursor = MyDb.rawQuery("select IS_FIRST from FIRST_USE", null);
            cursor.moveToFirst();
            if (cursor.getString(0).trim().equals("No"))
            {
                done = false;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return done;
    }

    public boolean CheckPassword(String username, String password)
    {
        try
        {
            Cursor cursor = MyDb.rawQuery("select" +
                    " password from " + LOGIN_TABLE
                    + " where username= '" + username + "'", null);
            cursor.moveToFirst();
            if (cursor.getString(0).equals(password))
            {
                return true;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return false;
    }

    public String getRim()
    {
        String data = null;
        try
        {
            Cursor cursor = MyDb.rawQuery("select RIM from USER_DATA", null);
            cursor.moveToFirst();
            data = cursor.getString(0);
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return data;
    }

    public void DeleteTempTable()
    {
        try
        {
            MyDb.delete("TEMP_HISTORY_TABLE", null, null);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void updateTempTable(ArrayList<HistoryClass.RootObject> data)
    {
        try
        {
            for (HistoryClass.RootObject dd : data)
            {
                MyDb.execSQL("insert into TEMP_HISTORY_TABLE (trsxDate,trsxAcctno,trsxAmount," +
                        "trsxBalance,trsxDecsription,trsxCurrency,trsxBranch) values ('" +
                        dd.trsxDate + "','" +
                        dd.trsxAcctno + "','" +
                        dd.trsxAmount + "','" +
                        dd.trsxBalance + "','" +
                        dd.trsxDecsription + "','" +
                        dd.trsxCurrency + "','" +
                        dd.trsxBranch + "');");
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public HistoryClass.RootObject getHistoryData(String count)
    {
        HistoryClass.RootObject data = new HistoryClass.RootObject();
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from TEMP_HISTORY_TABLE where count = "
                    + count, null);
            cursor.moveToFirst();
            data.trsxAcctno = cursor.getString(2).trim();
            data.trsxAmount = cursor.getString(3).trim();
            data.trsxBalance = cursor.getString(4).trim();
            data.trsxCurrency = cursor.getString(6).trim();
            data.trsxBranch = cursor.getString(7).trim();
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

    public void SaveUserData(AuthenticateResponseClass.Data data)
    {
        try
        {
            MyDb.execSQL("insert into USER_DATA(NAME,RIM) values ('" +
                    data.name + "','" + data.rim + "');");
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void setNewUseFlag()
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("IS_FIRST", "No");
            MyDb.insert("FIRST_USE", null, values);
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }

    public AuthenticateResponseClass.Data getUserData()
    {
        AuthenticateResponseClass.Data data = new AuthenticateResponseClass.Data();
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from USER_DATA", null);
            cursor.moveToFirst();
            data.name = cursor.getString(0).trim();
            data.rim = cursor.getString(1).trim();
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
            data.error = ex.getMessage();
        }

        return data;
    }

    //region Settings

    public void InitConfiguration()
    {
        ContentValues values = new ContentValues();
        values.put("PROMPT_ENABLE", 0);
        values.put("HOME_ALERT_ENABLE", 0);
        values.put("ALERT_SOUND_ENABLE", 0);
        values.put("INTERBANK_ENABLE", 0);
        values.put("IBANK_EASY_LOGIN_ENABLE", 0);
        values.put("STMT_DOWNLOAD_ENABLE", 0);
        values.put("SYSTEM_PASS_ENABLE", 0);
        values.put("SYSTEM_EASY_LOGIN", 0);
        values.put("TOKEN_ENABLE", 0);
        MyDb.insert("CONFIGURATIONS_TABLE", null, values);
    }

    public SystemConfigurationClass getConfiguration()
    {
        SystemConfigurationClass data = new SystemConfigurationClass();
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from CONFIGURATIONS_TABLE", null);
            cursor.moveToFirst();
            data.PROMPT_ENABLE = Integer.parseInt(cursor.getString(0).trim());
            data.HOME_ALERT_ENABLE = Integer.parseInt(cursor.getString(1).trim());
            data.ALERT_SOUND_ENABLE = Integer.parseInt(cursor.getString(2).trim());
            data.INTERBANK_ENABLE = Integer.parseInt(cursor.getString(3).trim());
            data.IBANK_EASY_LOGIN_ENABLE = Integer.parseInt(cursor.getString(4).trim());
            data.STMT_DOWNLOAD_ENABLE = Integer.parseInt(cursor.getString(5).trim());
            data.SYSTEM_PASS_ENABLE = Integer.parseInt(cursor.getString(6).trim());
            data.SYSTEM_EASY_LOGIN = Integer.parseInt(cursor.getString(7).trim());
            data.TOKEN_ENABLE = Integer.parseInt(cursor.getString(8).trim());
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return data;
    }

    public void saveConfiguration(SystemConfigurationClass data)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("PROMPT_ENABLE", data.PROMPT_ENABLE);
            values.put("HOME_ALERT_ENABLE", data.HOME_ALERT_ENABLE);
            values.put("ALERT_SOUND_ENABLE", data.ALERT_SOUND_ENABLE);
            values.put("INTERBANK_ENABLE", data.INTERBANK_ENABLE);
            values.put("IBANK_EASY_LOGIN_ENABLE", data.IBANK_EASY_LOGIN_ENABLE);
            values.put("STMT_DOWNLOAD_ENABLE", data.STMT_DOWNLOAD_ENABLE);
            values.put("SYSTEM_PASS_ENABLE", data.SYSTEM_PASS_ENABLE);
            values.put("SYSTEM_EASY_LOGIN", data.SYSTEM_EASY_LOGIN);
            values.put("TOKEN_ENABLE", data.TOKEN_ENABLE);
            MyDb.update("CONFIGURATIONS_TABLE", values, null, null);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    //endregion
}





