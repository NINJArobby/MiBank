package zenithbank.com.gh.mibank.Main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import zenithbank.com.gh.mibank.Main.Adapters.InvestRateAdapter;
import zenithbank.com.gh.mibank.Main.Classes.InvestRatesMessage;
import zenithbank.com.gh.mibank.Main.System.BusStation;
import zenithbank.com.gh.mibank.Main.System.MiBankInvestmentRatesUpdater;
import zenithbank.com.gh.mibank.R;

public class InterestRatesFragment extends Fragment
{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView lstRates;
    LinearLayout anchor;
    private String mParam1;
    private String mParam2;

    public InterestRatesFragment()
    {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static InterestRatesFragment newInstance(String param1, String param2)
    {
        InterestRatesFragment fragment = new InterestRatesFragment();
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
        BusStation.getBus().register(this);
        try
        {
            v = inflater.inflate(R.layout.fragment_interest_rates, container, false);
            lstRates = (ListView) v.findViewById(R.id.lstRates);
            anchor = (LinearLayout) v.findViewById(R.id.anchorBal);
            Intent fetchersService = new Intent(getActivity(), MiBankInvestmentRatesUpdater.class);
            getActivity().startService(fetchersService);


        } catch (Exception ex)
        {
            ex.getMessage();
        }
        // Inflate the layout for this fragment
        return v;
    }

    @Subscribe
    public void OnInvestmentRatesRecived(final InvestRatesMessage rdata)
    {
        try
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    anchor.setBackgroundDrawable(getResources().getDrawable(R.drawable.reed));
                    InvestRateAdapter adapter = new InvestRateAdapter(getActivity(), rdata.getRates());
                    lstRates.setAdapter(adapter);
                }
            });
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }

}
