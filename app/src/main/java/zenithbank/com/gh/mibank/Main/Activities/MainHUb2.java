package zenithbank.com.gh.mibank.Main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import zenithbank.com.gh.mibank.Main.Fragments.BalancesFragment;
import zenithbank.com.gh.mibank.Main.Fragments.InterestRatesFragment;
import zenithbank.com.gh.mibank.Main.Fragments.RatesFragment;
import zenithbank.com.gh.mibank.Main.Fragments.RecentHistoryFragment;
import zenithbank.com.gh.mibank.R;


public class MainHUb2 extends ActionBarActivity
{
    ActionBar actionBar;
    ViewPager pager;

    BalancesFragment balFRag;
    RecentHistoryFragment historyFragment;
    RatesFragment ratesFragment;
    InterestRatesFragment interestRatesFragment;

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.main_hub2);
            actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.reed));
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            pager.setOffscreenPageLimit(3);

            balFRag = new BalancesFragment();
            // historyFragment = new RecentHistoryFragment();
            ratesFragment = new RatesFragment();
            interestRatesFragment = new InterestRatesFragment();

            final ActionBar.Tab InvestTab = actionBar.newTab();
            // InvestTab.setText("Investment");
            InvestTab.setIcon(getResources().getDrawable(R.drawable.ic_cash_multiple));
            InvestTab.setTabListener(new ActionBar.TabListener()
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
            actionBar.addTab(InvestTab);

            ActionBar.Tab ratesTab = actionBar.newTab();
            // ratesTab.setText("Exchange");
            ratesTab.setIcon(getResources().getDrawable(R.drawable.ic_scale_balance));
            ratesTab.setTabListener(new ActionBar.TabListener()
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
            actionBar.addTab(ratesTab);

            ActionBar.Tab BalanceTab = actionBar.newTab();
            //BalanceTab.setText("Balances");
            BalanceTab.setIcon(getResources().getDrawable(R.drawable.ic_wallet));
            BalanceTab.setTabListener(new ActionBar.TabListener()
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
            actionBar.addTab(BalanceTab);

        } catch (Exception ex)
        {
            ex.getMessage();
        }
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
            case R.id.action_about:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
                        frag = ratesFragment;
                        break;
                    case 1:
                        frag = balFRag;
                        break;
                    case 2:
                        frag = interestRatesFragment;
                        break;
                    /*case 3:
                        frag = historyFragment;
                        //break;*/
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
            return 3;
        }
    }
}
