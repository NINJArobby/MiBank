package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 11/19/2015.
 */
public class TokenReceivedEvent
{
    private String data;

    public TokenReceivedEvent(String data1)
    {
        this.data = data1;
    }

    public String getData()
    {
        return data;
    }
}
