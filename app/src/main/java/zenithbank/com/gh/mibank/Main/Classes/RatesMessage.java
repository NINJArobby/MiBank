package zenithbank.com.gh.mibank.Main.Classes;


import java.util.ArrayList;

public class RatesMessage
{
    private ArrayList<CurrencyClass.RootObject> data;

    public RatesMessage(ArrayList<CurrencyClass.RootObject> msg)
    {
        this.data = msg;
    }

    public ArrayList<CurrencyClass.RootObject> getCurrencies()
    {
        return this.data;
    }
}
