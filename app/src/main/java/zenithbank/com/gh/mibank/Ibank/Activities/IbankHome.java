package zenithbank.com.gh.mibank.Ibank.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Ibank.Fragments.InterBankFrag;
import zenithbank.com.gh.mibank.Ibank.Fragments.InterFrag;
import zenithbank.com.gh.mibank.Ibank.Fragments.IntraFrag;
import zenithbank.com.gh.mibank.Ibank.Fragments.VisaFrag;
import zenithbank.com.gh.mibank.Ibank.System.BenefListIntentService;
import zenithbank.com.gh.mibank.Ibank.System.IbankDatabaseEngine;
import zenithbank.com.gh.mibank.Ibank.System.IbankServiceEngine;
import zenithbank.com.gh.mibank.Main.Activities.ActivationClass;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;
import zenithbank.com.gh.mibank.Notification.System.N_SystemService;
import zenithbank.com.gh.mibank.R;


public class IbankHome extends ActionBarActivity
{

    IbankDatabaseEngine ibankDatabaseEngine;
    EditText Access, user, pass;
    Button activate, login;
    ProgressDialog progressDialog;
    DrawerLayout mDrawerLayout;
    N_SystemService n_systemService;
    System_DBEngine system_dbEngine;

    String _access, _user, _pass;

    PowerManager pm;
    PowerManager.WakeLock wl;
    IbankServiceEngine ibankServiceEngine;
    ActionBar actionBar;
    ViewPager pager;

    IntraFrag intraFrag;
    InterFrag interFrag;
    VisaFrag visaFrag;
    InterBankFrag interBankFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ibankDatabaseEngine = new IbankDatabaseEngine();
        ibankDatabaseEngine.Init(this);
        ibankServiceEngine = new IbankServiceEngine();
        n_systemService = new N_SystemService();

