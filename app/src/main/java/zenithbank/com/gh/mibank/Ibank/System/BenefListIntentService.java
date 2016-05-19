package zenithbank.com.gh.mibank.Ibank.System;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import zenithbank.com.gh.mibank.Ibank.Classes.I_BenefListErrorMessage;
import zenithbank.com.gh.mibank.Ibank.Classes.I_BenefListMessage;
import zenithbank.com.gh.mibank.Ibank.Classes.TransferBeneficiaryClass;

/**
 * Created by Robby on 4/5/2016.
 */
public class BenefListIntentService extends IntentService
{
    IbankServiceEngine ibankServiceEngine;
    IbankDatabaseEngine ibankDatabaseEngine;
    TransferBeneficiaryClass.RootObject[] data;

    public BenefListIntentService()
    {
        super("BenefListIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        ibankServiceEngine = new IbankServiceEngine();
        ibankDatabaseEngine = new IbankDatabaseEngine();
        ibankDatabaseEngine.Init(getApplicationContext());
        String res = ibankServiceEngine.GetTransferBeneficiares(ibankDatabaseEngine.getCookie());
        try
        {
            ArrayList<TransferBeneficiaryClass.RootObject> rData = new ArrayList<>();
            data = new Gson().fromJson(res, TransferBeneficiaryClass.RootObject[].class);
            Collections.addAll(rData, data);
            for (TransferBeneficiaryClass.RootObject dd : rData)
            {
                ibankDatabaseEngine.updateTransferBeneficiaryTable(
                        String.valueOf(dd.transferBeneficiary.id),
                        dd.transferBeneficiary.transferProduct.productName,
                        dd.transferBeneficiary.field1,
                        dd.transferBeneficiary.field2,
                        dd.transferBeneficiary.field3,
                        dd.transferBeneficiary.field4,
                        dd.transferBeneficiary.field5
                );
            }
            I_BusStation.getBus().post(new I_BenefListMessage(rData));
        } catch (Exception ex)
        {
            ex.getMessage();
            I_BusStation.getBus().post(new I_BenefListErrorMessage("NoData"));
        }

    }
}
