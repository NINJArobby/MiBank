package zenithbank.com.gh.mibank.Ibank.System;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import zenithbank.com.gh.mibank.Ibank.Classes.I_TransferProductListMessage;
import zenithbank.com.gh.mibank.Ibank.Classes.TransferProductClass;

/**
 * Created by Robby on 4/6/2016.
 */
public class I_TransferProductUpdater extends IntentService
{
    IbankServiceEngine ibankServiceEngine;
    IbankDatabaseEngine ibankDatabaseEngine;


    public I_TransferProductUpdater()
    {
        super("I_TransferProductUpdater");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        ibankDatabaseEngine = new IbankDatabaseEngine();
        ibankDatabaseEngine.Init(getApplicationContext());
        ibankServiceEngine = new IbankServiceEngine();
        String res = ibankServiceEngine.GetTransferProducts(ibankDatabaseEngine.getCookie());
        TransferProductClass.RootObject[] response
                = new Gson().fromJson(res, TransferProductClass.RootObject[].class);

        ArrayList<TransferProductClass.RootObject> _response = new ArrayList<>();
        Collections.addAll(_response, response);
        I_BusStation.getBus().post(new I_TransferProductListMessage(_response));
    }
}
