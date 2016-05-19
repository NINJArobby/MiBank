package zenithbank.com.gh.mibank.Ibank.System;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Ibank.Classes.Bill_Schedule_Class;
import zenithbank.com.gh.mibank.Ibank.Classes.CustomerInfo;
import zenithbank.com.gh.mibank.Ibank.Classes.SettingClass;
import zenithbank.com.gh.mibank.Ibank.Classes.TransactionClass;
import zenithbank.com.gh.mibank.Ibank.Classes.TransferBeneficiaryClass;

/**
 * Created by Robby on 3/31/2016.
 */
public class IbankDatabaseEngine extends Activity
{
    public String CUSTOMER_TABLE = "CUSTOMER";
    public String ACCOUNTS_TABLE = "ACCOUNTS";
    public String BILLS_TABLE = "BILLS";
    public String TRANSFERS_TABLE = "TRANSFERS_TABLE";
    public String TRANSFERS_PRODUCTS_TABLE = "TRANSFERS_PRODUCTS_TABLE";
    public String TRANSACTION_HISTORY_TABLE = "TRANSACTIONS_TABLE";
    public String SECURITY_TABLE = "ACCESS_TABLE";
    public String INSTALL_TABLE = "INSTALL_TABLE";
    public String COOKIE_TABLE = "COOKIES";
    public String BALANCES_TABLE = "ACCOUNT_BALANCE_TABLE";
    public String TOKEN_TABLE = "TOKEN_TABLE";
    public String AUTO_PAYMENT_TABLE = "AUTO_PAYMENTS";
    public String TEMPORAL_BILLS_PRODUCTS = "TEMPORAL_PRODUCTS";
    public String TRANSFER_BENEFICIARIES = "TRANSFER_BENEFICIARIES_TEMP";
    public String BILL_PAYMENT_SCHEDULES = "BILL_PAYMENT_SCHEDULES";
    public String EASY_LOGIN_TABLE = "EASY_LOGIN_TABLE";
    public String NOTIFICATION_LOG = "NotificationLogsDB";
    public String EMAIL_TABLE = "EMAIL_TABLE";
    public String SETTINGS_TABLE = "SETTINGS_TABLE";
    public String PHONE_TABLE = "PHONE_TABLE";
    public String CARD_ACCOUNTS_TABLE = "CARD_ACCOUNTS_TABLE";

    SQLiteDatabase MyDb;
    String[] AccountValues;

