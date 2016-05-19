package zenithbank.com.gh.mibank.Adverts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class AdvertBroadcastReciever extends BroadcastReceiver
{
    Intent vid;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        try
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                String _path = bundle.getString(DownloadService.FILEPATH);
                int resultCode = bundle.getInt(DownloadService.RESULT);
                if (resultCode == Activity.RESULT_OK)
                {
                    String _type = bundle.getString("type");
                    if (_type.equalsIgnoreCase("video"))
                    {
                        vid = new Intent(context, VideoAdvert.class);
                    }
                    if (_type.equalsIgnoreCase("audio"))
                    {
                        vid = new Intent(context, AudioAdvert.class);
                    }

                    //download complete
                    vid.putExtra("Path", _path);
                    context.startActivity(vid);
                }
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }
}
