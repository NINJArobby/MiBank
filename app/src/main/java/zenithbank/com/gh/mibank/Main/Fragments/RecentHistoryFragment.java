package zenithbank.com.gh.mibank.Main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.squareup.otto.Subscribe;

import zenithbank.com.gh.mibank.Main.Adapters.HistoryAdapter;
import zenithbank.com.gh.mibank.Main.Classes.HistoryMessage;
import zenithbank.com.gh.mibank.Main.System.BusStation;
import zenithbank.com.gh.mibank.Main.System.MiBankHistoryUpdater;
import zenithbank.com.gh.mibank.Main.System.MibankServiceEngine;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;
import zenithbank.com.gh.mibank.R;

public class RecentHistoryFragment extends Fragment
{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MibankServiceEngine mibankServiceEngine;
    System_DBEngine system_dbEngine;
    ListView balList;
    LinearLayout anchor;
    private String mParam1;
    private String mParam2;

    public RecentHistoryFragment()
    {
        // Required empty public constructor
    }

    public static RecentHistoryFragment newInstance(String param1, String param2)
    {
        RecentHistoryFragment fragment = new RecentHistoryFragment();
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
        View view = null;
        try
        {
            view = inflater.inflate(R.layout.fragment_recent_history, container, false);
            mibankServiceEngine = new MibankServiceEngine();
            system_dbEngine = new System_DBEngine();
            system_dbEngine.Init(getActivity());
            BusStation.getBus().register(this);

            balList = (ListView) view.findViewById(R.id.lstHistory);
            balList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    TextView desc = (TextView) view.findViewById(R.id.txtDesc);
                    TextView amt = (TextView) view.findViewById(R.id.txtAmnt);
                    TextView crncy = (TextView) view.findViewById(R.id.txtCrncy);
                    AlertTrans(desc.getText().toString().trim(),
                            amt.getText().toString().trim(),
                            crncy.getText().toString().trim());
                }
            });
            anchor = (LinearLayout) view.findViewById(R.id.anchorBal);
            Intent fetchersService = new Intent(getActivity(), MiBankHistoryUpdater.class);
            getActivity().startService(fetchersService);
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }

    @Subscribe
    public void OnHistoryRecived(final HistoryMessage rdata)
    {
        try
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    anchor.setBackgroundDrawable(getResources().getDrawable(R.drawable.reed));
                    HistoryAdapter adapter = new HistoryAdapter(getActivity(), rdata.getBalances());
                    balList.setAdapter(adapter);
                }
            });
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }

    void AlertTrans(String desc, String amount, String crncy)
    {
        new MaterialStyledDialog(getActivity())
                .setTitle(crncy + " " + amount)
                .setDescription(desc)
                .setPositive("OK", new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which)
                    {

                    }
                })
                .show();
    }
}
