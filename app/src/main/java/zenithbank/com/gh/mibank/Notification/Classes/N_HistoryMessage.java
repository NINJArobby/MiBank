package zenithbank.com.gh.mibank.Notification.Classes;

import java.util.ArrayList;

public class N_HistoryMessage
{
    private String msg;
    private ArrayList<N_HistoryClass.RootObject> data;

    public N_HistoryMessage(ArrayList<N_HistoryClass.RootObject> msg)
    {
        this.data = msg;
    }

    public ArrayList<N_HistoryClass.RootObject> getTransactions()
    {
        return this.data;
    }
}
