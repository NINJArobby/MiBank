package zenithbank.com.gh.mibank.Ibank.System;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import zenithbank.com.gh.mibank.Ibank.Classes.I_SmsMessage;


public class SMSListener extends BroadcastReceiver
{
    String msg_from;
    OnSmsRecievedListener smsRecievedListener;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            {
                Bundle bundle = intent.getExtras();   //---get the SMS message passed in---
                SmsMessage[] msgs = null;

                if (bundle != null)
                {
                    //---retrieve the SMS message received---
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++)
                    {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        if (msgBody.contains("token"))
                        {
                            int count = msgBody.length();
                            msgBody = msgBody.substring(15, count);
                            I_BusStation.getBus().post(new I_SmsMessage(msgBody));
                            //smsRecievedListener.smsRecieved(msgBody);
                        }

                    }
                }
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public interface OnSmsRecievedListener
    {
        void smsRecieved(String token);
    }


}
