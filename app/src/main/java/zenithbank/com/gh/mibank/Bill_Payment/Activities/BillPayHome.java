package zenithbank.com.gh.mibank.Bill_Payment.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Bill_Payment.Adapters.BillsAdapter;
import zenithbank.com.gh.mibank.Bill_Payment.Classes.BillPaymentClass;
import zenithbank.com.gh.mibank.Bill_Payment.Classes.BillPaymentRequestClass;
import zenithbank.com.gh.mibank.Bill_Payment.Classes.BillsClass;
import zenithbank.com.gh.mibank.Bill_Payment.Classes.ExpressREsponseClass;
import zenithbank.com.gh.mibank.Bill_Payment.System.BillPayment_ServiceEngine;
import zenithbank.com.gh.mibank.Bill_Payment.System.Bill_Database_Engine;
import zenithbank.com.gh.mibank.Ibank.System.IbankDatabaseEngine;
import zenithbank.com.gh.mibank.R;

/**
 * Created by Robby on 5/3/2016.
 */
public class BillPayHome extends ActionBarActivity
{
    FloatingActionButton fab;
    SwipeMenuListView billList;
    Bill_Database_Engine bill_database_engine;
    SwipeMenuCreator swipeMenuCreator;
    BillsAdapter adapter;
    ArrayList<BillsClass> data;
    BillPayment_ServiceEngine serviceEngine;

    Spinner fromAcctSpn;
    Spinner fromAcct;
    String frmacct;
    AlertDialog.Builder builder;
    IbankDatabaseEngine ibankDatabaseEngine;
    Spinner BillType;
    EditText txtbillNumber;
    EditText txtbillName;
    ArrayList<String> acctList;
    EditText txtBillAmount;
    LayoutInflater inflater;
    DrawerLayout mDrawerLayout;
    ActionBar actionBar;
    private View saveBillView;
    private AlertDialog myDialog;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billhome_layout);

        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.reed));
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        try
        {
            //
            billList = (SwipeMenuListView) findViewById(R.id.lsBIlls);
            bill_database_engine = new Bill_Database_Engine();
            bill_database_engine.Init(this);
            serviceEngine = new BillPayment_ServiceEngine();
            fromAcct = (Spinner) findViewById(R.id.FromAcctSpinner);
            fab = (FloatingActionButton) findViewById(R.id.addfab);
            ibankDatabaseEngine = new IbankDatabaseEngine();
            ibankDatabaseEngine.Init(this);
            acctList = ibankDatabaseEngine.getAccounts();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, acctList);

            dataAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            fromAcct.setAdapter(dataAdapter);

            data = bill_database_engine.getAllBillSetups();
            adapter = new BillsAdapter(this, data);
            swipeMenuCreator = new SwipeMenuCreator()
            {
                @Override
                public void create(SwipeMenu menu)
                {
                    // create "open" item
                    SwipeMenuItem openItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    openItem.setWidth(200);
                    // set item title
                    openItem.setTitle("Pay");
                    // set item title fontsize
                    openItem.setTitleSize(18);
                    // set item title font color
                    openItem.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(openItem);
                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    // set item width
                    deleteItem.setWidth(200);
                    // set a icon
                    deleteItem.setIcon(R.drawable.ic_delete_sweep);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };

            billList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
                {
                    try
                    {
                        BillsClass bill = data.get(position);
                        BillPaymentRequestClass.RootObject request = new BillPaymentRequestClass.RootObject();
                        request.amount = Integer.parseInt(bill.billAmount);
                        request.bankAccountNumber = frmacct;
                        request.serviceAccountNumber = bill.billNumber;
                        request.serviceType = bill.billType;
                        Time time = new Time();
                        time.setToNow();
                        request.transId = Long.toString(time.toMillis(false));
                        switch (index)
                        {
                            case 0:
                                //pay for item
                                ExpressREsponseClass.RootObject response =
                                        new Gson().fromJson(serviceEngine.MakePayment(request),
                                                ExpressREsponseClass.RootObject.class);
                                //try to pay b4 saving record
                                if (response.status == 1)
                                {
                                    BillPaymentClass payment = new BillPaymentClass();
                                    payment.billAmount = bill.billAmount;
                                    payment.billName = bill.billName;
                                    payment.billNumber = bill.billNumber;
                                    payment.billType = bill.billType;
                                    payment.PaymentDate = bill_database_engine.getCurrentDateTime();
                                    payment.PaymentStatus = true;
                                    bill_database_engine.saveExpressResponse(response);
                                    bill_database_engine.savePaymentHistory(payment);
                                }
                                else
                                {
                                    BillPaymentClass payment = new BillPaymentClass();
                                    payment.billAmount = bill.billAmount;
                                    payment.billName = bill.billName;
                                    payment.billNumber = bill.billNumber;
                                    payment.billType = bill.billType;
                                    payment.PaymentDate = bill_database_engine.getCurrentDateTime();
                                    payment.PaymentStatus = false;
                                    bill_database_engine.savePaymentHistory(payment);
                                    bill_database_engine.saveExpressResponse(response);
                                }
                                break;
                            case 1:
                                try
                                {
                                    bill_database_engine.DeleteBillSetup(bill.billNumber);
                                    data.remove(position);
                                    adapter.notifyDataSetChanged();
                                } catch (Exception ex)
                                {
                                    ex.getMessage();
                                }

                                //delete item
                                break;
                        }
                    } catch (Exception ex)
                    {
                        ex.getMessage();
                    }
                    return false;
                }
            });
            billList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            billList.setMenuCreator(swipeMenuCreator);
            billList.setAdapter(adapter);

            builder = new AlertDialog.Builder(BillPayHome.this);
            inflater = LayoutInflater.from(getApplicationContext());


            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        saveBillView = inflater.inflate(R.layout.add_bill_layout, null);
                        final BillsClass bill = new BillsClass();
                        BillType = (Spinner)
                                saveBillView.findViewById(R.id.spnFromAcct);
                        txtbillNumber = (EditText)
                                saveBillView.findViewById(R.id.txtbillNumber);
                        txtbillName = (EditText)
                                saveBillView.findViewById(R.id.txtbillNane);
                        txtBillAmount = (EditText)
                                saveBillView.findViewById(R.id.txtBillAmount);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (BillPayHome.this, android.R.layout.simple_spinner_item, getResources().
                                        getStringArray(R.array.ExpressPayTypes));

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);
                        BillType.setAdapter(dataAdapter);

                        builder.setView(saveBillView);
                        builder.setTitle("Add A Bill");
                        builder.setIcon(R.drawable.logo3);
                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                bill.Date = bill_database_engine.getCurrentDateTime();
                                bill.billNumber = txtbillNumber.getText().toString().trim();
                                bill.billName = txtbillName.getText().toString().trim();
                                bill.billAmount = txtBillAmount.getText().toString().trim();
                                bill.billType = BillType.getSelectedItem().toString().trim();

                                if (bill.billType.contains("Tap"))
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Select Valid Bill type", Toast.LENGTH_LONG).show();
                                    myDialog.dismiss();
                                }
                                else
                                {
                                    bill_database_engine.AddBillPayment(bill);
                                    data = bill_database_engine.getAllBillSetups();
                                    adapter = new BillsAdapter(BillPayHome.this, data);
                                    billList.setAdapter(adapter);

                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        });
                        // builder.setCancelable(false);
                        myDialog = builder.create();
                        myDialog.show();
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
    }


}
