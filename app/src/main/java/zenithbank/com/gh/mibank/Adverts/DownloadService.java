package zenithbank.com.gh.mibank.Adverts;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;


public class DownloadService extends IntentService
{
    public static final String _URL = "URL";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    Context context;
    String urlPath;
    String fileName;
    String type;
    private int result = Activity.RESULT_CANCELED;
    private ThinDownloadManager downloadManager;

    public DownloadService()
    {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        context = getApplicationContext();
        //Bundle bundle = intent.getExtras();
        urlPath = intent.getStringExtra(_URL);
        fileName = intent.getStringExtra(FILENAME);
        type = intent.getStringExtra("type");
        downloadManager = new ThinDownloadManager();

        Uri downloadUri = Uri.parse(urlPath);
        final Uri destinationUri = Uri.parse(getCacheFolder(this).getAbsolutePath()
                + "/" + fileName);
        File output = new File(getCacheFolder(this).getAbsolutePath(),
                fileName);
        if (output.exists())
        {
            output.delete();
        }
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri);
        downloadRequest.setDestinationURI(destinationUri);
        downloadRequest.setPriority(DownloadRequest.Priority.HIGH);
        downloadRequest.setDownloadListener(new DownloadStatusListener()
        {
            @Override
            public void onDownloadComplete(int id)
            {
                if (type.equalsIgnoreCase("video"))
                {
                    launchVideoAlert(destinationUri);
                }
                if (type.equalsIgnoreCase("audio"))
                {
                    launchAudioAdvert(destinationUri);
                }
            }

            @Override
            public void onDownloadFailed(int id, int errorCode, String errorMessage)
            {
                Log.d("DownloadError", errorMessage);
            }

            @Override
            public void onProgress(int id, long totalBytes, long downloadedBytes, int progress)
            {
                Log.d("Downloading...", String.valueOf(progress));
            }
        });
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

    void launchVideoAlert(Uri destinationUri)
    {
        Intent _advert = new Intent(getApplicationContext(), VideoAdvert.class);
        _advert.putExtra("outputPath", destinationUri.toString());
        _advert.putExtra("filename", fileName);
        _advert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(_advert);
    }

    void launchAudioAdvert(Uri destinationUri)
    {
        Intent _advert = new Intent(getApplicationContext(), AudioAdvert.class);
        _advert.putExtra("outputPath", destinationUri.toString());
        _advert.putExtra("filename", fileName);
        _advert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(_advert);
    }

}
