package zenithbank.com.gh.mibank.Ibank.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Ibank.Classes.I_SmsMessage;
import zenithbank.com.gh.mibank.Ibank.Classes.TransferRequestClass;
import zenithbank.com.gh.mibank.Ibank.System.I_BusStation;
import zenithbank.com.gh.mibank.Ibank.System.IbankDatabaseEngine;
import zenithbank.com.gh.mibank.Ibank.System.IbankServiceEngine;
import zenithbank.com.gh.mibank.Main.Classes.SystemConfigurationClass;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;
import zenithbank.com.gh.mibank.R;

public class InterFrag extends Fragment
{
    Spinner fromAcct;
    FloatingActionButton floatingActionButton;
    ProgressDialog progressDialog;
    Button fabTokenRequest;

    String strFromAcct, strToAcct, strAmount, strToken, strDescription, recp_name;

    IbankServiceEngine ibankServiceEngine;
    IbankDatabaseEngine ibankDatabaseEngine;
    System_DBEngine system_dbEngine;

    EditText amt, toAcct, EdtxtToken, EdtxtDescription;
    TextView txtValidate;

    ArrayList<String> acctList;
    View view = null;

    public InterFrag()
    {
        // Required empty public constructor
    }

    public static InterFrag newInstance(String param1, String param2)
    {
        InterFrag fragment = new InterFrag();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = null;
        system_dbEngine = new System_DBEngine();
        system_dbEngine.Init(getActivity());
        ibankDatabaseEngine = new IbankDatabaseEngine();
        ibankDatabaseEngine.Init(getActivity());
        ibankServiceEngine = new IbankServiceEngine();
        SystemConfigurationClass conf = system_dbEngine.getConfiguration();
        I_BusStation.getBus().register(this);

        acctList = ibankDatabaseEngine.getAccounts();
        try
        {
            view = inflater.inflate(R.layout.fragment_inter, container, false);
            fromAcct = (Spinner) view.findViewById(R.id.SpnFromAccts);
            toAcct = (EditText) view.findViewById(R.id.EdtxToAcct);
            EdtxtToken = (EditText) view.findViewById(R.id.EdtxtToken);
            EdtxtDescription = (EditText) view.findViewById(R.id.EdtxtDescription);
            floatingActionButton = (FloatingActionButton) view.findViewById(R.id.Transfab);
            fabTokenRequest = (Button) view.findViewById(R.id.fabTokenRequest);
            amt = (EditText) view.findViewById(R.id.EdtxAmount);
            txtValidate = (TextView) view.findViewById(R.id.txtValidate);
            txtValidate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    strToAcct = toAcct.getText().toString().trim();
                    if (strToAcct.isEmpty())
                    {
                        Toast.makeText(getActivity(), "Recipient account is empty",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog = ProgressDialog.show(getActivity(),
                                "Validating...", "Please Wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        new checkAccount().execute();
                    }
                }
            });

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.simple_spinner_item, acctList);

            dataAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            fromAcct.setAdapter(dataAdapter);


            floatingActionButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        strFromAcct = fromAcct.getSelectedItem().toString().trim();
                        strToAcct = toAcct.getText().toString().trim();
                        strAmount = amt.getText().toString().trim();
                        strToken = EdtxtToken.getText().toString().trim();
                        strDescription = EdtxtDescription.getText().toString().trim();

