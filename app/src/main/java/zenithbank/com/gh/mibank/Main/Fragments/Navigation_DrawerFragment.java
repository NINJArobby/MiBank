package zenithbank.com.gh.mibank.Main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Bill_Payment.Activities.BillPayHome;
import zenithbank.com.gh.mibank.Ibank.Activities.IbankHome;
import zenithbank.com.gh.mibank.Main.Adapters.MenuAdapter;
import zenithbank.com.gh.mibank.Notification.Main.NotifyHome;
import zenithbank.com.gh.mibank.R;
import zenithbank.com.gh.mibank.TopUps.T_Main.TopupsHome;
import zenithbank.com.gh.mibank.TouchTransfer.Activities.TouchTransHome;


public class Navigation_DrawerFragment extends Fragment
{


    public static final String PREF_FILE_NAME = "testPref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    public static final String KEY_TITLE1 = "song"; // parent node
    public static final String KEY_NUMBER = "id";
    MenuAdapter menuAdapter;
    ListView listView;
    LinearLayout exitLayout;

    public Navigation_DrawerFragment()
    {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        try
        {
            listView = (ListView) v.findViewById(R.id.ListMenu1);
            ArrayList<String> list = new ArrayList<>();
            list.add("Notifications");
            list.add("iBank mini");
            list.add("Mobile TopUps");
            list.add("Touch Trans");
            list.add("Bill Payment");
            //
            //list.add("Card Requests");
            //list.add("iCheque");*/

            menuAdapter = new MenuAdapter(getActivity(), list);
            listView.setAdapter(menuAdapter);

            exitLayout = (LinearLayout) v.findViewById(R.id.exitLayout);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    switch (position)
                    {
                        case 0:
                            startActivity(new Intent(getActivity(), NotifyHome.class));
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), IbankHome.class));
                            break;
                        case 2:
                            startActivity(new Intent(getActivity(), TopupsHome.class));
                            break;
                        case 3:
                            startActivity(new Intent(getActivity(), TouchTransHome.class));
                            break;
                        case 4:
                            startActivity(new Intent(getActivity(), BillPayHome.class));
                        default:
                    }
                }
            });

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return v;
    }
}
