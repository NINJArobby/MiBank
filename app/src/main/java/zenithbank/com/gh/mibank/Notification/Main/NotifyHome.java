package zenithbank.com.gh.mibank.Notification.Main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.squareup.otto.Subscribe;

import zenithbank.com.gh.mibank.Notification.Classes.N_MenuMessage;
import zenithbank.com.gh.mibank.Notification.Fragments.NotificationHomeFrag;
import zenithbank.com.gh.mibank.Notification.Fragments.NotificationSearchFrag;
import zenithbank.com.gh.mibank.Notification.System.N_BusStation;
import zenithbank.com.gh.mibank.R;


public class NotifyHome extends ActionBarActivity
{
    DrawerLayout mDrawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.notify_home);
            toolbar = (Toolbar) findViewById(R.id.main_appBar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Prompt Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                    }
                    else
                    {
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                    }


                }
            });

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            N_BusStation.getBus().register(this);
            NotificationHomeFrag home = new NotificationHomeFrag();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.anchor, home).commit();
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    @Subscribe
    public void onMenuSelected(N_MenuMessage message)
    {
        if (message.getMessage().equals("search_zenith"))
        {
            //load new search activity
            NotificationSearchFrag SFrag = new NotificationSearchFrag();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.anchor, SFrag)
                    .addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            toolbar.setTitle("Z-Search");
        }
        else if (message.getMessage().equals("search_local"))
        {
            NotificationHomeFrag home = new NotificationHomeFrag();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.anchor, home)
                    .addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            toolbar.setTitle("Prompt Home");
        }
    }

}
