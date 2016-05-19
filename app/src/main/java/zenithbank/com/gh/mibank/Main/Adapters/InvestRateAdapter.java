package zenithbank.com.gh.mibank.Main.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Main.Classes.InvestRatesClass;
import zenithbank.com.gh.mibank.R;

/**
 * Created by Robby on 3/10/2016.
 */
public class InvestRateAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    ArrayList<InvestRatesClass.RootObject> data;
    private Activity activity;


    public InvestRateAdapter(Activity a, ArrayList<InvestRatesClass.RootObject> _data)
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
        InvestRatesClass.RootObject sdata = data.get(position);
        try
        {
            view = inflater.inflate(R.layout.crncy_row, parent, false);
            TextView Atext = (TextView) view.findViewById(R.id.txtAmt);
            TextView Rtext = (TextView) view.findViewById(R.id.txtRate);
            Rtext.setText(sdata.name);
            Float _amt = Float.parseFloat(sdata.rate);
            Atext.setText(String.valueOf(_amt));

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }
}
