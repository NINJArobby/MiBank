package zenithbank.com.gh.mibank.Ibank.Classes;

import java.util.ArrayList;

/**
 * Created by Robby on 4/5/2016.
 */
public class I_BenefListMessage
{
    private TransferBeneficiaryClass.RootObject msg;
    private ArrayList<TransferBeneficiaryClass.RootObject> data;

    public I_BenefListMessage(ArrayList<TransferBeneficiaryClass.RootObject> msg)
    {
        this.data = msg;
    }

    public ArrayList<TransferBeneficiaryClass.RootObject> getBalances()
    {
        return this.data;
    }
}
