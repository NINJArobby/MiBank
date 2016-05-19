package zenithbank.com.gh.mibank.Main.System;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import zenithbank.com.gh.mibank.Main.Classes.CurrencyClass;
import zenithbank.com.gh.mibank.Main.Classes.RatesMessage;


public class MiBankRatesUpdater extends IntentService
{

    System_DBEngine system_dbEngine;

    public MiBankRatesUpdater()
    {
        super("MiBankRatesUpdater");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        ArrayList<CurrencyClass.RootObject> rData = new ArrayList<>();
        try
        {
            system_dbEngine = new System_DBEngine();
            system_dbEngine.Init(this);
            MibankServiceEngine mbEngine = new MibankServiceEngine();

            //get Rates

            CurrencyClass.RootObject[] data;
            String rates = mbEngine.getCurrncyRates();
            rates = rates.replace("\\", "");
            data = new Gson().fromJson(rates, CurrencyClass.RootObject[].class);
            Collections.addAll(rData, data);

        } catch (Exception ex)
        {
            ex.getMessage();
        }
        BusStation.getBus().post(new RatesMessage(rData));
    }

}
