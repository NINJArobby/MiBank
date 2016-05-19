package zenithbank.com.gh.mibank.Adverts;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import zenithbank.com.gh.mibank.R;


public class VideoAdvert extends FragmentActivity
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
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.video_view_layout);
            intent = getIntent();
            path = getCacheFolder(this).getAbsolutePath();
            filename = intent.getStringExtra("filename");
            //filename = "dance.3gp";
            vidName = filename;

            PlayVId2();
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("Position", videoView.getCurrentPosition());
        videoView.pause();
    }

    public void PlayVId2()
    {

        //set video path and start it
        videoView = (VideoView) findViewById(R.id.advertVid);
        videoView.setVideoURI(Uri.parse(path + "/" + filename));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                videoView.seekTo(position);
                if (position == 0)
                {
                    videoView.start();
                }
                else
                {
                    videoView.pause();
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                VideoAdvert.this.finish();
            }
        });
        videoView.start();
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
            VideoAdvert.this.finish();
        }
        else
        {
            Toast.makeText(this, "Press Back Again To Exit Video", Toast.LENGTH_SHORT).show();
        }
        //super.onBackPressed();
    }


}