package zenithbank.com.gh.mibank.Adverts;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

import zenithbank.com.gh.mibank.R;

public class AudioAdvert extends FragmentActivity
{
    VideoView videoView;
    String path;
    Intent intent;
    String vidName;
    String filename;
    boolean leave = false;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_view_layout);

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                AudioAdvert.this.finish();
            }
        });
        intent = getIntent();
        path = getCacheFolder(this).getAbsolutePath();
        filename = intent.getStringExtra("filename");
        //filename = "dance.3gp";
        vidName = filename;
        try
        {
            mediaPlayer.setDataSource(path + "/" + filename);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public File getCacheFolder(Context context)
    {
        File cacheDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "cachefolder");
            if (!cacheDir.isDirectory())
            {

                cacheDir.mkdirs();
            }
        }
        if (!cacheDir.isDirectory())
        {

            cacheDir = context.getCacheDir(); //get system cache folder
        }
        return cacheDir;
    }

    @Override
    public void onBackPressed()
    {
        if (leave)
        {
            AudioAdvert.this.finish();
        }
        else
        {
            Toast.makeText(this, "Press Back Again To Exit Video", Toast.LENGTH_SHORT).show();
        }
        //super.onBackPressed();
    }
}
