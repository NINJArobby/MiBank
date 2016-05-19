package zenithbank.com.gh.mibank.Main.Classes;

import java.util.ArrayList;

public class HistoryMessage
{
    private HistoryClass.RootObject msg;
    private ArrayList<HistoryClass.RootObject> data;

    public HistoryMessage(ArrayList<HistoryClass.RootObject> msg)
    {
        this.data = msg;
    }

    public ArrayList<HistoryClass.RootObject> getBalances()
    {
        return this.data;
    }
}