                        if (strAmount.isEmpty())
                        {
                            Toast.makeText(getActivity(), "Amount not set",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (strFromAcct.isEmpty())
                        {
                            Toast.makeText(getActivity(), "Debit Account not set",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (strToAcct.isEmpty())
                        {
                            Toast.makeText(getActivity(), "Credit Account not set",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (strDescription.isEmpty())
                        {
                            strDescription = "MiBank Transfer";
                        }
                        if (strToken.isEmpty())
                        {
                            Toast.makeText(getActivity(), "Amount not set",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog = ProgressDialog.show(getActivity(),
                                    "Processing...", "Please Wait...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            new doTransfer().execute();
                        }

                    } catch (Exception ex)
                    {
                        ex.getMessage();
                    }
                }
            });

            fabTokenRequest.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    new requestToken().execute();
                    floatingActionButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Requesting token",
                            Toast.LENGTH_SHORT).show();
                }
            });
            floatingActionButton.setVisibility(View.INVISIBLE);

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }

    void AlertSuccess()
    {
        new AlertDialog.Builder(getActivity())
                .setTitle("Success")
                .setMessage("Transfer Successful")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //todo write logic to save transfer logs
                    }
                })
                .setIcon(R.drawable.alert)
                .show();
    }

    @Subscribe
    public void onTokenRecieved(I_SmsMessage message)
    {
        try
        {
            EdtxtToken.setText(message.getMessage());
            Toast.makeText(getActivity(), "Token Received", Toast.LENGTH_SHORT).show();
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private void AlertValidateError()
    {
        new AlertDialog.Builder(getActivity())
                .setTitle("Validation Error")
                .setMessage("Not a valid account")
                .setPositiveButton("Close", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setIcon(R.drawable.logo3)
                .show();
    }

    private void AlertValidate(String name)
    {
        new AlertDialog.Builder(getActivity())
                .setTitle("Validation")
                .setMessage("Account belongs to " + name)
                .setPositiveButton("Close", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setIcon(R.drawable.logo3)
                .show();
    }

    void loadInter()
    {

    }

    class doTransfer extends AsyncTask<String, String, String>
    {
        Boolean done = false;
        String error = null;

        @Override
        protected String doInBackground(String... params)
        {
            String cookie1 = ibankDatabaseEngine.getCookie();
            try
            {
                if (!ibankServiceEngine.CanTransfer(strFromAcct, ibankDatabaseEngine.getCookie()))
                {
                    error = "NotTransferAcct";
                    throw new Exception("NotTransferAcct");
                }
                TransferRequestClass.RootObject transfer = new TransferRequestClass.RootObject();
                transfer.transferRequest = new TransferRequestClass.TransferRequest();
                transfer.transferRequest.amt = Double.valueOf(strAmount);
                transfer.transferRequest.fromAcct = strFromAcct;
                transfer.transferRequest.toAcct = strToAcct;
                transfer.transferRequest.description = strDescription;
                transfer.transferRequest.token = strToken;
                String res = ibankServiceEngine.DoIntraAccountTransfer(transfer, cookie1);
                if (res.contains("DeadCookie"))
                {
                    error = "DeadCookie";
                    throw new Exception("DeadCookieException");
                }
                if (res.contains("TransferErrorException"))
                {
                    error = "TransferErrorException";
                    throw new Exception("DeadCookieException");
                }
                done = true;
            } catch (Exception ex)
            {
                ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressDialog.dismiss();
            if (done)
            {
                AlertSuccess();
            }
            else
            {
                if (error.equalsIgnoreCase("DeadCookie"))
                {
                    //todo check if easy login
                    //doRelogin();
                    ibankDatabaseEngine.deleteLoginTable();
                    ibankDatabaseEngine.deleteIsLoggedIN();
                    getActivity().onBackPressed();
                    Toast.makeText(getActivity(), "You need to Activate again " +
                            strFromAcct, Toast.LENGTH_LONG).show();
                }
                else if (error.equalsIgnoreCase("NotTransferAcct"))
                {
                    Toast.makeText(getActivity(), "You cannot transfer from " +
                            strFromAcct, Toast.LENGTH_LONG).show();
                    //AlertError();
                }
            }
        }
    }

    class requestToken extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            //token/push
            ibankServiceEngine.RequestToken(ibankDatabaseEngine.getCookie());
            return null;
        }

    }

    class checkAccount extends AsyncTask<String, String, String>
    {
        boolean done = false;

        @Override
        protected String doInBackground(String... params)
        {

            try
            {
                recp_name = ibankServiceEngine.ValidateAccount(strToAcct);
                if (!recp_name.equalsIgnoreCase("NotValid"))
                {
                    done = true;
                }

            } catch (Exception ex)
            {
                ex.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (done)
            {
                progressDialog.dismiss();
                AlertValidate(recp_name);
            }
            else
            {
                progressDialog.dismiss();
                AlertValidateError();
            }
        }
    }
}
