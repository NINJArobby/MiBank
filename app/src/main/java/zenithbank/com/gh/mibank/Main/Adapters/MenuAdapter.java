package zenithbank.com.gh.mibank.Main.Adapters;

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


public class MenuAdapter extends BaseAdapter
{


    private static LayoutInflater inflater = null;
    ArrayList<String> data;
    Random rand = new Random();
    private Activity activity;


    public MenuAdapter(Activity a, ArrayList<String> d)
    {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return data.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        try
        {
            if (convertView == null)
            {
                vi = inflater.inflate(R.layout.menu_row, parent, false);
                ImageView imageView = (ImageView) vi.findViewById(R.id.mLogo);
                TextView textView = (TextView) vi.findViewById(R.id.mTitle);
                switch (position)
                {
                    case 0:

                        imageView.setImageDrawable(activity.getResources()
                                .getDrawable(R.mipmap.ic_notifications_white_18dp));

                        textView.setText(data.get(position));
                        break;
                    case 1:
                        imageView.setImageDrawable(activity.getResources()
                                .getDrawable(R.mipmap.ic_home_white_18dp));
                        textView.setText(data.get(position));
                        break;
                    case 2:
                        imageView.setImageDrawable(activity.getResources()
                                .getDrawable(R.mipmap.ic_speaker_phone_white_18dp));
                        textView.setText(data.get(position));
                        break;
                    case 3:
                        imageView.setImageDrawable(activity.getResources()
                                .getDrawable(R.drawable.ic_webhook));
                        textView.setText(data.get(position));
                        break;
                    case 4:
                        imageView.setImageDrawable(activity.getResources()
                                .getDrawable(R.mipmap.ic_notifications_white_18dp));
                        textView.setText(data.get(position));
                        break;
                    case 5:
                        imageView.setImageDrawable(activity.getResources()
                                .getDrawable(R.mipmap.ic_border_color_white_18dp));
                        textView.setText(data.get(position));
                        break;
                    case 6:
                        imageView.setImageDrawable(activity.getResources()
                                .getDrawable(R.mipmap.ic_help_outline_white_18dp));
                        textView.setText(data.get(position));
                        break;
                    default:
                }
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }


        return vi;
    }
}
