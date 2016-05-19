package zenithbank.com.gh.mibank.Main.Classes;

import java.util.ArrayList;


public class BalanceMessage
{
    private String msg;
    private ArrayList<BalanceClass.RootObject> data;

    public BalanceMessage(ArrayList<BalanceClass.RootObject> msg)
    {
        this.data = msg;
    }

    public ArrayList<BalanceClass.RootObject> getBalances()
    {
        return this.data;
    }
}
