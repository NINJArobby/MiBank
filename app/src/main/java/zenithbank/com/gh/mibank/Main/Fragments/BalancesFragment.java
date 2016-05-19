package zenithbank.com.gh.mibank.Main.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import zenithbank.com.gh.mibank.Main.Adapters.BalanceAdapter;
import zenithbank.com.gh.mibank.Main.Classes.BalanceMessage;
import zenithbank.com.gh.mibank.Main.System.BusStation;
import zenithbank.com.gh.mibank.Main.System.MiBankBalanceUpdater;
import zenithbank.com.gh.mibank.Main.System.MibankServiceEngine;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;

import static zenithbank.com.gh.mibank.R.drawable;
import static zenithbank.com.gh.mibank.R.id;
import static zenithbank.com.gh.mibank.R.layout;

public class BalancesFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView balList;
    LinearLayout anchor;
    MibankServiceEngine mibankServiceEngine;
    System_DBEngine system_dbEngine;
    AlertDialog alertDialog;


    public BalancesFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BalancesFragment newInstance(String param1, String param2)
    {
        BalancesFragment fragment = new BalancesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = null;
        mibankServiceEngine = new MibankServiceEngine();
        system_dbEngine = new System_DBEngine();
        system_dbEngine.Init(getActivity());
        BusStation.getBus().register(this);
        try
        {
            v = inflater.inflate(layout.fragment_balances, container, false);
            balList = (ListView) v.findViewById(id.lstBalance);
            anchor = (LinearLayout) v.findViewById(id.anchorBal);
            Intent fetchersService = new Intent(getActivity(), MiBankBalanceUpdater.class);
            getActivity().startService(fetchersService);

        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return v;
    }


    @Subscribe
    public void OnBalancesRecived(final BalanceMessage rdata)
    {
        try
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    anchor.setBackgroundDrawable(getResources().getDrawable(drawable.reed));
                    BalanceAdapter adapter = new BalanceAdapter(getActivity(), rdata.getBalances());
                    balList.setAdapter(adapter);
                }
            });
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }


}
