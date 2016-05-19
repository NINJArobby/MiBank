package zenithbank.com.gh.mibank.Main.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Main.Classes.HistoryClass;
import zenithbank.com.gh.mibank.R;


public class HistoryAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    ArrayList<HistoryClass.RootObject> data;
    private Activity activity;

    public HistoryAdapter(Activity a, ArrayList<HistoryClass.RootObject> _data)
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
        HistoryClass.RootObject dd = data.get(position);
        View view = null;
        try
        {
            view = inflater.inflate(R.layout.historys_row, parent, false);
            TextView desc = (TextView) view.findViewById(R.id.txtDesc);
            TextView amt = (TextView) view.findViewById(R.id.txtAmnt);
            TextView crncy = (TextView) view.findViewById(R.id.txtCrncy);
            desc.setText(dd.trsxDecsription);
            Float _amt = Float.parseFloat(dd.trsxAmount);
            amt.setText(String.valueOf(_amt));
            crncy.setText(dd.trsxCurrency);

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }
}
