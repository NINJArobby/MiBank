package zenithbank.com.gh.mibank.Main.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Main.Classes.BalanceClass;
import zenithbank.com.gh.mibank.R;


public class BalanceAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    ArrayList<BalanceClass.RootObject> data;
    private Activity activity;

    public BalanceAdapter(Activity a, ArrayList<BalanceClass.RootObject> _data)
    {
        activity = a;
        data = _data;
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
        View view = null;
        BalanceClass.RootObject data1 = data.get(position);
        try
        {
            view = inflater.inflate(R.layout.balance_row, parent, false);
            TextView acctNo = (TextView) view.findViewById(R.id.txtAcct);
            TextView amt = (TextView) view.findViewById(R.id.txtBall);
            acctNo.setText(data1.acctNo);
            Float _amt = Float.parseFloat(data1.balance);
            amt.setText(String.valueOf(_amt));
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }
}
