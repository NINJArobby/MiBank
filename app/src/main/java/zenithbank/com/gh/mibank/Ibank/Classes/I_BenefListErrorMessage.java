package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 4/5/2016.
 */
public class I_BenefListErrorMessage
{
    private String msg;
    private String data;

    public I_BenefListErrorMessage(String msg)
    {
        this.data = msg;
    }

    public String getBalances()
    {
        return this.data;
    }
}
