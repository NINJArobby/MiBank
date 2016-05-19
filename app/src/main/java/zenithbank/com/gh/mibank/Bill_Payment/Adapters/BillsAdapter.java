package zenithbank.com.gh.mibank.Bill_Payment.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.Bill_Payment.Classes.BillsClass;
import zenithbank.com.gh.mibank.R;

/**
 * Created by Robby on 5/4/2016.
 */
public class BillsAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    ArrayList<BillsClass> data;
    private Activity activity;

    public BillsAdapter(Activity a, ArrayList<BillsClass> _data)
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
        View view = convertView;
        BillsClass bclass = data.get(position);
        try
        {
            view = inflater.inflate(R.layout.bills_row, parent, false);
            ImageView logo = (ImageView) view.findViewById(R.id.imgLogo);
            TextView name = (TextView) view.findViewById(R.id.txtBillName);
            TextView number = (TextView) view.findViewById(R.id.txtBillAccount);
            TextView amount = (TextView) view.findViewById(R.id.TXTAmount);

            name.setText(bclass.billName);
            number.setText(bclass.billNumber);
            amount.setText(bclass.billAmount);
            switch (bclass.billType)
            {
                case "DSTV":
                    logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.dstv));
                    break;
                case "GOTV":
                    logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.gotv));
                    break;
                case "GOTV_LITE":
                    logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.gotv));
                    break;
                case "SURFLINE":
                    logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.surfline));
                    break;
                case "ECG_POSTPAID":
                    logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.ecg));
                default:

            }

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }
}
