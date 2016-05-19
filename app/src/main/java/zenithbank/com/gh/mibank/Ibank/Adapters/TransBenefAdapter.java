package zenithbank.com.gh.mibank.Ibank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import zenithbank.com.gh.mibank.Ibank.Classes.TransferBeneficiaryClass;
import zenithbank.com.gh.mibank.R;

/**
 * Created by Robby on 4/5/2016.
 */
public class TransBenefAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    ArrayList<TransferBeneficiaryClass.RootObject> data;
    Random rand = new Random();
    TextView name, acct, bank, phone, id, mail, txtFName;
    private Activity activity;


    public TransBenefAdapter(Activity a, ArrayList<TransferBeneficiaryClass.RootObject> d)
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
        TransferBeneficiaryClass.RootObject dd = data.get(position);
        try
        {
            view = inflater.inflate(R.layout.bene_layout, parent, false);
            name = (TextView) view.findViewById(R.id.txtBenefName);
            acct = (TextView) view.findViewById(R.id.txtBenefAcct);
            bank = (TextView) view.findViewById(R.id.txtBeneBank);
            phone = (TextView) view.findViewById(R.id.txtBenefContact);
            id = (TextView) view.findViewById(R.id.txtTrsfID);
            mail = (TextView) view.findViewById(R.id.txtBenefmail);
            txtFName = (TextView) view.findViewById(R.id.txtFName);

            name.setText(dd.transferBeneficiary.field1);
            acct.setText(dd.transferBeneficiary.field2);
            bank.setText(dd.transferBeneficiary.field3);
            phone.setText(dd.transferBeneficiary.field4);
            id.setText(String.valueOf(dd.transferBeneficiary.id));
            try
            {
                mail.setText(dd.transferBeneficiary.field5);
            } catch (Exception ex)
            {
                ex.getMessage();
            }

            txtFName.setText(String.valueOf(dd.transferBeneficiary.field1.charAt(0)));
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            txtFName.setTextColor(color);

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }

    private int getBankname(String field3)
    {
        return 0;
    }
}
