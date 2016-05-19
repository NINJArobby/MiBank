package zenithbank.com.gh.mibank.Notification.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import zenithbank.com.gh.mibank.Notification.Classes.N_HistoryClass;
import zenithbank.com.gh.mibank.Notification.Classes.N_NotificationMessage;
import zenithbank.com.gh.mibank.Notification.System.N_SystemDBEngine;
import zenithbank.com.gh.mibank.R;


/**
 * Created by Robby on 6/20/2014.
 */
public class ReceiveActivity extends Activity
{
    public static Activity receive;
    Intent _records;
    N_SystemDBEngine dbEngine;
    View popupView;
    N_SystemDBEngine n_systemDBEngine;
    private PopupWindow pwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received);

        try
        {
            dbEngine = new N_SystemDBEngine();
            dbEngine.Init(this);
            TextView txtDate = (TextView) findViewById(R.id.txtDate);
            TextView txtAcctNo = (TextView) findViewById(R.id.txtAcctNo);
            TextView txtAmount = (TextView) findViewById(R.id.txtAmount);
            TextView txttp = (TextView) findViewById(R.id.txttp);
            TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
            TextView txtcurrency = (TextView) findViewById(R.id.txtcurrency);
            TextView txt_Branch = (TextView) findViewById(R.id.txt_Branch);
            TextView txt_Balance = (TextView) findViewById(R.id.txt_Balance);
            Button btnRec = (Button) findViewById(R.id.Recdone);

            receive = this;
            _records = new Intent(this, NotifyHome.class);
            Intent intent = getIntent();
            String message = intent.getExtras().getString("message");
            if (message.contains("ACTIVITY||"))
            {
                N_HistoryClass.RootObject data =
                        new Gson().fromJson(message, N_HistoryClass.RootObject.class);
                txtDate.setText(data.trsxDate);
                txtAcctNo.setText(data.trsxAcctno);
                txtAmount.setText(data.trsxAmount);
                txttp.setText(data.trsxType);
                txtDescription.setText(data.trsxDecsription);
                txtcurrency.setText(data.trsxCurrency);
                txt_Branch.setText(data.trsxBranch);
                txt_Balance.setText(data.trsxBalance);
            }
            else
            {
                N_NotificationMessage.RootObject data =
                        new Gson().fromJson(message, N_NotificationMessage.RootObject.class);


                txtDate.setText(data.messageBody.date);
                txtAcctNo.setText(data.messageBody.acctNo);
                txtAmount.setText(String.valueOf(data.messageBody.amount));
                txttp.setText(data.messageBody.tranxType);
                txtDescription.setText(data.messageBody.description);
                txtcurrency.setText(data.messageBody.currency);
                txt_Branch.setText(data.messageBody.branchName);
                txt_Balance.setText(data.messageBody.balance);
            }
            //get json and parse to object

            //dbEngine.updateNotificationTable(data);

            btnRec.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onBackPressed();
                }
            });


        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    @Override
    public void onBackPressed()
    {
        try
        {
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("Would you like to view your accounts history?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            startActivity(new Intent(getApplicationContext(), NotifyHome.class));
                            ReceiveActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ReceiveActivity.this.finish();
                        }
                    })

                    .setIcon(R.drawable.alert)
                    .show();
        } catch (Exception ex)
        {
            ex.getMessage();
        }


    }

    public void showPopup2(
            String date, String account, String amount, String type, String desc,
            String currency, String branch)
    {
        try
        {
            LayoutInflater layoutInflater
                    = (LayoutInflater) ReceiveActivity.this
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            popupView = layoutInflater.inflate(R.layout.activity_received, null);
            pwindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            final TextView txtDate = (TextView) popupView.findViewById(R.id.txtDate);
            final TextView txtAcctNo = (TextView) popupView.findViewById(R.id.txtAcctNo);
            final TextView txtAmount = (TextView) popupView.findViewById(R.id.txtAmount);
            final TextView txttp = (TextView) popupView.findViewById(R.id.txttp);
            final TextView txtDescription = (TextView) popupView.findViewById(R.id.txtDescription);
            final TextView txtcurrency = (TextView) popupView.findViewById(R.id.txtcurrency);
            final TextView txt_Branch = (TextView) popupView.findViewById(R.id.txt_Branch);
            final Button btnRec = (Button) popupView.findViewById(R.id.Recdone);

            btnRec.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onBackPressed();
                }
            });

            txtDate.setText("Date: " + date);
            txtAcctNo.setText("Account: " + account);
            txtAmount.setText("Amount: " + amount);
            txttp.setText("Type: " + type);
            txtDescription.setText("Desc: " + desc);
            txtcurrency.setText("Currency: " + currency);
            txt_Branch.setText("Branch: " + branch);


            new Handler().postDelayed(new Runnable()
            {

                public void run()
                {
                    pwindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                }

            }, 100L);


            //pwindow.showAtLocation(popupView, Gravity.CENTER, 300, 300);
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }


}
