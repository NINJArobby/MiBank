package zenithbank.com.gh.mibank.Main.System;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import zenithbank.com.gh.mibank.Main.Classes.HistoryClass;
import zenithbank.com.gh.mibank.Main.Classes.HistoryMessage;

/**
 * Created by Robby on 3/10/2016.
 */
public class MiBankHistoryUpdater extends IntentService
{
    System_DBEngine system_dbEngine;

    public MiBankHistoryUpdater()
    {
        super("MiBankHistoryUpdater");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        ArrayList<HistoryClass.RootObject> rData = new ArrayList<>();
        try
        {
            //test rim must be removed
            system_dbEngine = new System_DBEngine();
            system_dbEngine.Init(this);
            MibankServiceEngine mbEngine = new MibankServiceEngine();

            //get history
            HistoryClass.RootObject[] data;
            String rates = mbEngine.getRecentHistory(system_dbEngine.getRim());
            rates = rates.replace("\\", "");
            data = new Gson().fromJson(rates, HistoryClass.RootObject[].class);
            Collections.addAll(rData, data);
            system_dbEngine.updateTempTable(rData);
            BusStation.getBus().post(new HistoryMessage(rData));

        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }
}
