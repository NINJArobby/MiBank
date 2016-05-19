package zenithbank.com.gh.mibank.Main.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.squareup.otto.Subscribe;

import dmax.dialog.SpotsDialog;
import zenithbank.com.gh.mibank.Main.Classes.AuthenticateResponseClass;
import zenithbank.com.gh.mibank.Main.Classes.DeviceRegisterMessage;
import zenithbank.com.gh.mibank.Main.Classes.SystemConfigurationClass;
import zenithbank.com.gh.mibank.Main.System.BusStation;
import zenithbank.com.gh.mibank.Main.System.MiBankDeviceRegister;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;
import zenithbank.com.gh.mibank.R;


public class ActivationClass extends ActionBarActivity
{
    Intent fetchersService;
    AlertDialog alertDialog;
    CheckBox chkBxEnableAlert, chkBxHomeAlert, chkBxSound,
            chkBxEasyLoginIbank, chkBxEasyLogin, chkBxPassEnable,
            chkBxToken, chkBxInterbank, chkBxStmtDownload;
    Button btnSaveSettings;
    SystemConfigurationClass configuration, configuration2;
    AuthenticateResponseClass.Data userDestails;
    ActionBar bar;

    System_DBEngine system_dbEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiate_layout);

        BusStation.getBus().register(this);
        fetchersService = new Intent(this, MiBankDeviceRegister.class);

        chkBxEnableAlert = (CheckBox) findViewById(R.id.chkBxEnableAlert);
        chkBxHomeAlert = (CheckBox) findViewById(R.id.chkBxHomeAlert);
        chkBxSound = (CheckBox) findViewById(R.id.chkBxSound);
        chkBxEasyLoginIbank = (CheckBox) findViewById(R.id.chkBxEasyLoginIbank);
        chkBxStmtDownload = (CheckBox) findViewById(R.id.chkBxStmtDownload);
        chkBxEasyLogin = (CheckBox) findViewById(R.id.chkBxEasyLogin);
        chkBxToken = (CheckBox) findViewById(R.id.chkBxToken);
        chkBxInterbank = (CheckBox) findViewById(R.id.chkBxInterbank);
        chkBxPassEnable = (CheckBox) findViewById(R.id.chkBxPassEnable);
        btnSaveSettings = (Button) findViewById(R.id.btnSaveSettings);
        btnSaveSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                system_dbEngine.saveConfiguration(configuration2);
                ShowSaveOK();
            }
        });

        system_dbEngine = new System_DBEngine();
        system_dbEngine.Init(this);
        userDestails = system_dbEngine.getUserData();

        if (system_dbEngine.isFirstUse())
        {
            system_dbEngine.InitConfiguration();
        }

        configuration = system_dbEngine.getConfiguration();
        if (configuration.ALERT_SOUND_ENABLE == 1)
        {
            chkBxSound.setChecked(true);
        }
        if (configuration.HOME_ALERT_ENABLE == 1)
        {
            chkBxHomeAlert.setChecked(true);
        }
        if (configuration.IBANK_EASY_LOGIN_ENABLE == 1)
        {
            chkBxEasyLoginIbank.setChecked(true);
        }
        if (configuration.INTERBANK_ENABLE == 1)
        {
            chkBxInterbank.setChecked(true);
        }
        if (configuration.PROMPT_ENABLE == 1)
        {
            chkBxEnableAlert.setChecked(true);
        }
        if (configuration.STMT_DOWNLOAD_ENABLE == 1)
        {
            chkBxStmtDownload.setChecked(true);
        }
        if (configuration.SYSTEM_PASS_ENABLE == 1)
        {
            chkBxPassEnable.setChecked(true);
        }
        if (configuration.TOKEN_ENABLE == 1)
        {
            chkBxToken.setChecked(true);
        }
        if (configuration.SYSTEM_EASY_LOGIN == 1)
        {
            chkBxEasyLogin.setChecked(true);
        }


        //region checkboxes

        configuration2 = system_dbEngine.getConfiguration();
        chkBxEnableAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    configuration2.PROMPT_ENABLE = 1;
                }
                else
                {
                    configuration2.PROMPT_ENABLE = 0;
                }
            }
        });
        chkBxHomeAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    configuration2.HOME_ALERT_ENABLE = 1;
                }
                else
                {
                    configuration2.HOME_ALERT_ENABLE = 0;
                }
            }
        });
        chkBxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    configuration2.ALERT_SOUND_ENABLE = 1;
                }
                else
                {
                    configuration2.ALERT_SOUND_ENABLE = 0;
                }
            }
        });
        chkBxEasyLoginIbank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    configuration2.IBANK_EASY_LOGIN_ENABLE = 1;
                }
                else
                {
                    configuration2.IBANK_EASY_LOGIN_ENABLE = 0;
                }
            }
        });
        chkBxStmtDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    configuration2.STMT_DOWNLOAD_ENABLE = 1;
                }
                else
                {
                    configuration2.STMT_DOWNLOAD_ENABLE = 0;
                }
            }
        });
        chkBxEasyLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    configuration2.SYSTEM_EASY_LOGIN = 1;
                }
                else
                {
                    configuration2.SYSTEM_EASY_LOGIN = 0;
                }
            }
        });
        chkBxToken.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    configuration2.TOKEN_ENABLE = 1;
                }
                else
                {
                    configuration2.TOKEN_ENABLE = 0;
                }
            }
        });
        chkBxInterbank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    configuration2.INTERBANK_ENABLE = 1;
                }
                else
                {
                    configuration2.INTERBANK_ENABLE = 0;
                }
            }
        });
        chkBxPassEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    configuration2.SYSTEM_PASS_ENABLE = 1;
                }
                else
                {
                    configuration2.SYSTEM_PASS_ENABLE = 0;
                }
            }
        });
