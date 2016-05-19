package zenithbank.com.gh.mibank.Main.System;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import zenithbank.com.gh.mibank.Main.Classes.AuthenticateResponseClass;
import zenithbank.com.gh.mibank.Main.Classes.DeviceRegisterMessage;

/**
 * Created by Robby on 3/14/2016.
 */
public class MiBankDeviceRegister extends IntentService
{
    static final String TAG = "ZPROMPT";
    String SENDER_ID = "623887383809";
    String Password = "";
    GoogleCloudMessaging gcm;
    System_DBEngine system_dbEngine;
    MibankServiceEngine serviceEngine;

    public MiBankDeviceRegister()
    {
        super("MiBankDeviceRegister");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try
        {
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            String regid = gcm.register(SENDER_ID);
            if (regid.equals(""))
            {
                throw new Exception("DevIDNotFound");
            }
            system_dbEngine = new System_DBEngine();
            system_dbEngine.Init(getApplicationContext());
            serviceEngine = new MibankServiceEngine();
            AuthenticateResponseClass.Data data1 = system_dbEngine.getUserData();
            String regDev = serviceEngine.SaveDevID(data1.rim, regid);
            if (regDev.equals("success"))
            {
                //save to db
                system_dbEngine.saveDevID(regid);
                BusStation.getBus().post(new DeviceRegisterMessage("success"));
            }
            if (regDev.equals("device Already Registered"))
            {
                BusStation.getBus().post(new DeviceRegisterMessage("Registration Exist"));
            }
            if (regDev.equals("registration_error"))
            {
                BusStation.getBus().post(new DeviceRegisterMessage("Registration Exist"));
            }

        } catch (UnsupportedOperationException ex)
        {
            BusStation.getBus().post(new DeviceRegisterMessage("NotSupportedError"));

        } catch (Exception ex)
        {
            ex.printStackTrace();
            BusStation.getBus().post(new DeviceRegisterMessage("DevIDNotFound"));
        }
    }
}
