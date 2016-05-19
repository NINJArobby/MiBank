package zenithbank.com.gh.mibank.Main.System;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import zenithbank.com.gh.mibank.Main.Classes.InvestRatesClass;
import zenithbank.com.gh.mibank.Main.Classes.InvestRatesMessage;

public class MiBankInvestmentRatesUpdater extends IntentService
{
    System_DBEngine system_dbEngine;

    public MiBankInvestmentRatesUpdater()
    {
        super("MiBankInvestmentRatesUpdater");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        system_dbEngine = new System_DBEngine();
        system_dbEngine.Init(this);
        MibankServiceEngine mbEngine = new MibankServiceEngine();

        //get Rates
        ArrayList<InvestRatesClass.RootObject> rData = new ArrayList<>();
        InvestRatesClass.RootObject[] data;
        String rates = mbEngine.getInvestmentRates();
        rates = rates.replace("\\", "");
        data = new Gson().fromJson(rates, InvestRatesClass.RootObject[].class);
        Collections.addAll(rData, data);
        BusStation.getBus().post(new InvestRatesMessage(rData));
    }
}
