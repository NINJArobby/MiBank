package zenithbank.com.gh.mibank.Ibank.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Ibank.Classes.I_TransferProductListMessage;
import zenithbank.com.gh.mibank.Ibank.Classes.TransferBeneficiaryClass;
import zenithbank.com.gh.mibank.Ibank.Classes.TransferProductClass;
import zenithbank.com.gh.mibank.Ibank.System.I_BusStation;
import zenithbank.com.gh.mibank.Ibank.System.I_TransferProductUpdater;
import zenithbank.com.gh.mibank.Ibank.System.IbankDatabaseEngine;
import zenithbank.com.gh.mibank.Ibank.System.IbankServiceEngine;
import zenithbank.com.gh.mibank.R;

/**
 * Created by Robby on 4/5/2016.
 */
public class AddBeneficiary extends Activity
{
    TextView f1, f2, f3, f4, f5;
    EditText t1, t2, t3, t4, t5;
    Spinner produtList;
    ProgressDialog progressDialog;
    Button btnAdd;

    ArrayList<TransferProductClass.RootObject> trans;
    TransferProductClass.RootObject _tranSel;

    IbankServiceEngine ibankServiceEngine;
    IbankDatabaseEngine ibankDatabaseEngine;
    TransferBeneficiaryClass.RootObject benef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_benefciary_layout);
        I_BusStation.getBus().register(this);
        ibankServiceEngine = new IbankServiceEngine();
        ibankDatabaseEngine = new IbankDatabaseEngine();
        ibankDatabaseEngine.Init(this);
        benef = new TransferBeneficiaryClass.RootObject();

        t1 = (EditText) findViewById(R.id.edtTxt1);
        t2 = (EditText) findViewById(R.id.edtTxt2);
        t3 = (EditText) findViewById(R.id.edtTxt3);
        t4 = (EditText) findViewById(R.id.edtTxt4);
        t5 = (EditText) findViewById(R.id.edtTxt5);

        f1 = (TextView) findViewById(R.id.txtVw1);
        f2 = (TextView) findViewById(R.id.txtVw2);
        f3 = (TextView) findViewById(R.id.txtVw3);
        f4 = (TextView) findViewById(R.id.txtVw4);
        f5 = (TextView) findViewById(R.id.txtVw5);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        produtList = (Spinner) findViewById(R.id.SpnTransType);
        produtList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    _tranSel = trans.get(position);
                    f1.setText(_tranSel.transferProduct.field1);
                    f2.setText(_tranSel.transferProduct.field2);
                    f3.setText(_tranSel.transferProduct.field3);
                    f4.setText(_tranSel.transferProduct.field4);
                    f5.setText(_tranSel.transferProduct.field5);
                } catch (Exception ex)
                {
                    ex.getMessage();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                benef.transferBeneficiary.field1 = t1.getText().toString().trim();
                benef.transferBeneficiary.field2 = t2.getText().toString().trim();
                benef.transferBeneficiary.field3 = t3.getText().toString().trim();
                benef.transferBeneficiary.field4 = t4.getText().toString().trim();
                benef.transferBeneficiary.field5 = t5.getText().toString().trim();
                benef.transferBeneficiary.transferProduct.productName =
                        _tranSel.transferProduct.productName;
                benef.transferBeneficiary.transferProduct.productCode =
                        _tranSel.transferProduct.productCode;
                benef.transferBeneficiary.transferProduct.field1 = _tranSel.transferProduct.field1;
                benef.transferBeneficiary.transferProduct.field2 = _tranSel.transferProduct.field2;
                benef.transferBeneficiary.transferProduct.field3 = _tranSel.transferProduct.field3;
                benef.transferBeneficiary.transferProduct.field4 = _tranSel.transferProduct.field4;
                benef.transferBeneficiary.transferProduct.field5 = _tranSel.transferProduct.field5;
                benef.transferBeneficiary.valid = _tranSel.transferProduct.valid;

                progressDialog = ProgressDialog.show(AddBeneficiary.this,
                        "Processing...", "Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                new AddBenef().execute();
            }
        });

        Intent fetchersService = new Intent(this, I_TransferProductUpdater.class);
        startService(fetchersService);
        progressDialog = ProgressDialog.show(AddBeneficiary.this,
                "Processing...", "Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

    }

    @Subscribe
    public void onProductListReceived(final I_TransferProductListMessage message)
    {
        final ArrayList<String> dd = new ArrayList<>();
        progressDialog.dismiss();
        trans = message.getBalances();
        try
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (TransferProductClass.RootObject d : message.getBalances())
                            {
                                dd.add(d.transferProduct.productName);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                                    (getApplicationContext(), android.R.layout.simple_spinner_item, dd);

                            dataAdapter.setDropDownViewResource
                                    (android.R.layout.simple_spinner_dropdown_item);
                            produtList.setAdapter(dataAdapter);
                        }
                    });
                }
            });
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private void AlertError()
    {
        new AlertDialog.Builder(this)
                .setTitle("Error!!!")
                .setMessage("Something went wrong. Please try Again")
                .setNegativeButton("ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                }).show();
    }

    private void AlertOK()
    {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Setup Complete")
                .setNegativeButton("ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                }).show();
    }

    class AddBenef extends AsyncTask<String, String, String>
    {
        boolean done = false;

        @Override
        protected String doInBackground(String... params)
        {
            done = ibankServiceEngine.AddNewBenefeciary(benef, ibankDatabaseEngine.getCookie());
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressDialog.dismiss();
            if (done)
            {
                AlertOK();
            }
            else
            {
                AlertError();
            }
        }
    }
}
