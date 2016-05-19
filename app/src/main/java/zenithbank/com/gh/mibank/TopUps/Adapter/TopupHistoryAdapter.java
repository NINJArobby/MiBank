package zenithbank.com.gh.mibank.TopUps.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import zenithbank.com.gh.mibank.R;
import zenithbank.com.gh.mibank.TopUps.Clasees.T_HistoryClass;

/**
 * Created by Robby on 4/9/2016.
 */
public class TopupHistoryAdapter extends BaseAdapter
{

    private static LayoutInflater inflater = null;
    ArrayList<T_HistoryClass.transaction> data;
    private Activity activity;

    public TopupHistoryAdapter(Activity a, ArrayList<T_HistoryClass.transaction> _data)
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
        T_HistoryClass.transaction dd = data.get(position);
        try
        {
            view = inflater.inflate(R.layout.topup_row, parent, false);
            TextView name = (TextView) view.findViewById(R.id.txtName);
            TextView number = (TextView) view.findViewById(R.id.txtNumber);
            TextView txtType = (TextView) view.findViewById(R.id.txtType);
            TextView txtDate = (TextView) view.findViewById(R.id.txtDate);
            ImageView stat = (ImageView) view.findViewById(R.id.ImgStatus);
            TextView txtAmount = (TextView) view.findViewById(R.id.txtAmount);

            name.setText(dd.name);
            number.setText(dd.number);
            txtType.setText(dd.network);
            txtDate.setText(dd.trsxDate);
            txtAmount.setText(dd.amount);

            if (dd.status.equals("SUCCESS"))
            {
                stat.setImageDrawable(activity.getResources().getDrawable(R.drawable.plus));
            }
            else
            {
                stat.setImageDrawable(activity.getResources().getDrawable(R.drawable.wrong));
            }

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }
}
