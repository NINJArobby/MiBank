package zenithbank.com.gh.mibank.Notification.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Notification.Adapters.N_MenuAdapter;
import zenithbank.com.gh.mibank.Notification.Classes.N_MenuMessage;
import zenithbank.com.gh.mibank.Notification.System.N_BusStation;
import zenithbank.com.gh.mibank.R;


public class NotifyNavigationDrawer extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;
    N_MenuAdapter menuAdapter;
    LinearLayout exitLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotifyNavigationDrawer()
    {
        // Required empty public constructor
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
            view = inflater.inflate(R.layout.fragment_notify_navigation_drawer, container, false);
            listView = (ListView) view.findViewById(R.id.ListMenu);
            exitLayout = (LinearLayout) view.findViewById(R.id.exitLayout);
            ArrayList<String> list = new ArrayList<>();
            list.add("Transaction Search");
            list.add("Prompts Home");


            menuAdapter = new N_MenuAdapter(getActivity(), list);
            listView.setAdapter(menuAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    switch (position)
                    {
                        case 0:
                            N_BusStation.getBus().post(new N_MenuMessage("search_zenith"));
                            break;
                        case 1:
                            N_BusStation.getBus().post(new N_MenuMessage("search_local"));
                            break;
                    }
                }
            });

            exitLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getActivity().onBackPressed();
                }
            });


        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }


}