// endregion

        if (system_dbEngine.isFirstUse())
        {
            ShowSetupDialog();
        }

    }

    @Subscribe
    public void onSignupComplete(DeviceRegisterMessage message)
    {
        alertDialog.dismiss();
        try
        {
            switch (message.getRim())
            {
                case "GeneralError":
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            /*LayoutInflater inflater = (LayoutInflater)
                                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View customView = inflater.inflate(R.layout.smiley_layout_sad, null);*/

                            new MaterialStyledDialog(ActivationClass.this)
                                    .setTitle("OOPS!!!")
                                    .setDescription("Something went wrong. please try again later")
                                    .setIcon(getResources().getDrawable(R.drawable.sad))
                                    .setNegative("Back", new MaterialDialog.SingleButtonCallback()
                                    {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which)
                                        {
                                            onBackPressed();
                                        }
                                    })
                                    .setPositive("Retry", new MaterialDialog.SingleButtonCallback()
                                    {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which)
                                        {
                                            //// TODO: 3/16/2016 start new process
                                            alertDialog.show();
                                            startService(fetchersService);
                                        }
                                    })
                                    .withIconAnimation(true)
                                    .show();
                        }
                    });
                    break;
                case "NotSupportedError":
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            /*LayoutInflater inflater = (LayoutInflater)
                                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View customView = inflater.inflate(R.layout.smiley_layout_sad, null);
                            */
                            new MaterialStyledDialog(ActivationClass.this)
                                    .setTitle("This is interesting...")
                                    .setDescription("Looks like your device is not supported..." +
                                            "we can't continue")
                                    .setIcon(getResources().getDrawable(R.drawable.sad))
                                    .setNegative("Quit", new MaterialDialog.SingleButtonCallback()
                                    {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which)
                                        {
                                            ActivationClass.this.finish();
                                        }
                                    })
                                    .show();
                        }
                    });
                    break;
                case "DevIDNotFound":
                    try
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                new MaterialStyledDialog(ActivationClass.this)
                                        .setTitle("Error!!!")
                                        .setDescription("Check Network and try again")
                                        .setIcon(getResources().getDrawable(R.drawable.sad))
                                        .setNegative("Back", new MaterialDialog.SingleButtonCallback()
                                        {
                                            @Override
                                            public void onClick(MaterialDialog dialog, DialogAction which)
                                            {
                                                onBackPressed();
                                            }
                                        })
                                        .setPositive("Retry", new MaterialDialog.SingleButtonCallback()
                                        {
                                            @Override
                                            public void onClick(MaterialDialog dialog, DialogAction which)
                                            {
                                                //// TODO: 3/16/2016 start new process
                                                startService(fetchersService);
                                            }
                                        })
                                        .withIconAnimation(true)
                                        .show();
                            }
                        });
                    } catch (Exception ex)
                    {
                        ex.getMessage();
                    }
                    break;

                case "Registration Exist":
                    try
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                new MaterialStyledDialog(ActivationClass.this)
                                        .setTitle("Welcome Back...")
                                        .setDescription("This device has already been registered.")
                                        .setIcon(getResources().getDrawable(R.drawable.happy))
                                        .setPositive("Continue", new MaterialDialog.SingleButtonCallback()
                                        {
                                            @Override
                                            public void onClick(MaterialDialog dialog, DialogAction which)
                                            {
                                                //// TODO: 3/16/2016 start new process
                                                startActivity(new Intent(getApplicationContext(), MainHUb2.class));
                                            }
                                        })
                                        .withIconAnimation(true)
                                        .show();
                            }
                        });
                    } catch (Exception ex)
                    {
                        ex.getMessage();
                    }
                    break;

                case "registration_error":
                    try
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                new MaterialStyledDialog(ActivationClass.this)
                                        .setTitle("Error!!!")
                                        .setDescription("Something went wrong. please try again")
                                        .setIcon(getResources().getDrawable(R.drawable.sad))
                                        .setNegative("Back", new MaterialDialog.SingleButtonCallback()
                                        {
                                            @Override
                                            public void onClick(MaterialDialog dialog, DialogAction which)
                                            {
                                                onBackPressed();
                                            }
                                        })
                                        .setPositive("Retry", new MaterialDialog.SingleButtonCallback()
                                        {
                                            @Override
                                            public void onClick(MaterialDialog dialog, DialogAction which)
                                            {
                                                //// TODO: 3/16/2016 start new process
                                                alertDialog.show();
                                                startService(fetchersService);
                                            }
                                        })
                                        .withIconAnimation(true)
                                        .show();
                            }
                        });
                    } catch (Exception ex)
                    {
                        ex.getMessage();
                    }
                    break;

                case "success":
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            new MaterialStyledDialog(ActivationClass.this)
                                    .setTitle("Congratulations...")
                                    .setDescription("Activation Completed. Welcome to the" +
                                            " MiBank Family. Please set your preference and hit " +
                                            "the save button to continue.")
                                    .setIcon(getResources().getDrawable(R.drawable.happy))
                                    .setPositive("Continue", new MaterialDialog.SingleButtonCallback()
                                    {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which)
                                        {
                                            system_dbEngine.setNewUseFlag();
                                        }
                                    })
                                    .withIconAnimation(true)
                                    .show();
                        }
                    });
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    void ShowSetupDialog()
    {
        new MaterialStyledDialog(ActivationClass.this)
                .setTitle("Welcome " + userDestails.name)
                .setDescription("We will now proceed to finish the whole setup process. " +
                        "This might take a minute or two depending on your internet " +
                        "connection speed. So just relax wait...")
                .setIcon(getResources().getDrawable(R.drawable.happy))
                .setPositive("Continue", new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which)
                    {
                        alertDialog = new SpotsDialog(ActivationClass.this);
                        alertDialog.setTitle("Processing...");
                        alertDialog.setMessage("Processing...");
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        startService(fetchersService);
                    }
                })
                .withIconAnimation(true)
                .show();
    }

    void ShowSaveOK()
    {
        new MaterialStyledDialog(ActivationClass.this)
                .setTitle("Saved")
                .setDescription("Settings Saved Successfully")
                .setIcon(getResources().getDrawable(R.drawable.happy))
                .setPositive("Home", new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which)
                    {

                        startActivity(new Intent(ActivationClass.this, MainHUb2.class));

                    }
                })
                .setNegative("Continue", new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which)
                    {

                    }
                })
                .withIconAnimation(true)
                .show();
    }

    @Override
    public void onBackPressed()
    {
        if (!system_dbEngine.isFirstUse())
        {
            super.onBackPressed();
        }

    }
}
