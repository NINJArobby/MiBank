package zenithbank.com.gh.mibank.Main.Classes;

import java.util.ArrayList;


public class InvestRatesMessage
{
    private String msg;
    private ArrayList<InvestRatesClass.RootObject> data;

    public InvestRatesMessage(ArrayList<InvestRatesClass.RootObject> msg)
    {
        this.data = msg;
    }

    public ArrayList<InvestRatesClass.RootObject> getRates()
    {
        return this.data;
    }
}