        if (!ibankDatabaseEngine.isLoggedIn())
        {
            setContentView(R.layout.ibank_signon_layout);
            Access = (EditText) findViewById(R.id.edxtxAccesscode);
            user = (EditText) findViewById(R.id.edxtxUsername);
            pass = (EditText) findViewById(R.id.edxtxPassword);
            login = (Button) findViewById(R.id.btn_login);

            login.setOnClickListener(new View.OnClickListener()
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
                    progressDialog = ProgressDialog.show(IbankHome.this,
                            "Activating...", "Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    new doLogin().execute();
                }
            });
        }
        else
        {
            Load();
        }

    }

    void AlertError()
    {
        // Locate the TextView
        new AlertDialog.Builder(IbankHome.this)
                .setTitle("Error!!!")
                .setMessage("Please Check Credentials and try again")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        onBackPressed();
                    }
                }).show();
    }

    void Load()
    {
        setContentView(R.layout.ibankhome);
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.reed));
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        visaFrag = new VisaFrag();
        interFrag = new InterFrag();
        intraFrag = new IntraFrag();
        interBankFrag = new InterBankFrag();

        FragmentManager fragmentManager = getSupportFragmentManager();
        pager = (ViewPager) findViewById(R.id.HomePager);
        pager.setAdapter(new PagerAdapter1(fragmentManager));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        pager.setOffscreenPageLimit(4);

        //tabs
        final ActionBar.Tab InterTab = actionBar.newTab();
        InterTab.setIcon(getResources().getDrawable(R.drawable.ic_share_variant_white_18dp));
        InterTab.setTabListener(new ActionBar.TabListener()
        {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
            {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }
        });
        actionBar.addTab(InterTab);

        final ActionBar.Tab IntraTab = actionBar.newTab();
        IntraTab.setIcon(getResources().getDrawable(R.drawable.ic_chemical_weapon));
        IntraTab.setTabListener(new ActionBar.TabListener()
        {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
            {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }
        });
        actionBar.addTab(IntraTab);

        final ActionBar.Tab VisaTab = actionBar.newTab();
        VisaTab.setIcon(getResources().getDrawable(R.drawable.ic_credit_card_multiple));
        VisaTab.setTabListener(new ActionBar.TabListener()
        {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
            {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }
        });
        actionBar.addTab(VisaTab);

        final ActionBar.Tab InterbankTab = actionBar.newTab();
        InterbankTab.setIcon(getResources().getDrawable(R.drawable.ic_source_fork));
        InterbankTab.setTabListener(new ActionBar.TabListener()
        {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
            {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }
        });
        actionBar.addTab(InterbankTab);
       /* progressDialog = ProgressDialog.show(IbankHome.this,
                "fetching accounts...", "Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);*/
        //new fetchAccounts().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_hub2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:

                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                else
                {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.action_settings:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                else
                {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.action_config:
                startActivity(new Intent(this, ActivationClass.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
        else
        {
            super.onBackPressed();
        }
    }*/

    class PagerAdapter1 extends FragmentPagerAdapter
    {

        public PagerAdapter1(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {

            Fragment frag = null;
            try
            {
                switch (position)
                {
                    case 0:
                        frag = interFrag;
                        break;
                    case 1:
                        frag = intraFrag;
                        break;
                    case 2:
                        frag = visaFrag;
                        break;
                    case 3:
                        frag = interBankFrag;
                        break;

                }
            } catch (Exception ex)
            {
                ex.getMessage();
            }


            return frag;
        }

        @Override
        public int getCount()
        {
            return 4;
        }
    }

    class doLogin extends AsyncTask<String, String, String>
    {

        boolean done = false;

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                ArrayList AuthenticateResponse = ibankServiceEngine.SignUp(_access, _user, _pass);
                String res = AuthenticateResponse.get(1).toString().trim();
                String cookie = AuthenticateResponse.get(0).toString().trim();
                if (AuthenticateResponse.get(0).toString().trim().contains("error"))
                {
                    throw new Exception("LoginError");
                }
                else
                {

                    JSONObject rootObject = new JSONObject(res);
                    String profile = rootObject.getString("profile");
                    JSONObject profileObject = new JSONObject(profile);

                    //get rimINfo object
                    String riminfo = profileObject.getString("rimInfo");
                    JSONObject rimObject = new JSONObject(riminfo);

                    String mobiles = profileObject.getString("mobilenos");
                    String emails = profileObject.getString("emails");
                    String accounts = profileObject.getString("accounts");

                    //get accounts object
                    if (!accounts.isEmpty())
                    {
                        ibankDatabaseEngine.DeleteAccountsTable();
                        if (profileObject.get("accounts") instanceof JSONArray)
                        {
                            JSONArray accountsArray = new JSONArray(accounts);
                            int count = accountsArray.length();
                            for (int x = 0; x < count; x++)
                            {
                                JSONObject ac_Ob = accountsArray.getJSONObject(x);
                                ibankDatabaseEngine.updateAccountsTable(
                                        ac_Ob.getString("acctType"),
                                        ac_Ob.getString("acctNo"),
                                        ac_Ob.getString("acctDesc"),
                                        ac_Ob.getString("isoCurrency")
                                );
                            }
                        }
                        else
                        {
                            JSONObject accountsObject = new JSONObject(accounts);
                            ibankDatabaseEngine.updateAccountsTable(
                                    accountsObject.getString("acctType"),
                                    accountsObject.getString("acctNo"),
                                    accountsObject.getString("acctDesc"),
                                    accountsObject.getString("isoCurrency")
                            );
                        }
                    }

                    //get card accounts
                    String card_accounts = profileObject.getString("cardAccounts");
                    if (!card_accounts.isEmpty())
                    {
                        ibankDatabaseEngine.DeleteCardAccountsTable();
                        if (profileObject.get("cardAccounts") instanceof JSONArray)
                        {
                            JSONArray card_accountsArray = new JSONArray(card_accounts);
                            int count = card_accountsArray.length();
                            for (int x = 0; x < count; x++)
                            {
                                JSONObject ac_Ob = card_accountsArray.getJSONObject(x);
                                ibankDatabaseEngine.updateCardAccountsTable(
                                        ac_Ob.getString("acctType"),
                                        ac_Ob.getString("acctNo"),
                                        ac_Ob.getString("acctDesc"),
                                        ac_Ob.getString("isoCurrency")
                                );
                            }
                        }
                        else
                        {
                            JSONObject card_accountsObject = new JSONObject(card_accounts);
                            ibankDatabaseEngine.updateCardAccountsTable(
                                    card_accountsObject.getString("acctType"),
                                    card_accountsObject.getString("acctNo"),
                                    card_accountsObject.getString("acctDesc"),
                                    card_accountsObject.getString("isoCurrency")
                            );
                        }
                    }
                    ibankDatabaseEngine.setIsLoggedIN();
                    ibankDatabaseEngine.saveCookie(cookie);
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
            progressDialog.dismiss();
            wl.release();
            if (done)
            {
                //onBackPressed();
                Intent fetchersService = new Intent(getApplicationContext(),
                        BenefListIntentService.class);
                startService(fetchersService);
                Load();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error!!..Check Credentials",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
