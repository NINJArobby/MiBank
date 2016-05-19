package zenithbank.com.gh.mibank.Main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Main.Adapters.RatesAdapter;
import zenithbank.com.gh.mibank.Main.Classes.CurrencyClass;
import zenithbank.com.gh.mibank.Main.Classes.RatesMessage;
import zenithbank.com.gh.mibank.Main.System.BusStation;
import zenithbank.com.gh.mibank.Main.System.MiBankRatesUpdater;
import zenithbank.com.gh.mibank.R;


public class RatesFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressBar progressBar;
    LinearLayout anchor;
    ListView crncyList;
    private String mParam1;
    private String mParam2;

    public RatesFragment()
    {
        // Required empty public constructor
    }

    public static RatesFragment newInstance(String param1, String param2)
    {
        RatesFragment fragment = new RatesFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = null;
        try
        {
            v = inflater.inflate(R.layout.fragment_rates, container, false);
            anchor = (LinearLayout) v.findViewById(R.id.anchorBal);
            crncyList = (ListView) v.findViewById(R.id.crncyList);
            BusStation.getBus().register(this);
            Intent fetchersService = new Intent(getActivity(), MiBankRatesUpdater.class);
            getActivity().startService(fetchersService);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return v;
    }

    @Subscribe
    public void RatesReceived(final RatesMessage rdata)
    {
        ArrayList<CurrencyClass.RootObject> sdata = new ArrayList<>();

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                anchor.setBackgroundDrawable(getResources().getDrawable(R.drawable.reed));
                final RatesAdapter adapter1 = new RatesAdapter(getActivity(), rdata.getCurrencies());
                crncyList.setAdapter(adapter1);
            }
        });

    }
}
