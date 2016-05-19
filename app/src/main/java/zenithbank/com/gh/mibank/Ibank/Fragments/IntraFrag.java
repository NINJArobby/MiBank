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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Ibank.Classes.TransferRequestClass;
import zenithbank.com.gh.mibank.Ibank.System.IbankDatabaseEngine;
import zenithbank.com.gh.mibank.Ibank.System.IbankServiceEngine;
import zenithbank.com.gh.mibank.Main.System.BusStation;
import zenithbank.com.gh.mibank.R;


public class IntraFrag extends Fragment
{

    Spinner fromAcct, toAcct;
    FloatingActionButton floatingActionButton;
    ProgressDialog progressDialog;

    String strFromAcct, strToAcct, strAmount;

    IbankServiceEngine ibankServiceEngine;
    IbankDatabaseEngine ibankDatabaseEngine;
    EditText amt;

    ArrayList<String> acctList;

    public IntraFrag()
    {
        // Required empty public constructor
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
        View view = container;
        ibankDatabaseEngine = new IbankDatabaseEngine();
        ibankDatabaseEngine.Init(getActivity());
        ibankServiceEngine = new IbankServiceEngine();
        acctList = ibankDatabaseEngine.getAccounts();
        BusStation.getBus().register(this);

        try
        {
            view = inflater.inflate(R.layout.fragment_intra, container, false);
            fromAcct = (Spinner) view.findViewById(R.id.spnFromAcct);
            toAcct = (Spinner) view.findViewById(R.id.spnToAcct);
            floatingActionButton = (FloatingActionButton) view.findViewById(R.id.Transfab);
            amt = (EditText) view.findViewById(R.id.EdtxAmount);


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.simple_spinner_item, acctList);

            dataAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            fromAcct.setAdapter(dataAdapter);
            toAcct.setAdapter(dataAdapter);

            floatingActionButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        strFromAcct = fromAcct.getSelectedItem().toString().trim();
                        strToAcct = toAcct.getSelectedItem().toString().trim();
                        strAmount = amt.getText().toString().trim();

                        if (strAmount.isEmpty())
                        {
                            Toast.makeText(getActivity(), "Amount not set", Toast.LENGTH_SHORT).show();
                        }
                        if (strFromAcct.isEmpty())
                        {
                            Toast.makeText(getActivity(), "Debit Account not set", Toast.LENGTH_SHORT).show();
                        }
                        if (strToAcct.isEmpty())
                        {
                            Toast.makeText(getActivity(), "Credit Account not set", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog = ProgressDialog.show(getActivity(),
                                "Processing...", "Please Wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        new doTransfer().execute();
                    } catch (Exception ex)
                    {
                        ex.getMessage();
                    }


                }
            });

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

    void AlertError()
    {
        new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage("Error performing transfer. please try again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setIcon(R.drawable.alert)
                .show();
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
                transfer.transferRequest.token = "";
                transfer.transferRequest.description = "";
                //transfer.transferRequest.token = token;
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

    class checkCanTransfer extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            if (!ibankServiceEngine.CanTransfer(strFromAcct, ibankDatabaseEngine.getCookie()))
            {
                Toast.makeText(getActivity(), "You cannot transfer from this account",
                        Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }


}
