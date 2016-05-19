package zenithbank.com.gh.mibank.Notification.System;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.StringTokenizer;

import zenithbank.com.gh.mibank.Main.Classes.SystemConfigurationClass;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;
import zenithbank.com.gh.mibank.Notification.Classes.N_HistoryClass;
import zenithbank.com.gh.mibank.Notification.Classes.N_NotificationMessage;
import zenithbank.com.gh.mibank.Notification.Main.ReceiveActivity;
import zenithbank.com.gh.mibank.Notification.Main.Token;
import zenithbank.com.gh.mibank.R;


public class GcmIntentService extends IntentService
{

    int NOTIFICATION_ID;
    NotificationManager mNotificationManager;
    Uri noteSound = null;
    long[] vibrate = {0, 100, 200, 300};
    String[] splitStr;
    N_SystemDBEngine n_systemDBEngine;
    System_DBEngine system_dbEngine;
    String[] splitStr1;
    SystemConfigurationClass configurationClass;

    public GcmIntentService()
    {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        Calendar c = Calendar.getInstance();
        NOTIFICATION_ID = c.get(Calendar.SECOND) + 4;

        n_systemDBEngine = new N_SystemDBEngine();
        system_dbEngine = new System_DBEngine();
        n_systemDBEngine.Init(this);
        system_dbEngine.Init(this);

        configurationClass = system_dbEngine.getConfiguration();
        String payLoad = intent.getStringExtra("message");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        noteSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                // sendNotification("Send error: " + extras.toString(), "");
            }
            else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType))
            {
                //sendNotification("Deleted messages on server: " +
                //extras.toString(), "");
                // If it's a regular GCM message, do some work.
            }
        }

        if (payLoad.contains("TOKEN"))
        {

            if (configurationClass.TOKEN_ENABLE == 1)
            {
                String delimit = "||";
                StringTokenizer ress = new StringTokenizer(payLoad, delimit);
                splitStr = new String[ress.countTokens()];
                int index = 0;
                while (ress.hasMoreElements())
                {
                    splitStr[index++] = ress.nextToken();
                }
                Intent _token = new Intent(this, Token.class);
                _token.putExtra("TOKEN", splitStr[1]);
                _token.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(_token);
            }

        }

        if (payLoad.contains("ACTIVITY||"))
        {
            if (configurationClass.PROMPT_ENABLE == 1)
            {
                sendNotificationOld("Account Activity", payLoad);
            }
        }
        else
        {
            if (configurationClass.PROMPT_ENABLE == 1)
            {
                sendNotificationNew("Account Activity", payLoad);
            }

        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotificationNew(String msg, String Payload)
    {

        N_NotificationMessage.RootObject data =
                new Gson().fromJson(Payload, N_NotificationMessage.RootObject.class);
        n_systemDBEngine.updateNotificationTableNew(data);
        int requestID = NOTIFICATION_ID + 3;
        mNotificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myintent = new Intent(getApplicationContext(), ReceiveActivity.class);
        myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        myintent.putExtra("message", Payload);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                requestID, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.logo3);
        mBuilder.setContentTitle("Account Activity");
        mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg));
        mBuilder.setContentText(msg);
        if (configurationClass.ALERT_SOUND_ENABLE == 1)
        {
            mBuilder.setSound(noteSound);
            mBuilder.setVibrate(vibrate);
        }
        if (configurationClass.HOME_ALERT_ENABLE == 1)
        {
            mBuilder.setFullScreenIntent(contentIntent, true);
        }
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void sendNotificationOld(String msg, String Payload)
    {
        String delimit = "||";
        StringTokenizer ress = new StringTokenizer(Payload, delimit);
        splitStr1 = new String[ress.countTokens()];
        int index = 0;
        while (ress.hasMoreElements())
        {
            splitStr1[index++] = ress.nextToken();
        }
        String D1 = splitStr1[0].trim();
        String D2 = splitStr1[1].trim();
        if (splitStr1[0].equals("TOKEN"))
        {
            /*Intent _token = new Intent(this, Token.class);
            _token.putExtra("TOKEN", splitStr[1]);
            _token.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(_token);*/

        }
        if (splitStr1[0].equals("ACTIVITY"))
        {
            try
            {
                String _data = D2;
                N_HistoryClass.RootObject history = new N_HistoryClass.RootObject();
                String delimit1 = ";";
                StringTokenizer ress1 = new StringTokenizer(_data, delimit1);
                splitStr1 = new String[ress1.countTokens()];
                int index1 = 0;
                while (ress1.hasMoreElements())
                {
                    splitStr1[index1++] = ress1.nextToken();
                }

                history.trsxDate = splitStr1[0];
                history.trsxAcctno = splitStr1[1];
                history.trsxAmount = splitStr1[2];
                history.trsxType = splitStr1[3];
                history.trsxDecsription = splitStr1[4];
                history.trsxCurrency = splitStr1[5];
                history.trsxBranch = splitStr1[6];
                //history.trsxBalance = splitStr1[7];

                n_systemDBEngine.updateNotificationTable(history);
                String gsonString = new Gson().toJson(history);

                int requestID = NOTIFICATION_ID + 3;
                mNotificationManager = (NotificationManager)
                        getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


                Intent myintent = new Intent(getApplicationContext(), ReceiveActivity.class);
                myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


                myintent.putExtra("message", gsonString);
                PendingIntent contentIntent = PendingIntent.getActivity(this,
                        requestID, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setFullScreenIntent(contentIntent, true)
                                .setSmallIcon(R.drawable.logo3)
                                .setContentTitle("Account Activity")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(msg))
                                .setContentText(msg)
                                .setSound(noteSound)
                                .setVibrate(vibrate)
                                .setAutoCancel(true)
                                .setFullScreenIntent(contentIntent, true);
                mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            } catch (Exception ex)
            {
                ex.getMessage();
            }
        }
        if (splitStr1[0].equals("ADVERT-VID"))
        {
            /*Intent _advert = new Intent(this, DownloadService.class);
            _advert.putExtra("URL", splitStr[1]);
            _advert.putExtra("filename", splitStr[2]);
            _advert.putExtra("type", "video");
            try
            {
                context2.startService(_advert);
            } catch (Exception ex)
            {
                ex.getMessage();
            }*/

        }
        if (splitStr1[0].equals("ADVERT-AUD"))
        {
            /*Intent _advert = new Intent(this, DownloadService.class);
            _advert.putExtra("URL", splitStr[1]);
            _advert.putExtra("filename", splitStr[2]);
            _advert.putExtra("type", "audio");
            context.startService(_advert);*/
        }
        if (splitStr1[0].equals("ADVERT-TXT"))
        {
            /*Intent _advert = new Intent(this, TextAdvert.class);
            _advert.putExtra("ADVERT", splitStr[1]);
            _advert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(_advert);*/

        }


    }
}