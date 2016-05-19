package zenithbank.com.gh.mibank.Notification.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Notification.Adapters.N_HistoryAdapter;
import zenithbank.com.gh.mibank.Notification.Classes.N_HistoryClass;
import zenithbank.com.gh.mibank.Notification.System.N_SystemDBEngine;
import zenithbank.com.gh.mibank.R;


public class NotificationHomeFrag extends Fragment
{

    ListView listView;
    N_SystemDBEngine n_systemDBEngine;
    ArrayList<N_HistoryClass.RootObject> data;

    public NotificationHomeFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = container;
        n_systemDBEngine = new N_SystemDBEngine();
        n_systemDBEngine.Init(getActivity());
        data = n_systemDBEngine.getAllNotificationData();
        N_HistoryAdapter adapter = new N_HistoryAdapter(getActivity(), data);

        try
        {
            view = inflater.inflate(R.layout.fragment_notification_home, container, false);
            listView = (ListView) view.findViewById(R.id.lsNotifyHome);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    LayoutInflater inflater = (LayoutInflater) getActivity()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = inflater.inflate(R.layout.n_preview_layout, null);
                    TextView DescText = (TextView) customView.findViewById(R.id.txtDesc);
                    TextView AmtText = (TextView) customView.findViewById(R.id.txtAmount);
                    TextView DateText = (TextView) customView.findViewById(R.id.txtDate);

                    TextView nDescText = (TextView) view.findViewById(R.id.txtListDesc);
                    TextView nAmtText = (TextView) view.findViewById(R.id.txtListAmount);
                    TextView nDateText = (TextView) view.findViewById(R.id.txtListDate);

                    DescText.setText(nDescText.getText().toString().trim());
                    AmtText.setText(nAmtText.getText().toString().trim());
                    DateText.setText(nDateText.getText().toString().trim());

                    new MaterialStyledDialog(getActivity())
                            .setCustomView(customView)
                            .setNegative("Close", new MaterialDialog.SingleButtonCallback()
                            {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which)
                                {

                                }
                            })
                            .show();
                }
            });

            listView.setAdapter(adapter);

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }


}
