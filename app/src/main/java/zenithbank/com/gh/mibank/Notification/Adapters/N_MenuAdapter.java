package zenithbank.com.gh.mibank.Notification.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import zenithbank.com.gh.mibank.R;

/**
 * Created by Robby on 3/11/2016.
 */
public class N_MenuAdapter extends BaseAdapter
{

    private static LayoutInflater inflater = null;
    ArrayList<String> data;
    Random rand = new Random();
    private Activity activity;


    public N_MenuAdapter(Activity a, ArrayList<String> d)
    {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        try
        {
            view = inflater.inflate(R.layout.menu_row, parent, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.mLogo);
            TextView textView = (TextView) view.findViewById(R.id.mTitle);
            switch (position)
            {
                case 0:
                    imageView.setImageDrawable(activity.getResources().
                            getDrawable(R.drawable.ic_account_search));
                    textView.setText(data.get(position));
                    break;
                case 1:
                    imageView.setImageDrawable(activity.getResources().
                            getDrawable(R.drawable.ic_home_variant));
                    textView.setText(data.get(position));
                    break;
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }
}