    public void Init(Context context)
    {
        try
        {
            //create necessary tables
            MyDb = context.openOrCreateDatabase("IBank_DB", MODE_PRIVATE, null);
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE +
                    " (rim_no integer not null," +
                    "rimName varchar not null," +
                    "rsmName varchar not null," +
                    "username varchar not null," +
                    "accesscode varchar not null);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + EMAIL_TABLE +
                    " ( EMAIL_ADDRESS varchar not null);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + PHONE_TABLE +
                    " ( PHONE_NO varchar not null);");


            //accounts table
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + ACCOUNTS_TABLE +
                    " (acctType varchar not null," +
                    "acctNo integer not null," +
                    "acctDesc varchar not null," +
                    "isoCurrency varchar(3) not null);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + CARD_ACCOUNTS_TABLE +
                    " (acctType varchar not null," +
                    "acctNo integer not null," +
                    "acctDesc varchar not null," +
                    "isoCurrency varchar(3) not null);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + BALANCES_TABLE +
                    " (acctType varchar not null," +
                    "acctNo integer not null," +
                    "acctDesc varchar not null," +
                    "isoCurrency varchar(3) not null," +
                    "curBal varchar not null," +
                    "acctAvail varchar not null," +
                    "holdBal varchar not null," +
                    "title1 varchar not null);");


            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + BILLS_TABLE +
                    " (BillInfo_id integer not null," +
                    "Bill_id integer not null," +
                    "productName varchar not null," +
                    "field1 varchar not null);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + TRANSFERS_TABLE +
                    " (id integer not null," +
                    "transferBeneficiary varchar not null," +
                    "beneID integer not null," +
                    "amount varchar not null," +
                    "tfrAcctNo integer not null," +
                    "firstScheduleDt varchar not null," +
                    "frid integer," +
                    "transferId integer);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + TRANSFERS_PRODUCTS_TABLE +
                    " (ID integer not null," +
                    "productCode varchar not null," +
                    "productName varchar not null," +
                    "field1 varchar," +
                    "field2 varchar," +
                    "field3 varchar," +
                    "field4 varchar," +
                    "field5 varchar)");

            //create table
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TRANSACTION_HISTORY_TABLE +
                    " (ID integer primary key autoincrement not null," +
                    "TRAN_DATE integer, " +
                    "ACCOUNT VARCHAR, " +
                    "AMOUNT  VARCHAR," +
                    "TYPE VARCHAR, " +
                    "DESCRIPTION VARCHAR, " +
                    "CURRENCY VARCHAR(3), " +
                    "BRANCH VARCHAR);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + SECURITY_TABLE +
                    "(ACCESSCODE VARCHAR,USERNAME VARCHAR);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + INSTALL_TABLE +
                    "(signedOn integer);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + COOKIE_TABLE +
                    "(cookie varchar not null);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + TOKEN_TABLE +
                    "(TOKEN varchar not null,TOKEN_SET INTEGER NOT NULL);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + AUTO_PAYMENT_TABLE +
                    "(billID varchar not null," +
                    "billType varchar not null," +
                    "repaymentDate varchar not null);");
            //MyDb.execSQL("insert into " + COOKIE_TABLE + " (cookie) values ('NON');");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + TEMPORAL_BILLS_PRODUCTS +
                    "(id varchar not null," +
                    "productName varchar not null," +
                    "field1 varchar not null," +
                    "field2 varchar," +
                    "field3 varchar," +
                    "field4 varchar," +
                    "field5 varchar," +
                    "field6 varchar," +
                    "field7 varchar," +
                    "field8 varchar," +
                    "field9 varchar);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + TRANSFER_BENEFICIARIES +
                    " (Beneficiary_id varchar not null," +
                    "productName varchar not null," +
                    "field1 varchar not null," +
                    "field2 varchar," +
                    "field3 varchar," +
                    "field4 varchar," +
                    "field5 varchar);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + BILL_PAYMENT_SCHEDULES +
                    " (Schedule_id varchar," +
                    "BillInfo_id integer not null," +
                    "Bill_id integer not null," +
                    "productName varchar not null," +
                    "amount varchar," +
                    "schedule_dt DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "status integer);");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + EASY_LOGIN_TABLE +
                    " (IsEasyLogin integer);");


            //create table
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS "
                    + NOTIFICATION_LOG +
                    " (ID integer primary key autoincrement not null," +
                    "TRAN_DATE VARCHAR, " +
                    "ACCOUNT VARCHAR, " +
                    "AMOUNT  VARCHAR," +
                    "TYPE VARCHAR, " +
                    "DESCRIPTION VARCHAR, " +
                    "CURRENCY VARCHAR(3), " +
                    "BRANCH VARCHAR );");

            MyDb.execSQL("CREATE TABLE IF NOT EXISTS " + SETTINGS_TABLE +
                    " (easyLogin varchar," +
                    "autoPayment varchar," +
                    "Locator varchar,notify varchar);");
            MyDb.execSQL("create table if not exists TEMP_ACCT_TABLE (ACCT_NO VARCHAR NOT NULL);");
            MyDb.execSQL("create table if not exists LoginTable (LoggedIn varchar not null);");


        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void updateCustomerTable(
            String rim_no, String rimName, String rsmName,
            String username, String accesscode)
    {
        try
        {
            MyDb.execSQL("insert into " + CUSTOMER_TABLE + " " +
                    "(rim_no,rimName,rsmName,username,accesscode)" +
                    " values (" +
                    rim_no + ",'" +
                    rimName + "','" +
                    rsmName + "','" +
                    username + "','" +
                    accesscode + "');");
        } catch (Exception ex)
        {
            ex.toString();
        }
    }

    public void updatePhoneTable(String phoneNumber)
    {
        try
        {
            MyDb.execSQL("Insert into " + PHONE_TABLE +
                    " (PHONE_NO) values ('" + phoneNumber + "')");

        } catch (Exception ex)
        {
            ex.toString();
        }
    }

    public void updateEmailTable(String email)
    {
        try
        {
            MyDb.execSQL("Insert into " + EMAIL_TABLE +
                    " (EMAIL_ADDRESS) values ('" + email + "')");

        } catch (Exception ex)
        {
            ex.toString();
        }
    }

    public void updateCardAccountsTable(String acctType, String acctNo, String acctDesc,
                                        String isoCurrency)
    {
        try
        {
            MyDb.execSQL("Insert into " + CARD_ACCOUNTS_TABLE + " " +
                    "(acctType,acctNo,acctDesc,isoCurrency) values ('" +
                    acctType + "'," +
                    acctNo + ",'" +
                    acctDesc + "','" +
                    isoCurrency + "');");
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void updateAccountsTable(String acctType, String acctNo, String acctDesc,
                                    String isoCurrency)
    {
        try
        {
            MyDb.execSQL("Insert into " + ACCOUNTS_TABLE + " " +
                    "(acctType,acctNo,acctDesc,isoCurrency) values ('" +
                    acctType + "'," +
                    acctNo + ",'" +
                    acctDesc + "','" +
                    isoCurrency + "');");
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public CustomerInfo.RootObject GetCustomerData()
    {
        CustomerInfo.RootObject data = new CustomerInfo.RootObject();
        try
        {

            data.profile = new CustomerInfo.Profile();
            data.profile.cardAccounts = new ArrayList<>();
            data.profile.emails = new CustomerInfo.Emails();
            data.profile.mobilenos = new CustomerInfo.Mobilenos();
            data.profile.rimInfo = new CustomerInfo.RimInfo();
            Cursor cursor = MyDb.rawQuery("select * from " + CUSTOMER_TABLE, null);
            cursor.moveToFirst();
            data.profile.rimInfo.rimNo = Integer.parseInt(cursor.getString(0));
            data.profile.rimInfo.rimName = cursor.getString(1);
            data.profile.rimInfo.rsmName = cursor.getString(2);
            data.profile.username = cursor.getString(3);
            data.profile.accesscode = Long.parseLong(cursor.getString(4));

            cursor.close();

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return data;
    }


    public void updateBillsTable(
            String billInfoId, String BillId, String productName,
            String field1)
    {
        try
        {

            MyDb.execSQL("Insert into " + BILLS_TABLE + "" +
                    " (BillInfo_id,Bill_id,productName,field1)" +
                    " Values (" +
                    billInfoId + "," +
                    BillId + ",'" +
                    productName + "','" +
                    field1 + "')");
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void updateBillSchedulesTable(String Schedule_id, String BillInfo_id, String Bill_id,
                                         String productName, String amount, String schedule_dt,
                                         String status)
    {
        try
        {
            MyDb.execSQL("Insert into " + BILL_PAYMENT_SCHEDULES + "" +
                    " (Schedule_id,BillInfo_id,Bill_id,productName,amount,schedule_dt,status)" +
                    " Values ('" +
                    Schedule_id + "'," +
                    BillInfo_id + "," +
                    Bill_id + ",'" +
                    productName + "','" +
                    amount + "," +
                    schedule_dt + "','" +
                    status + "')");
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void updateBalancesTable(
            String acctType, String acctNo, String acctDesc,
            String isoCurrency, String curBal,
            String acctAvail, String holdBal, String title1)
    {
        try
        {
            DeleteBalancesTable();
            MyDb.execSQL("Insert into " + BALANCES_TABLE + " (acctType,acctNo,acctDesc,isoCurrency,curBal,acctAvail" +
                    ",holdBal,title1) values ('" +
                    acctType + "'," +
                    acctNo + ",'" +
                    acctDesc + "','" +
                    isoCurrency + "','" +
                    curBal + "','" +
                    acctAvail + "','" +
                    holdBal + "','" +
                    title1 + "');");
        } catch (Exception ex)
        {
            ex.toString();
        }
    }

    public void updateTransfersTable(
            String id, String transferBeneficiary, String amount,
            String tfrAcctNo,
            String firstScheduleDt,
            String frid, String transferId)
    {
        try
        {
            MyDb.execSQL("insert into " + TRANSFERS_TABLE + "(id,transferBeneficiary,amount," +
                    "tfrAcctNo,firstScheduleDt" +
                    ",frid,transferId) values(" +
                    id + ",'" +
                    transferBeneficiary + "','" +
                    amount + "','" +
                    tfrAcctNo + "','" +
                    firstScheduleDt + "'," +
                    frid + "," + transferId + ");");
        } catch (Exception ex)
        {
            ex.toString();
        }
    }

    public void updateTransfersProductsTable(String productCode, String productName)
    {
        try
        {
            MyDb.execSQL("insert into " + TRANSFERS_PRODUCTS_TABLE + "(productCode,productName) values (" +
                    productCode + "','" +
                    productName + "');");
        } catch (Exception ex)
        {
            ex.toString();
        }
    }

    public void updateTransferBeneficiaryTable(
            String Beneficiary_id, String productName, String field1,
            String field2, String field3, String field4, String field5)
    {
        MyDb.delete(TRANSFER_BENEFICIARIES, null, null);
        try
        {
            MyDb.execSQL("insert into " + TRANSFER_BENEFICIARIES +
                    "(Beneficiary_id,productName,field1,field2,field3,field4,field5) values (" +
                    Beneficiary_id + "','" +
                    productName + "','" +
                    field1 + "','" +
                    field2 + "','" +
                    field3 + "','" +
                    field4 + "','" +
                    field5 + "');");
        } catch (Exception ex)
        {
            ex.toString();
        }
    }

    public void saveLoginCred(String accessCode, String userName)
    {
        try
        {
            MyDb.execSQL("insert into " + SECURITY_TABLE + " (ACCESSCODE,USERNAME) values ('" +
                    accessCode + "','" +
                    userName + "');");
        } catch (Exception ex)
        {
            ex.toString();
        }
    }

    public ArrayList<String> getCustomerProfile()
    {
        ArrayList<String> data = new ArrayList<>();
        try
        {
            Cursor cursor = MyDb.rawQuery("select rsmName from " + CUSTOMER_TABLE, null);
            cursor.moveToFirst();
            data.add(cursor.getString(0));

            cursor = MyDb.rawQuery("select EMAIL_ADDRESS from " + EMAIL_TABLE, null);
            cursor.moveToFirst();
            data.add(cursor.getString(0));

            cursor = MyDb.rawQuery("select PHONE_NO from " + PHONE_TABLE, null);
            cursor.moveToFirst();
            data.add(cursor.getString(0));

            cursor.close();
        } catch (Exception ex)
        {
            data.add("error: " + ex.getMessage());
        }

        return data;
    }

    public ArrayList<String> getSecurityValues()
    {
        ArrayList list = new ArrayList();

        try
        {
            Cursor cursor = MyDb.rawQuery("select * from " + SECURITY_TABLE, null);
            int xl = cursor.getCount();
            cursor.moveToFirst();
            list.add(cursor.getString(0));
            list.add(cursor.getString(1));

            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;
    }

    public ArrayList<String> FetchBalances()
    {

        ArrayList<String> list = new ArrayList<>();
        try
        {
            Cursor cursor = MyDb.rawQuery("select " +
                    "title1," +
                    "acctNo," +
                    "acctType," +
                    "isoCurrency," +
                    "curBal from " + BALANCES_TABLE, null);
            int xl = cursor.getCount();
            if (xl < 1)
            {
                throw new Exception("NoDataFound");
            }
            cursor.moveToFirst();

            String res = "";
            while (!cursor.isAfterLast())
            {
                for (int x = 0; x < cursor.getColumnCount(); x++)
                {
                    res = res + cursor.getString(x).trim() + ";";
                }
                list.add(res);
                cursor.moveToNext();
                res = "";
            }

            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
            list.add("No Accounts Found;null;null;null;null");
        }
        return list;
    }

    public ArrayList<String> getAccounts()
    {
        ArrayList list = new ArrayList();
        list.add("Tap to select Account");
        try
        {
            Cursor cursor = MyDb.rawQuery("select acctNo from " + ACCOUNTS_TABLE, null);
            int xl = cursor.getCount();
            int x = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                list.add(cursor.getString(0));
                cursor.moveToNext();
                x++;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    public ArrayList<String> getCardAccounts()
    {
        ArrayList list = new ArrayList();
        list.add("Tap to select Card");
        try
        {
            Cursor cursor = MyDb.rawQuery("select acctNo from " + CARD_ACCOUNTS_TABLE, null);
            int xl = cursor.getCount();
            int x = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                list.add(cursor.getString(0));
                cursor.moveToNext();
                x++;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    public ArrayList<String> getAllBills()
    {
        ArrayList list = new ArrayList();
        list.add("Tap to select");
        try
        {
            Cursor cursor = MyDb.rawQuery("select productName from " + BILLS_TABLE, null);
            int xl = cursor.getCount();
            int x = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                list.add(cursor.getString(0));
                cursor.moveToNext();
                x++;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    public ArrayList<String> getBillAccount(String Productname)
    {
        ArrayList list = new ArrayList();
        try
        {
            Cursor cursor = MyDb.rawQuery("select field1 from " + BILLS_TABLE +
                    " where productName = '" + Productname + "'", null);
            int xl = cursor.getCount();
            int x = 0;
            list.add("Tap to select");
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                list.add(cursor.getString(0));
                cursor.moveToNext();
                x++;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    public String getBillID(String billAccount)
    {
        String list = "";
        try
        {
            Cursor cursor = MyDb.rawQuery("select Bill_id from " + BILLS_TABLE +
                    " where field1 = '" + billAccount + "'", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                list = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    public String getBillInfoID(String billAccount)
    {
        String list = "";
        try
        {
            Cursor cursor = MyDb.rawQuery("select BillInfo_id from " + BILLS_TABLE +
                    " where field1 = '" + billAccount + "'", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                list = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    public boolean isFirstTime()
    {
        boolean isFirst = true;
        String first = null;
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from " + INSTALL_TABLE, null);
            int xl = cursor.getCount();
            cursor.close();
            if (xl > 0)
            {
                isFirst = false;
            }
        } catch (Exception ex)
        {
            ex.toString();
        }
        return isFirst;
    }

    public void setSignedUp()
    {
        try
        {
            MyDb.execSQL("insert into INSTALL_TABLE (signedOn) values(1);");
        } catch (Exception ex)
        {

        }
    }

    public String getCustomerName()
    {
        String name = null;
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from " + CUSTOMER_TABLE, null);
            int xl = cursor.getColumnCount();
            cursor.moveToFirst();
            name = cursor.getString(1).trim();
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return name;
    }

    public boolean saveCookie(String Cookie)
    {
        boolean IsDone = false;
        try
        {
            MyDb.execSQL("insert into " + COOKIE_TABLE + "(cookie) values('" + Cookie + "');");
            IsDone = true;
        } catch (Exception ex)
        {
            ex.toString();
        }
        return IsDone;
    }

    public String getCookie()
    {
        String COOKIE = "";
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from " + COOKIE_TABLE, null);
            int xl = cursor.getColumnCount();
            int _size = cursor.getCount();
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                COOKIE = cursor.getString(0).toString().trim();
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return COOKIE;
    }

    public boolean tokenRecieved()
    {
        boolean _rec = false;
        String token = "";
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from " + COOKIE_TABLE, null);
            int xl = cursor.getColumnCount();
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                token = cursor.getString(0).trim();
                if (token.length() > 3)
                {
                    _rec = true;
                }
                cursor.moveToNext();
            }

            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return _rec;
    }

    public void UpdateTransferTokenTable(String token)
    {
        try
        {
            MyDb.execSQL("Delete from" + TOKEN_TABLE);
            MyDb.execSQL("insert into " + TOKEN_TABLE + " (TOKEN,TOKEN_SET) values ('" +
                    token + "',1);");
        } catch (Exception ex)
        {
            ex.toString();
        }
    }

    public String getTransferToken()
    {
        String name = null;
        try
        {
            Cursor cursor = MyDb.rawQuery("select TOKEN from " + TOKEN_TABLE, null);
            int xl = cursor.getColumnCount();
            cursor.moveToFirst();
            name = cursor.getString(1).trim();
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return name;
    }

    public ArrayList<TransferBeneficiaryClass.RootObject> getBeneficiaries()
    {
        ArrayList<TransferBeneficiaryClass.RootObject> list = new ArrayList<>();
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from "
                    + TRANSFER_BENEFICIARIES, null);
            int xl = cursor.getCount();
            int x = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                TransferBeneficiaryClass.RootObject dd = new TransferBeneficiaryClass.RootObject();
                dd.transferBeneficiary.id = Integer.parseInt(cursor.getString(0).trim());
                dd.transferBeneficiary.transferProduct.productName = cursor.getString(1).trim();
                dd.transferBeneficiary.field1 = cursor.getString(2).trim();
                dd.transferBeneficiary.field2 = cursor.getString(3).trim();
                dd.transferBeneficiary.field3 = cursor.getString(4).trim();
                dd.transferBeneficiary.field4 = cursor.getString(5).trim();
                dd.transferBeneficiary.field5 = cursor.getString(6).trim();
                list.add(dd);
                cursor.moveToNext();
                x++;
            }

            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    public String getBeneficiaryID(String beneAccount)
    {
        String list = "";
        try
        {
            Cursor cursor = MyDb.rawQuery("select Beneficiary_id from "
                            + TRANSFER_BENEFICIARIES +
                            " where field2 = '" + beneAccount + "';"
                    , null);
            int xl = cursor.getCount();
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                list = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    public void setAutoPayment(int day, int month, int year, String billType, String billID)
    {
        try
        {
            MyDb.execSQL("insert into AUTO_PAYMENT_TABLE (billID,billType,repaymentDate) " +
                    "values(");
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void UpdateTemporalProducts(
            String id, String productName,
            String field1, String field2,
            String field3, String field4, String field5,
            String field6, String field7, String field8,
            String field9)
    {
        try
        {
            MyDb.execSQL("insert into " + TEMPORAL_BILLS_PRODUCTS + "(id,productName,field1" +
                    " field2," +
                    "field3,field4,field5," +
                    "field6,field7,field8," +
                    "field9)" +
                    " values ('" +
                    id + "','" +
                    productName + "','" +
                    field1 + "','" +
                    field2 + "','" +
                    field3 + "','" +
                    field4 + "','" +
                    field5 + "','" +
                    field6 + "','" +
                    field7 + "','" +
                    field8 + "','" +
                    field9 + "'+);");
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void DeleteTemporalTable()
    {
        try
        {
            MyDb.execSQL("DELETE FROM " + TEMPORAL_BILLS_PRODUCTS);
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void DeleteCardAccountsTable()
    {
        try
        {
            MyDb.execSQL("DELETE FROM " + CARD_ACCOUNTS_TABLE);
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void DeleteAccountsTable()
    {
        try
        {
            MyDb.execSQL("DELETE FROM " + ACCOUNTS_TABLE);
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void DeleteBillsTable()
    {
        try
        {
            MyDb.execSQL("DELETE FROM " + BILLS_TABLE);
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void DeleteCookieTable()
    {
        try
        {
            MyDb.execSQL("DELETE FROM " + COOKIE_TABLE);
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public void DeleteBalancesTable()
    {
        try
        {
            MyDb.execSQL("DELETE FROM " + BALANCES_TABLE);
        } catch (Exception ex)
        {
            ex.toString();
        }

    }

    public ArrayList<String> getBillProducts()
    {
        ArrayList list = new ArrayList();
        try
        {
            Cursor cursor =
                    MyDb.rawQuery("select productName from " + TEMPORAL_BILLS_PRODUCTS
                            , null);
            int xl = cursor.getCount();
            int x = 0;
            list.add("Tap to select");
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                list.add(cursor.getString(0));
                cursor.moveToNext();
                x++;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    public ArrayList<String> getProductFields(String productName)
    {
        ArrayList list = new ArrayList();
        try
        {
            Cursor cursor =
                    MyDb.rawQuery("select * from " + TEMPORAL_BILLS_PRODUCTS
                            + " where productName = '" + productName + "';", null);
            int xl = cursor.getCount();
            cursor.moveToFirst();
            int count = cursor.getColumnCount();

            for (int x = 3; x < count; x++)
            {
                list.add(cursor.getString(0).trim());
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;

    }

    //check if there is a payment for that day
    public Boolean IsTherePayment(String schedule_dt)
    {
        boolean IsDue = false;
        try
        {
            Cursor cursor =
                    MyDb.rawQuery("select * from " + BILL_PAYMENT_SCHEDULES
                            + " where status=0 and " +
                            "schedule_dt ='" + schedule_dt + "';", null);
            //cursor.moveToFirst();

            if (cursor.getCount() > 0)
            {
                IsDue = true;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return IsDue;
    }

    public boolean EasyLogin()
    {
        boolean IsEasy = false;
        try
        {
            Cursor cursor = MyDb.rawQuery("select easyLogin from " + SETTINGS_TABLE + ";", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                String ddd = cursor.getString(0).trim();
                if (ddd.equalsIgnoreCase("1"))
                {
                    IsEasy = true;
                }
                cursor.moveToNext();
            }

        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return IsEasy;
    }

    public ArrayList<Bill_Schedule_Class.RootObject> fetchBillsSchedule()
    {
        ArrayList<Bill_Schedule_Class.RootObject> _Schedule = new ArrayList<>();
        Bill_Schedule_Class.RootObject scheduler1 = new Bill_Schedule_Class.RootObject();

        Cursor cursor =
                MyDb.rawQuery("select * from " + BILL_PAYMENT_SCHEDULES
                        + " where status= 0;", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {

            scheduler1.billPayment.id = Integer.parseInt(cursor.getString(0));
            scheduler1.billPayment.billInformation.id = cursor.getString(1);
            scheduler1.billPayment.id = Integer.parseInt(cursor.getString(2));
            scheduler1.billPayment.productName = cursor.getString(3);
            scheduler1.billPayment.amount = Integer.parseInt(cursor.getString(4));
            scheduler1.billPayment.schedule_dt = cursor.getString(5);
            scheduler1.billPayment.status = Integer.parseInt(cursor.getString(6));
            _Schedule.add(scheduler1);
            cursor.moveToNext();
        }
        cursor.close();

        return _Schedule;
    }

    public void setEasyLogin()
    {
        try
        {
            try
            {
                MyDb.execSQL("Update " + SETTINGS_TABLE + " set " +
                        "easyLogin = '1'");
            } catch (Exception ex)
            {
                ex.getMessage();
            }

        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public ArrayList<String> FetchPrompts()
    {
        ArrayList list = new ArrayList();
        try
        {
            Cursor cursor = MyDb.rawQuery("select " +
                    "TRAN_DATE," +
                    "ACCOUNT," +
                    "AMOUNT," +
                    "TYPE," +
                    "DESCRIPTION,CURRENCY,BRANCH" +
                    " from " + NOTIFICATION_LOG
                    + "Order by TRAN_DATE DSC", null);
            int xl = cursor.getCount();
            cursor.moveToFirst();
            String res = "";
            while (!cursor.isAfterLast())
            {
                for (int x = 0; x < cursor.getColumnCount(); x++)
                {
                    res = res + cursor.getString(x).trim() + ";";
                }
                list.add(res);
                cursor.moveToNext();
                res = "";
            }

            cursor.close();
        } catch (Exception ex)
        {
            ex.toString();
        }
        return list;
    }

    public void UPDATE_PROMPTS_TABLE(
            String TRAN_DATE, String ACCOUNT, String AMOUNT, String TYPE,
            String DESCRIPTION, String CURRENCY, String BRANCH)
    {
        try
        {

            MyDb.execSQL("Insert into " + NOTIFICATION_LOG + " (TRAN_DATE,ACCOUNT,AMOUNT,TYPE," +
                    "DESCRIPTION,CURRENCY,BRANCH)" +
                    " values ('" +
                    TRAN_DATE + "'," +
                    ACCOUNT + ",'" +
                    AMOUNT + "','" +
                    TYPE + "','" +
                    DESCRIPTION + "','" +
                    CURRENCY + "','" +
                    BRANCH + "');");
        } catch (Exception ex)
        {
            ex.toString();

        }
    }

    public void SaveSettings(SettingClass.RootObject _settingsClass)
    {
        try
        {
            MyDb.execSQL("Update " + SETTINGS_TABLE + " set " +
                    "easyLogin = '" + _settingsClass._settingInfo.easyLogin + "'," +
                    "autoPayment = '" + _settingsClass._settingInfo.autoPay + "'," +
                    "Locator = '" + _settingsClass._settingInfo.locator + "'," +
                    "notify = '" + +_settingsClass._settingInfo.notify + "'"
            );
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }

    public SettingClass.RootObject fetchSettings()
    {
        SettingClass.RootObject settings = new SettingClass.RootObject();
        try
        {
            Cursor cursor = MyDb.rawQuery("select * from " + SETTINGS_TABLE, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                settings._settingInfo.easyLogin = Integer.parseInt(cursor.getString(0));
                settings._settingInfo.autoPay = Integer.parseInt(cursor.getString(1));
                settings._settingInfo.locator = Integer.parseInt(cursor.getString(2));
                settings._settingInfo.notify = Integer.parseInt(cursor.getString(3));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }


        return settings;
    }

    public void initSettings()
    {
        try
        {
            MyDb.execSQL("insert into " + SETTINGS_TABLE + " (easyLogin,autoPayment,Locator,notify)" +
                    " Values ('0','0','0','0');");
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public boolean isNotifyAllow()
    {
        boolean allow = false;
        try
        {
            Cursor cursor = MyDb.rawQuery("select notify from " + SETTINGS_TABLE, null);
            cursor.moveToFirst();
            if (cursor.getString(0).trim().equals("1"))
            {
                allow = true;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return allow;
    }

    public void SaveTtransaction(TransactionClass.Transaction transactionClass)
    {
        try
        {
            MyDb.execSQL("INSERT INTO " + NOTIFICATION_LOG
                    + " (TRAN_DATE,ACCOUNT,AMOUNT,TYPE,DESCRIPTION,CURRENCY,BRANCH) Values" +
                    " ('" +
                    transactionClass._date + "','" +
                    transactionClass._account + "','" +
                    transactionClass._amount + "','" +
                    transactionClass._type + "','" +
                    transactionClass._description + "','" +
                    transactionClass._currency + "','" +
                    transactionClass._branch + "');"
            );
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }

    public ArrayList<TransactionClass.Transaction> FetchRecords()
    {
        ArrayList<TransactionClass.Transaction> fetchRecords = new ArrayList<>();

        try
        {
            Cursor cursor = MyDb.rawQuery("SELECT * FROM " + NOTIFICATION_LOG, null);
            cursor.moveToFirst();
            int count = cursor.getColumnCount();
            TransactionClass.Transaction transaction;
            while (!cursor.isAfterLast())
            {
                transaction = new TransactionClass.Transaction();
                transaction._date = cursor.getString(0);
                transaction._account = cursor.getString(1);
                transaction._amount = cursor.getString(2);
                transaction._type = cursor.getString(3);
                transaction._description = cursor.getString(4);
                transaction._currency = cursor.getString(5);
                transaction._branch = cursor.getString(6);

                fetchRecords.add(transaction);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return fetchRecords;
    }

    public void saveTempAccounts(ArrayList<String> data)
    {
        try
        {
            ContentValues values = new ContentValues();
            for (String acct : data)
            {
                values.put("ACCT_NO", acct);
                MyDb.insert("TEMP_ACCT_TABLE", null, values);
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public ArrayList<String> getTempAccounts()
    {
        ArrayList<String> data = new ArrayList<>();
        try
        {
            Cursor cursor = MyDb.rawQuery("Select ACCT_NO from TEMP_ACCT_TABLE", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                data.add(cursor.getString(0).trim());
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return data;
    }

    public void deleteTempAccts()
    {
        try
        {
            MyDb.delete("TEMP_ACCT_TABLE", null, null);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public boolean isLoggedIn()
    {
        boolean loggedIn = false;
        try
        {
            Cursor cursor = MyDb.rawQuery("select LoggedIn from LoginTable", null);
            cursor.moveToFirst();
            if (cursor.getString(0).trim().equals("Yes"))
            {
                loggedIn = true;
            }
            cursor.close();
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return loggedIn;
    }

    public void deleteLoginTable()
    {
        try
        {
            MyDb.delete("LoginTable", null, null);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void setIsLoggedIN()
    {
        try
        {
            MyDb.delete("LoginTable", null, null);
            ContentValues values = new ContentValues();
            values.put("LoggedIn", "Yes");
            MyDb.insert("LoginTable", null, values);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void deleteIsLoggedIN()
    {
        try
        {
            MyDb.delete("LoginTable", null, null);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }
}
