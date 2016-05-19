package zenithbank.com.gh.mibank.Notification.System;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import zenithbank.com.gh.mibank.Main.System.BusStation;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;
import zenithbank.com.gh.mibank.Notification.Classes.N_HistoryClass;
import zenithbank.com.gh.mibank.Notification.Classes.N_HistoryMessage;
import zenithbank.com.gh.mibank.Notification.Classes.N_SearchErrorMessage;
import zenithbank.com.gh.mibank.Notification.Classes.TrsxSearchClass;


public class N_SearchUpdater extends IntentService
{
    System_DBEngine system_dbEngine;
    N_SystemService n_systemService;

    public N_SearchUpdater()
    {
        super("N_SearchUpdater");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        ArrayList<N_HistoryClass.RootObject> rData = new ArrayList<>();
        try
        {
            //test rim must be removed
            system_dbEngine = new System_DBEngine();
            system_dbEngine.Init(this);
            n_systemService = new N_SystemService();

            String fromDate = intent.getStringExtra("fromDate");
            fromDate = fromDate.replace("-", "");
            String toDate = intent.getStringExtra("toDate");
            toDate = toDate.replace("-", "");
            String acct = intent.getStringExtra("acct");

            TrsxSearchClass trsxSearchClass = new TrsxSearchClass();
            trsxSearchClass.acct = acct;
            trsxSearchClass.fromDate = fromDate;
            trsxSearchClass.toDate = toDate;

            String res = n_systemService.doSearch(trsxSearchClass);
            if (res.contains("catchME"))
            {
                throw new Exception("NoDataFound");
            }
            N_HistoryClass.RootObject[] response
                    = new Gson().fromJson(res, N_HistoryClass.RootObject[].class);

            ArrayList<N_HistoryClass.RootObject> _response = new ArrayList<>();
            Collections.addAll(_response, response);
            BusStation.getBus().post(new N_HistoryMessage(_response));

        } catch (Exception ex)
        {
            ex.getMessage();
            BusStation.getBus().post(new N_SearchErrorMessage(ex.getMessage()));
        }
    }
}
