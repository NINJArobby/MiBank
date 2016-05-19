package zenithbank.com.gh.mibank.Ibank.BusMessages;

import java.util.ArrayList;

/**
 * Created by Robby on 4/1/2016.
 */
public class AccountsMessage
{
    private String msg;
    private ArrayList<String> data;

    public AccountsMessage(ArrayList<String> msg)
    {
        this.data = msg;
    }

    public ArrayList<String> getTransactions()
    {
        return this.data;
    }
}
