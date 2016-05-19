package zenithbank.com.gh.mibank.Main.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import zenithbank.com.gh.mibank.Main.Classes.CurrencyClass;
import zenithbank.com.gh.mibank.R;


public class RatesAdapter extends BaseAdapter
{

    private static LayoutInflater inflater = null;
    ArrayList<CurrencyClass.RootObject> data;
    Random rand = new Random();
    private Activity activity;


    public RatesAdapter(Activity a, ArrayList<CurrencyClass.RootObject> _data)
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
        CurrencyClass.RootObject sdata = data.get(position);
        try
        {
            view = inflater.inflate(R.layout.crncy_row, parent, false);
            TextView Atext = (TextView) view.findViewById(R.id.txtAmt);
            TextView Rtext = (TextView) view.findViewById(R.id.txtRate);
            Float _amt = Float.parseFloat(sdata.rate);
            Rtext.setText(sdata.crncyName);
            Atext.setText(String.valueOf(_amt));
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }
}
