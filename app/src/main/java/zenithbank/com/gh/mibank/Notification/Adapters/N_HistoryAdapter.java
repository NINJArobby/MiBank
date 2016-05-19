package zenithbank.com.gh.mibank.Notification.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Notification.Classes.N_HistoryClass;
import zenithbank.com.gh.mibank.R;


public class N_HistoryAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    ArrayList<N_HistoryClass.RootObject> data;
    private Activity activity;

    public N_HistoryAdapter(Activity a, ArrayList<N_HistoryClass.RootObject> _data)
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
        N_HistoryClass.RootObject dd = data.get(position);
        View view = convertView;
        try
        {
            view = inflater.inflate(R.layout.bal_row, parent, false);
            TextView desc = (TextView) view.findViewById(R.id.txtListDesc);
            TextView amt = (TextView) view.findViewById(R.id.txtListAmount);
            TextView acct = (TextView) view.findViewById(R.id.txtListAccount);
            //TextView crncy = (TextView) view.findViewById(R.id.txtCrncy);
            TextView date = (TextView) view.findViewById(R.id.txtListDate);
            TextView type = (TextView) view.findViewById(R.id.txtListType);

            desc.setText(dd.trsxDecsription);
            Float _amt = Float.parseFloat(dd.trsxAmount);
            amt.setText(dd.trsxCurrency + " " + String.valueOf(_amt));
            //crncy.setText(dd.trsxCurrency);
            acct.setText(dd.trsxAcctno);
            date.setText(dd.trsxDate + "  " + dd.trsxBranch);

            switch (dd.trsxType)
            {
                case "Credit":
                    type.setText("CR");
                    type.setTextColor(R.color.red);
                    break;
                case "Debit":
                    type.setText("DR");
                    type.setTextColor(R.color.blue);
                    break;
            }


        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }
}
