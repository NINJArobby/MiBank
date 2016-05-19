package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 4/4/2016.
 */
public class I_SmsMessage
{
    private String msg;


    public I_SmsMessage(String msg)
    {
        this.msg = msg;
    }

    public String getMessage()
    {
        return this.msg;
    }
}
