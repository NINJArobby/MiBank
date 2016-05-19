package zenithbank.com.gh.mibank.Main.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import zenithbank.com.gh.mibank.Ibank.System.IbankDatabaseEngine;
import zenithbank.com.gh.mibank.Main.Classes.AuthenticateResponseClass;
import zenithbank.com.gh.mibank.Main.Classes.SystemConfigurationClass;
import zenithbank.com.gh.mibank.Main.Classes.UserAccessClass;
import zenithbank.com.gh.mibank.Main.System.MibankServiceEngine;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;
import zenithbank.com.gh.mibank.R;
import zenithbank.com.gh.mibank.TouchTransfer.Activities.TouchTransHome;

public class Start extends AppCompatActivity
{
    EditText txtUsername, txtPassword;
    Button btnLogin;

    System_DBEngine systemDbEngine;
    MibankServiceEngine mibankServiceEngine;

    String username, password;
    AlertDialog alertDialog;
    UserAccessClass userAccessClass;
    IbankDatabaseEngine ibankDatabaseEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            systemDbEngine = new System_DBEngine();
            systemDbEngine.Init(this);
            userAccessClass = new UserAccessClass();
            mibankServiceEngine = new MibankServiceEngine();
            ibankDatabaseEngine = new IbankDatabaseEngine();
            ibankDatabaseEngine.Init(this);
            ibankDatabaseEngine.deleteLoginTable();

            if (systemDbEngine.isFirstUse())
            {
                setContentView(R.layout.activity_start);

                txtPassword = (EditText) findViewById(R.id.txtPass);
                txtUsername = (EditText) findViewById(R.id.txtUser);
                btnLogin = (Button) findViewById(R.id.btnIbankLogIN);

                btnLogin.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            userAccessClass.username = txtUsername.getText().toString().trim();
                            userAccessClass.password = txtPassword.getText().toString().trim();
                            alertDialog = new SpotsDialog(Start.this);
                            alertDialog.setTitle("Processing...");
                            alertDialog.setMessage("Processing...");
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                            //new doLoginIfFirstTime().execute();
                            startActivity(new Intent(getApplicationContext(), TouchTransHome.class));

                        } catch (Exception ex)
                        {
                            ex.getMessage();
                        }
                    }
                });
            }
            else
            {
                SystemConfigurationClass conf = systemDbEngine.getConfiguration();
                txtPassword = (EditText) findViewById(R.id.input_password);
                txtUsername = (EditText) findViewById(R.id.txtUser);
                if (conf.SYSTEM_EASY_LOGIN == 1)
                {
                    LocalAuthenticate();
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), TouchTransHome.class));
                    //startActivity(new Intent(getApplicationContext(), IbankHome.class));
                    Start.this.finish();
                }
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private void LocalAuthenticate()
    {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.login_view, null);

        final EditText customText1 = (EditText) customView.findViewById(R.id.txtUser);
        final EditText customText2 = (EditText) customView.findViewById(R.id.txtPass);
        Button SendButton = (Button) customView.findViewById(R.id.btnLogin);
        SendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (systemDbEngine.CheckPassword(customText1.getText().toString().trim(),
                        customText2.getText().toString().trim()))
                {
                    startActivity(new Intent(Start.this, MainHUb2.class));
                    Start.this.finish();
                }
                else
                {
                    LocalAuthenticate();
                    Toast.makeText(Start.this, "Error...Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    //do first login and save creds locally
    class doLoginIfFirstTime extends AsyncTask<String, String, String>
    {

        boolean done = false;

        @Override
        protected String doInBackground(String... params)
        {

            String res = mibankServiceEngine.Authenticate(userAccessClass);
            try
            {
                AuthenticateResponseClass.Data response =
                        new Gson().fromJson(res, AuthenticateResponseClass.Data.class);

                try
                {
                    if (!response.error.isEmpty())
                    {
                        throw new Exception("NotRegistered");
                    }
                } catch (NullPointerException ex)
                {
                    ex.getMessage();
                }

                systemDbEngine.SaveUserData(response);
                //systemDbEngine.setNewUseFlag();
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
            alertDialog.dismiss();
            if (done)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        new MaterialStyledDialog(Start.this)
                                .setTitle("Success!!!")
                                .setDescription("SignOn is Successful. You are encouraged to change" +
                                        " the default password after Activation")
                                .setIcon(getResources().getDrawable(R.drawable.happy))
                                .setPositive("OK", new MaterialDialog.SingleButtonCallback()
                                {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which)
                                    {
                                        systemDbEngine.saveAccessData(username, password);
                                        startActivity(new Intent(Start.this, ActivationClass.class));
                                        Start.this.finish();
                                    }
                                })
                                .withIconAnimation(true)
                                .setCancelable(false)
                                .show();
                    }
                });

            }
            else
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        new MaterialStyledDialog(Start.this)
                                .setTitle("Error!!!")
                                .setDescription("Something went wrong. please check credentials and" +
                                        " try again")
                                .setIcon(getResources().getDrawable(R.drawable.sad))
                                .setPositive("Retry", new MaterialDialog.SingleButtonCallback()
                                {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which)
                                    {

                                    }
                                })
                                .withIconAnimation(true)
                                .show();
                    }
                });
            }


        }
    }

    class doLogin extends AsyncTask<String, String, String>
    {

        boolean done = false;

        @Override
        protected String doInBackground(String... params)
        {

            String res = mibankServiceEngine.Authenticate(userAccessClass);
            try
            {
                AuthenticateResponseClass.Data response =
                        new Gson().fromJson(res, AuthenticateResponseClass.Data.class);

                try
                {
                    if (!response.error.isEmpty())
                    {
                        throw new Exception("NotRegistered");
                    }
                } catch (NullPointerException ex)
                {
                    ex.getMessage();
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
            alertDialog.dismiss();
            if (done)
            {
                startActivity(new Intent(Start.this, MainHUb2.class));
                Start.this.finish();
            }
            else
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        new MaterialStyledDialog(Start.this)
                                .setTitle("Error!!!")
                                .setDescription("Something went wrong. please check credentials and" +
                                        " try again")
                                .setIcon(getResources().getDrawable(R.drawable.sad))
                                .setPositive("Retry", new MaterialDialog.SingleButtonCallback()
                                {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which)
                                    {

                                    }
                                })
                                .withIconAnimation(true)
                                .show();
                    }
                });
            }


        }
    }


}

