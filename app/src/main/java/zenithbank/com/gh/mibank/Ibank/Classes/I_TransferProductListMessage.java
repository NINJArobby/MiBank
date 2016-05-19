package zenithbank.com.gh.mibank.Ibank.Classes;

import java.util.ArrayList;

/**
 * Created by Robby on 4/6/2016.
 */
public class I_TransferProductListMessage
{
    private TransferProductClass.RootObject msg;
    private ArrayList<TransferProductClass.RootObject> data;

    public I_TransferProductListMessage(ArrayList<TransferProductClass.RootObject> msg)
    {
        this.data = msg;
    }

    public ArrayList<TransferProductClass.RootObject> getBalances()
    {
        return this.data;
    }
}
