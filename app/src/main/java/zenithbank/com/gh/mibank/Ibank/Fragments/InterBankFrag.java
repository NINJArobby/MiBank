package zenithbank.com.gh.mibank.Ibank.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.otto.Subscribe;

import zenithbank.com.gh.mibank.Ibank.Activities.AddBeneficiary;
import zenithbank.com.gh.mibank.Ibank.Adapters.TransBenefAdapter;
import zenithbank.com.gh.mibank.Ibank.Classes.I_BenefListErrorMessage;
import zenithbank.com.gh.mibank.Ibank.Classes.I_BenefListMessage;
import zenithbank.com.gh.mibank.Ibank.System.I_BusStation;
import zenithbank.com.gh.mibank.Ibank.System.IbankDatabaseEngine;
import zenithbank.com.gh.mibank.R;


public class InterBankFrag extends Fragment
{

    ListView listView;
    FloatingActionButton fab;
    TextView txtExplain;
    IbankDatabaseEngine ibankDatabaseEngine;


    public InterBankFrag()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = container;
        try
        {
            I_BusStation.getBus().register(this);
            view = inflater.inflate(R.layout.fragment_inter_bank, container, false);
            listView = (ListView) view.findViewById(R.id.ls_beneList);
            fab = (FloatingActionButton) view.findViewById(R.id.Transfab);
            txtExplain = (TextView) view.findViewById(R.id.txtExplain);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    TextView name = (TextView) view.findViewById(R.id.txtBenefName);
                    TextView acct = (TextView) view.findViewById(R.id.txtBenefAcct);
                    TextView bank = (TextView) view.findViewById(R.id.txtBeneBank);
                    TextView phone = (TextView) view.findViewById(R.id.txtBenefContact);
                    TextView _id = (TextView) view.findViewById(R.id.txtTrsfID);
                    TextView mail = (TextView) view.findViewById(R.id.txtBenefmail);
                }
            });
            ibankDatabaseEngine = new IbankDatabaseEngine();
            ibankDatabaseEngine.Init(getActivity());
            if (ibankDatabaseEngine.isLoggedIn())
            {
                TransBenefAdapter adapter =
                        new TransBenefAdapter(getActivity(), ibankDatabaseEngine.getBeneficiaries());
                listView.setAdapter(adapter);
                txtExplain.setText("Showing list of current beneficiaries. Tap to transfer " +
                        "or tap '+' to add new");
            }

            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    startActivity(new Intent(getActivity(), AddBeneficiary.class));

                }
            });
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }

    @Subscribe
    public void onBenefListRecieved(final I_BenefListMessage message)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                TransBenefAdapter adapter =
                        new TransBenefAdapter(getActivity(), message.getBalances());
                listView.setAdapter(adapter);
                txtExplain.setText("Showing list of current beneficiaries. Tap to transfer " +
                        "or tap '+' to add new");
            }
        });
    }

    @Subscribe
    public void onBenefErrorReceived(I_BenefListErrorMessage error)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                txtExplain.setText("No Beneficiary setup. tap the button to add beneficiary");
            }
        });
    }

}
