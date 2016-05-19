package zenithbank.com.gh.mibank.Ibank.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Ibank.Classes.TransferRequestClass;
import zenithbank.com.gh.mibank.Ibank.System.IbankDatabaseEngine;
import zenithbank.com.gh.mibank.Ibank.System.IbankServiceEngine;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;
import zenithbank.com.gh.mibank.Notification.System.N_SystemService;
import zenithbank.com.gh.mibank.R;


public class TransferModule extends Activity
{
    IbankDatabaseEngine ibankDatabaseEngine;
    EditText Access, user, pass;
    Button activate, login;
    ProgressDialog progressDialog;
    DrawerLayout mDrawerLayout;

    N_SystemService n_systemService;
    System_DBEngine system_dbEngine;
    IbankServiceEngine ibankServiceEngine;
    TransferRequestClass.RootObject transferRequest;

    String _access, _user, _pass, fromAcct, toAcct, amount, _type;

    PowerManager pm;
    PowerManager.WakeLock wl;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibank_signon_layout);
        Bundle extras = intent.getExtras();
        _type = intent.getStringExtra("Type");

        Access = (EditText) findViewById(R.id.edxtxAccesscode);
        user = (EditText) findViewById(R.id.edxtxUsername);
        pass = (EditText) findViewById(R.id.edxtxPassword);
        activate = (Button) findViewById(R.id.btn_login);

        n_systemService = new N_SystemService();
        ibankServiceEngine = new IbankServiceEngine();
        system_dbEngine = new System_DBEngine();
        system_dbEngine.Init(this);


        activate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _access = Access.getText().toString().trim();
                _user = user.getText().toString().trim();
                _pass = pass.getText().toString().trim();
                pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Performing login");
                wl.acquire();
            }


        });
    }

    class doTransfer extends AsyncTask<String, String, String>
    {
        boolean done = false;
        String transResponse;
        String error;

        @Override
        protected String doInBackground(String... params)
        {
            ArrayList AuthenticateResponse = null;
            try
            {
                //returns Authenticated cookie
                ibankDatabaseEngine.DeleteCookieTable();
                ibankDatabaseEngine.saveCookie(AuthenticateResponse.get(0).toString().trim());
                if (!ibankServiceEngine.CanTransfer(fromAcct, ibankDatabaseEngine.getCookie()))
                {
                    throw new Exception("NotTransferAccount");
                }
                switch (_type)
                {
                    case "Intra":
                        transferRequest = new TransferRequestClass.RootObject();
                        transferRequest.transferRequest.amt =
                                Double.parseDouble(intent.getStringExtra("Amount"));
                        transferRequest.transferRequest.fromAcct =
                                intent.getStringExtra("FromAcct");
                        transferRequest.transferRequest.toAcct =
                                intent.getStringExtra("ToAcct");
                        transResponse = ibankServiceEngine.
                                DoIntraAccountTransfer(transferRequest,
                                        ibankDatabaseEngine.getCookie());
                        if (transResponse.equals("TransferErrorException"))
                        {
                            throw new Exception("transferError");
                        }
                        done = true;
                        break;
                    case "Inter":

                        break;

                    case "Visa":

                        break;
                }
            } catch (Exception ex)
            {
                error = ex.getMessage();
                Log.d("SIGN_UP_ERROR:", ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressDialog.dismiss();
            wl.release();
            if (done)
            {

            }
            else
            {
                if (error.equals("NotTransferAccount"))
                {
                    Toast.makeText(getApplicationContext(), "You cannot transfer from this account",
                            Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                if (error.equals("LoginError"))
                {
                    Toast.makeText(getApplicationContext(), "Error!!!..Check Credentials",
                            Toast.LENGTH_LONG).show();
                }
                if (error.equals("transferError"))
                {
                    Toast.makeText(getApplicationContext(), "Error!!!..Not Enough Funds",
                            Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        }
    }


}

