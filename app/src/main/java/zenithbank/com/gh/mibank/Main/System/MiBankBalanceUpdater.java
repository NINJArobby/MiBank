package zenithbank.com.gh.mibank.Main.System;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import zenithbank.com.gh.mibank.Main.Classes.BalanceClass;
import zenithbank.com.gh.mibank.Main.Classes.BalanceMessage;

/**
 * Created by Robby on 3/9/2016.
 */
public class MiBankBalanceUpdater extends IntentService
{
    System_DBEngine system_dbEngine;

    public MiBankBalanceUpdater()
    {
        super("MiBankBalanceUpdater");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try
        {
            system_dbEngine = new System_DBEngine();
            system_dbEngine.Init(this);
            MibankServiceEngine mbEngine = new MibankServiceEngine();

            //fetch balances
            ArrayList<BalanceClass.RootObject> bData = new ArrayList<>();
            BalanceClass.RootObject[] _bData;
            String balance = mbEngine.getBalances(system_dbEngine.getRim());
            balance = balance.replace("\\", "");
            _bData = new Gson().fromJson(balance, BalanceClass.RootObject[].class);
            Collections.addAll(bData, _bData);
            BusStation.getBus().post(new BalanceMessage(bData));

        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }
}
